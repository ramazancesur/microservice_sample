package com.banking.ledger.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JournalEntryJpaRepository extends JpaRepository<JournalEntryJpaEntity, UUID> {

    Optional<JournalEntryJpaEntity> findByReferenceId(String referenceId);

    @Query("""
            SELECT DISTINCT je FROM JournalEntryJpaEntity je
            JOIN je.entries le
            WHERE le.accountId = :accountId
              AND je.postedAt BETWEEN :from AND :to
            ORDER BY je.postedAt ASC
            """)
    List<JournalEntryJpaEntity> findByAccountIdAndPostedAtBetween(
            @Param("accountId") UUID accountId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
