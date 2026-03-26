package com.banking.ledger.application.service;

import com.banking.ledger.application.dto.JournalEntryResponse;
import com.banking.ledger.application.usecase.GetMovementsUseCase;
import com.banking.ledger.domain.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMovementsService implements GetMovementsUseCase {

    private final JournalEntryRepository journalEntryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<JournalEntryResponse> execute(UUID accountId, LocalDateTime from, LocalDateTime to) {
        return journalEntryRepository.findByAccountId(accountId, from, to).stream()
                .map(JournalEntryResponse::from)
                .toList();
    }
}
