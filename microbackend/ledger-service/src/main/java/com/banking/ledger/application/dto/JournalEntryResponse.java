package com.banking.ledger.application.dto;

import com.banking.ledger.domain.model.EntryType;
import com.banking.ledger.domain.model.JournalEntry;
import com.banking.ledger.domain.model.LedgerEntry;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record JournalEntryResponse(
        UUID id,
        String referenceId,
        String description,
        List<LedgerEntryResponse> entries,
        LocalDateTime postedAt
) {
    public record LedgerEntryResponse(UUID id, UUID accountId, EntryType type, BigDecimal amount, String currency) {
        static LedgerEntryResponse from(LedgerEntry entry) {
            return new LedgerEntryResponse(
                    entry.getId(), entry.getAccountId(),
                    entry.getType(), entry.getAmount(), entry.getCurrency());
        }
    }

    public static JournalEntryResponse from(JournalEntry journalEntry) {
        List<LedgerEntryResponse> entryResponses = journalEntry.getEntries().stream()
                .map(LedgerEntryResponse::from)
                .toList();
        return new JournalEntryResponse(
                journalEntry.getId(),
                journalEntry.getReferenceId(),
                journalEntry.getDescription(),
                entryResponses,
                journalEntry.getPostedAt()
        );
    }
}
