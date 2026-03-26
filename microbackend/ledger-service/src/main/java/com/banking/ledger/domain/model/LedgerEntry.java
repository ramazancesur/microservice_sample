package com.banking.ledger.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/** One line in a journal entry — either a debit or a credit side. */
public class LedgerEntry {

    private final UUID id;
    private final UUID accountId;
    private final EntryType type;
    private final BigDecimal amount;
    private final String currency;

    public LedgerEntry(UUID id, UUID accountId, EntryType type, BigDecimal amount, String currency) {
        Objects.requireNonNull(id, "Entry id required");
        Objects.requireNonNull(accountId, "Account id required");
        Objects.requireNonNull(type, "Entry type required");
        Objects.requireNonNull(amount, "Amount required");
        Objects.requireNonNull(currency, "Currency required");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Ledger entry amount must be positive");
        }
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
    }

    public static LedgerEntry debit(UUID accountId, BigDecimal amount, String currency) {
        return new LedgerEntry(UUID.randomUUID(), accountId, EntryType.DEBIT, amount, currency);
    }

    public static LedgerEntry credit(UUID accountId, BigDecimal amount, String currency) {
        return new LedgerEntry(UUID.randomUUID(), accountId, EntryType.CREDIT, amount, currency);
    }

    public UUID getId() { return id; }
    public UUID getAccountId() { return accountId; }
    public EntryType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
}
