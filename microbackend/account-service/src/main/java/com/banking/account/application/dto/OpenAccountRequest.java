package com.banking.account.application.dto;

import com.banking.account.domain.model.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record OpenAccountRequest(
        @NotNull(message = "Customer ID is required") UUID customerId,
        @NotNull(message = "Account type is required") AccountType type,
        @NotBlank @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO 4217 code") String currency
) {}
