package com.banking.ledger.application.dto;

import com.banking.ledger.domain.model.EntryType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record LedgerEntryRequest(
        @NotNull UUID accountId,
        @NotNull EntryType type,
        @NotNull @DecimalMin(value = "0.01", message = "Amount must be greater than 0") BigDecimal amount,
        @NotBlank @Size(min = 3, max = 3) String currency
) {}
