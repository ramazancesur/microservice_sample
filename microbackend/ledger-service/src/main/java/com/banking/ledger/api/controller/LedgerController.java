package com.banking.ledger.api.controller;

import com.banking.ledger.application.dto.AccountBalanceResponse;
import com.banking.ledger.application.dto.JournalEntryResponse;
import com.banking.ledger.application.dto.PostJournalRequest;
import com.banking.ledger.application.usecase.GetBalanceUseCase;
import com.banking.ledger.application.usecase.GetMovementsUseCase;
import com.banking.ledger.application.usecase.PostJournalUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(name = "Ledger", description = "Double-entry accounting API")
@RestController
@RequestMapping("/api/v1/ledger")
@RequiredArgsConstructor
public class LedgerController {

    private final PostJournalUseCase postJournalUseCase;
    private final GetBalanceUseCase getBalanceUseCase;
    private final GetMovementsUseCase getMovementsUseCase;

    @Operation(summary = "Post a balanced journal entry")
    @PostMapping("/journal")
    @ResponseStatus(HttpStatus.CREATED)
    public JournalEntryResponse postJournal(@Valid @RequestBody PostJournalRequest request) {
        return postJournalUseCase.execute(request);
    }

    @Operation(summary = "Get account balance from ledger")
    @GetMapping("/balance/{accountId}")
    public AccountBalanceResponse getBalance(
            @PathVariable UUID accountId,
            @RequestParam(defaultValue = "TRY") String currency) {
        return getBalanceUseCase.execute(accountId, currency);
    }

    @Operation(summary = "Get account movements in a date range")
    @GetMapping("/movements/{accountId}")
    public List<JournalEntryResponse> getMovements(
            @PathVariable UUID accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return getMovementsUseCase.execute(accountId, from, to);
    }
}
