package com.banking.ledger.application.usecase;

import com.banking.ledger.application.dto.AccountBalanceResponse;

import java.util.UUID;

public interface GetBalanceUseCase {
    AccountBalanceResponse execute(UUID accountId, String currency);
}
