package com.banking.account.api.controller;

import com.banking.account.application.dto.AccountResponse;
import com.banking.account.application.dto.OpenAccountRequest;
import com.banking.account.application.usecase.GetAccountUseCase;
import com.banking.account.application.usecase.OpenAccountUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Accounts", description = "Account management API")
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final OpenAccountUseCase openAccountUseCase;
    private final GetAccountUseCase getAccountUseCase;

    @Operation(summary = "Open a new account")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse openAccount(@Valid @RequestBody OpenAccountRequest request) {
        return openAccountUseCase.execute(request);
    }

    @Operation(summary = "Get account by ID")
    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable UUID id) {
        return getAccountUseCase.findById(id);
    }

    @Operation(summary = "List accounts by customer")
    @GetMapping("/customer/{customerId}")
    public List<AccountResponse> getAccountsByCustomer(@PathVariable UUID customerId) {
        return getAccountUseCase.findByCustomerId(customerId);
    }

    @Operation(summary = "Search/list accounts. Use ?q=accountNumber to filter.")
    @GetMapping
    public Page<AccountResponse> listAccounts(
            @RequestParam(required = false) String q,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        if (q != null && !q.isBlank()) {
            return getAccountUseCase.search(q.trim(), pageable);
        }
        return getAccountUseCase.findAll(pageable);
    }
}
