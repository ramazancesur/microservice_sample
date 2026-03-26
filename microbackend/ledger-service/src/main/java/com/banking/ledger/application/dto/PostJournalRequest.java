package com.banking.ledger.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PostJournalRequest(
        @NotBlank(message = "Reference ID is required") String referenceId,
        @NotBlank(message = "Description is required") String description,
        @NotEmpty(message = "At least one ledger entry is required") @Valid List<LedgerEntryRequest> entries
) {}
