package com.banking.customer.application.service;

import com.banking.customer.application.dto.CustomerResponse;
import com.banking.customer.application.usecase.GetCustomerUseCase;
import com.banking.customer.domain.exception.CustomerNotFoundException;
import com.banking.customer.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCustomerService implements GetCustomerUseCase {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse execute(UUID id) {
        return customerRepository.findById(id)
                .map(CustomerResponse::from)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
