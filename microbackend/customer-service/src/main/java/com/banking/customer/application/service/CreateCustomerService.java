package com.banking.customer.application.service;

import com.banking.customer.application.dto.CreateCustomerRequest;
import com.banking.customer.application.dto.CustomerResponse;
import com.banking.customer.application.usecase.CreateCustomerUseCase;
import com.banking.customer.domain.exception.DuplicateEmailException;
import com.banking.customer.domain.model.Customer;
import com.banking.customer.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateCustomerService implements CreateCustomerUseCase {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerResponse execute(CreateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException(request.email());
        }

        Customer customer = Customer.create(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phone()
        );

        Customer saved = customerRepository.save(customer);
        log.info("Created customer id={} email={}", saved.getId(), saved.getEmail());
        return CustomerResponse.from(saved);
    }
}
