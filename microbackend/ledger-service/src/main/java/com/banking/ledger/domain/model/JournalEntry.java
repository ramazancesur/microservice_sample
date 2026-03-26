package com.banking.ledger.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Immutable aggregate root for a double-entry journal posting.
 * Invariant: sum of all DEBIT amounts == sum of all CREDIT amounts.
 */
public class JournalEntry {

    private final UUID id;
    private final String referenceId;
    private final String description;
    private final List<LedgerEntry> entries;
    private final LocalDateTime postedAt;

    private JournalEntry(UUID id, String referenceId, String description,
                         List<LedgerEntry> entries, LocalDateTime postedAt) {
        this.id = id;
        this.referenceId = referenceId;
        this.description = description;
        this.entries = Collections.unmodifiableList(entries);
        this.postedAt = postedAt;
    }

    /**
     * Factory that validates balance before creating the journal entry.
     * Throws {@link UnbalancedJournalException} when debits ≠ credits.
     */
    public static JournalEntry post(String referenceId, String description, List<LedgerEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            throw new IllegalArgumentException("Journal entry must have at least one ledger entry");
        }
        assertBalanced(entries);
        return new JournalEntry(UUID.randomUUID(), referenceId, description, entries, LocalDateTime.now());
    }

    /** Reconstitute from persistence — skips balance check (already validated on first post). */
    public static JournalEntry reconstitute(UUID id, String referenceId, String description,
                                            List<LedgerEntry> entries, LocalDateTime postedAt) {
        return new JournalEntry(id, referenceId, description, entries, postedAt);
    }

    private static void assertBalanced(List<LedgerEntry> entries) {
        BigDecimal totalDebits = entries.stream()
                .filter(e -> e.getType() == EntryType.DEBIT)
                .map(LedgerEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCredits = entries.stream()
                .filter(e -> e.getType() == EntryType.CREDIT)
                .map(LedgerEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalDebits.compareTo(totalCredits) != 0) {
            throw new UnbalancedJournalException(totalDebits, totalCredits);
        }
    }

    public UUID getId() { return id; }
    public String getReferenceId() { return referenceId; }
    public String getDescription() { return description; }
    public List<LedgerEntry> getEntries() { return entries; }
    public LocalDateTime getPostedAt() { return postedAt; }
}
