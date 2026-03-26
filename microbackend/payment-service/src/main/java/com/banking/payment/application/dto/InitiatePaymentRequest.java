package com.banking.payment.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record InitiatePaymentRequest(
        @NotBlank(message = "Idempotency key is required") String idempotencyKey,
        @NotNull(message = "Source account ID is required") UUID sourceAccountId,
        @NotNull(message = "Target account ID is required") UUID targetAccountId,
        @NotNull @DecimalMin(value = "0.01", message = "Amount must be positive") BigDecimal amount,
        @NotBlank @Size(min = 3, max = 3, message = "Currency must be 3-letter ISO 4217 code") String currency
) {}
