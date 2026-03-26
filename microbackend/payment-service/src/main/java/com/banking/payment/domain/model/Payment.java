package com.banking.payment.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {

    private final UUID id;
    private final String idempotencyKey;
    private final UUID sourceAccountId;
    private final UUID targetAccountId;
    private final BigDecimal amount;
    private final String currency;
    private PaymentState state;
    private String failureReason;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Payment(UUID id, String idempotencyKey, UUID sourceAccountId,
                    UUID targetAccountId, BigDecimal amount, String currency,
                    PaymentState state, String failureReason,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.idempotencyKey = idempotencyKey;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.currency = currency;
        this.state = state;
        this.failureReason = failureReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Payment initiate(String idempotencyKey, UUID sourceAccountId,
                                   UUID targetAccountId, BigDecimal amount, String currency) {
        validateAmount(amount);
        validateCurrency(currency);
        if (sourceAccountId.equals(targetAccountId)) {
            throw new IllegalArgumentException("Source and target accounts must differ");
        }
        LocalDateTime now = LocalDateTime.now();
        return new Payment(UUID.randomUUID(), idempotencyKey, sourceAccountId,
                targetAccountId, amount, currency, PaymentState.PENDING, null, now, now);
    }

    public static Payment reconstitute(UUID id, String idempotencyKey, UUID sourceAccountId,
                                       UUID targetAccountId, BigDecimal amount, String currency,
                                       PaymentState state, String failureReason,
                                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Payment(id, idempotencyKey, sourceAccountId, targetAccountId,
                amount, currency, state, failureReason, createdAt, updatedAt);
    }

    public void markPosted() {
        requireState(PaymentState.PENDING, "mark as posted");
        this.state = PaymentState.POSTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void markFailed(String reason) {
        requireState(PaymentState.PENDING, "mark as failed");
        this.state = PaymentState.FAILED;
        this.failureReason = reason;
        this.updatedAt = LocalDateTime.now();
    }

    public void reverse() {
        if (this.state != PaymentState.POSTED) {
            throw new IllegalStateException("Only POSTED payments can be reversed");
        }
        this.state = PaymentState.REVERSED;
        this.updatedAt = LocalDateTime.now();
    }

    private void requireState(PaymentState expected, String action) {
        if (this.state != expected) {
            throw new IllegalStateException(
                    "Cannot " + action + " from state " + this.state);
        }
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
    }

    private static void validateCurrency(String currency) {
        if (currency == null || currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be a 3-letter ISO 4217 code");
        }
    }

    public UUID getId() { return id; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public UUID getSourceAccountId() { return sourceAccountId; }
    public UUID getTargetAccountId() { return targetAccountId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public PaymentState getState() { return state; }
    public String getFailureReason() { return failureReason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
