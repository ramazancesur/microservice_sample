package com.banking.customer.application.usecase;

import com.banking.customer.application.dto.CustomerResponse;
import com.banking.customer.application.dto.UpdateCustomerRequest;

import java.util.UUID;

public interface UpdateCustomerUseCase {
    CustomerResponse execute(UUID id, UpdateCustomerRequest request);
}
