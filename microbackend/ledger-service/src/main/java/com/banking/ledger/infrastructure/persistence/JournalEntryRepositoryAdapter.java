package com.banking.ledger.infrastructure.persistence;

import com.banking.ledger.domain.model.JournalEntry;
import com.banking.ledger.domain.model.LedgerEntry;
import com.banking.ledger.domain.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JournalEntryRepositoryAdapter implements JournalEntryRepository {

    private final JournalEntryJpaRepository jpaRepository;

    @Override
    public JournalEntry save(JournalEntry journalEntry) {
        JournalEntryJpaEntity entity = toEntity(journalEntry);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<JournalEntry> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<JournalEntry> findByReferenceId(String referenceId) {
        return jpaRepository.findByReferenceId(referenceId).map(this::toDomain);
    }

    @Override
    public List<JournalEntry> findByAccountId(UUID accountId, LocalDateTime from, LocalDateTime to) {
        return jpaRepository.findByAccountIdAndPostedAtBetween(accountId, from, to).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Page<JournalEntry> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(this::toDomain);
    }

    private JournalEntryJpaEntity toEntity(JournalEntry domain) {
        JournalEntryJpaEntity entity = new JournalEntryJpaEntity();
        entity.setId(domain.getId());
        entity.setReferenceId(domain.getReferenceId());
        entity.setDescription(domain.getDescription());
        entity.setPostedAt(domain.getPostedAt());

        List<LedgerEntryJpaEntity> lineEntities = domain.getEntries().stream()
                .map(le -> toLedgerEntity(le, entity))
                .toList();
        entity.getEntries().addAll(lineEntities);
        return entity;
    }

    private LedgerEntryJpaEntity toLedgerEntity(LedgerEntry entry, JournalEntryJpaEntity parent) {
        LedgerEntryJpaEntity entity = new LedgerEntryJpaEntity();
        entity.setId(entry.getId());
        entity.setJournalEntry(parent);
        entity.setAccountId(entry.getAccountId());
        entity.setType(entry.getType());
        entity.setAmount(entry.getAmount());
        entity.setCurrency(entry.getCurrency());
        return entity;
    }

    private JournalEntry toDomain(JournalEntryJpaEntity entity) {
        List<LedgerEntry> entries = entity.getEntries().stream()
                .map(le -> new LedgerEntry(
                        le.getId(), le.getAccountId(),
                        le.getType(), le.getAmount(), le.getCurrency()))
                .toList();
        return JournalEntry.reconstitute(
                entity.getId(), entity.getReferenceId(),
                entity.getDescription(), entries, entity.getPostedAt());
    }
}
