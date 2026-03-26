package com.banking.ledger.domain.repository;

import com.banking.ledger.domain.model.JournalEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JournalEntryRepository {

    JournalEntry save(JournalEntry journalEntry);

    Optional<JournalEntry> findById(UUID id);

    Optional<JournalEntry> findByReferenceId(String referenceId);

    List<JournalEntry> findByAccountId(UUID accountId, LocalDateTime from, LocalDateTime to);

    Page<JournalEntry> findAll(Pageable pageable);
}
