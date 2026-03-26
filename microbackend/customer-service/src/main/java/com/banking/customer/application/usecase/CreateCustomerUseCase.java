package com.banking.customer.application.usecase;

import com.banking.customer.application.dto.CreateCustomerRequest;
import com.banking.customer.application.dto.CustomerResponse;

public interface CreateCustomerUseCase {
    CustomerResponse execute(CreateCustomerRequest request);
}
