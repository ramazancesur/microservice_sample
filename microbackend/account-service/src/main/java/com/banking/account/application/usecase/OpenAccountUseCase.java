package com.banking.account.application.usecase;

import com.banking.account.application.dto.AccountResponse;
import com.banking.account.application.dto.OpenAccountRequest;

public interface OpenAccountUseCase {
    AccountResponse execute(OpenAccountRequest request);
}
