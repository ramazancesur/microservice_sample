package com.banking.customer.application.usecase;

import com.banking.customer.application.dto.CustomerResponse;

import java.util.UUID;

public interface GetCustomerUseCase {
    CustomerResponse execute(UUID id);
}
