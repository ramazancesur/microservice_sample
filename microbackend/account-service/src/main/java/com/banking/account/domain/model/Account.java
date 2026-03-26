package com.banking.account.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Account {

    private final UUID id;
    private final UUID customerId;
    private final String accountNumber;
    private final AccountType type;
    private final String currency;
    private Money balance;
    private AccountStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Account(UUID id, UUID customerId, String accountNumber, AccountType type,
                    String currency, Money balance, AccountStatus status,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.currency = currency;
        this.balance = balance;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Account open(UUID customerId, AccountType type, String currency) {
        validateCurrency(currency);
        LocalDateTime now = LocalDateTime.now();
        return new Account(
                UUID.randomUUID(),
                customerId,
                generateAccountNumber(),
                type,
                currency,
                Money.zero(currency),
                AccountStatus.ACTIVE,
                now,
                now
        );
    }

    public static Account reconstitute(UUID id, UUID customerId, String accountNumber,
                                       AccountType type, String currency, Money balance,
                                       AccountStatus status, LocalDateTime createdAt,
                                       LocalDateTime updatedAt) {
        return new Account(id, customerId, accountNumber, type, currency,
                balance, status, createdAt, updatedAt);
    }

    public void credit(Money amount) {
        requireActive();
        this.balance = this.balance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void debit(Money amount) {
        requireActive();
        this.balance = this.balance.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void suspend() {
        this.status = AccountStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void close() {
        if (!balance.amount().equals(java.math.BigDecimal.ZERO.setScale(2))) {
            throw new IllegalStateException("Cannot close account with non-zero balance: " + balance);
        }
        this.status = AccountStatus.CLOSED;
        this.updatedAt = LocalDateTime.now();
    }

    private void requireActive() {
        if (this.status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active: " + id);
        }
    }

    private static void validateCurrency(String currency) {
        if (currency == null || currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be a 3-letter ISO 4217 code");
        }
    }

    private static String generateAccountNumber() {
        // Simplified IBAN-like number for demo; replace with proper IBAN generation
        return "TR" + String.format("%018d", (long) (Math.random() * 1_000_000_000_000_000_000L));
    }

    public UUID getId() { return id; }
    public UUID getCustomerId() { return customerId; }
    public String getAccountNumber() { return accountNumber; }
    public AccountType getType() { return type; }
    public String getCurrency() { return currency; }
    public Money getBalance() { return balance; }
    public AccountStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
