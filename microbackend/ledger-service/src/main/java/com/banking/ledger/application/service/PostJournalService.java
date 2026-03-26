package com.banking.ledger.application.service;

import com.banking.ledger.application.dto.JournalEntryResponse;
import com.banking.ledger.application.dto.LedgerEntryRequest;
import com.banking.ledger.application.dto.PostJournalRequest;
import com.banking.ledger.application.usecase.PostJournalUseCase;
import com.banking.ledger.domain.model.JournalEntry;
import com.banking.ledger.domain.model.LedgerEntry;
import com.banking.ledger.domain.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostJournalService implements PostJournalUseCase {

    private final JournalEntryRepository journalEntryRepository;

    @Override
    @Transactional
    public JournalEntryResponse execute(PostJournalRequest request) {
        // Idempotency: if referenceId already posted, return existing entry
        return journalEntryRepository.findByReferenceId(request.referenceId())
                .map(JournalEntryResponse::from)
                .orElseGet(() -> postNew(request));
    }

    private JournalEntryResponse postNew(PostJournalRequest request) {
        List<LedgerEntry> ledgerEntries = request.entries().stream()
                .map(this::toEntry)
                .toList();

        JournalEntry journalEntry = JournalEntry.post(
                request.referenceId(),
                request.description(),
                ledgerEntries
        );

        JournalEntry saved = journalEntryRepository.save(journalEntry);
        log.info("Posted journal entry id={} referenceId={}", saved.getId(), saved.getReferenceId());
        return JournalEntryResponse.from(saved);
    }

    private LedgerEntry toEntry(LedgerEntryRequest req) {
        return new LedgerEntry(
                java.util.UUID.randomUUID(),
                req.accountId(),
                req.type(),
                req.amount(),
                req.currency()
        );
    }
}
