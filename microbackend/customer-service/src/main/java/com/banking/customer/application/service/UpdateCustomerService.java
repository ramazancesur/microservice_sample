package com.banking.customer.application.service;

import com.banking.customer.application.dto.CustomerResponse;
import com.banking.customer.application.dto.UpdateCustomerRequest;
import com.banking.customer.application.usecase.UpdateCustomerUseCase;
import com.banking.customer.domain.exception.CustomerNotFoundException;
import com.banking.customer.domain.model.Customer;
import com.banking.customer.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateCustomerService implements UpdateCustomerUseCase {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerResponse execute(UUID id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        customer.updateProfile(request.firstName(), request.lastName(), request.phone());
        Customer saved = customerRepository.save(customer);
        log.info("Updated customer id={}", id);
        return CustomerResponse.from(saved);
    }
}
