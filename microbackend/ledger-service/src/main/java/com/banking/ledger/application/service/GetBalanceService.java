package com.banking.ledger.application.service;

import com.banking.ledger.application.dto.AccountBalanceResponse;
import com.banking.ledger.application.usecase.GetBalanceUseCase;
import com.banking.ledger.domain.model.EntryType;
import com.banking.ledger.domain.model.JournalEntry;
import com.banking.ledger.domain.model.LedgerEntry;
import com.banking.ledger.domain.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetBalanceService implements GetBalanceUseCase {

    private final JournalEntryRepository journalEntryRepository;

    @Override
    @Transactional(readOnly = true)
    public AccountBalanceResponse execute(UUID accountId, String currency) {
        // Fetch all entries for this account from the beginning of time
        List<JournalEntry> entries = journalEntryRepository
                .findByAccountId(accountId, LocalDateTime.MIN, LocalDateTime.now());

        BigDecimal balance = entries.stream()
                .flatMap(je -> je.getEntries().stream())
                .filter(e -> e.getAccountId().equals(accountId))
                .filter(e -> e.getCurrency().equals(currency))
                .map(e -> e.getType() == EntryType.CREDIT
                        ? e.getAmount()
                        : e.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AccountBalanceResponse(accountId, balance, currency);
    }
}
