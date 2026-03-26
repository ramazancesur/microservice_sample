package com.banking.customer.application.service;

import com.banking.customer.application.dto.CustomerResponse;
import com.banking.customer.application.usecase.ListCustomersUseCase;
import com.banking.customer.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListCustomersService implements ListCustomersUseCase {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> execute(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(CustomerResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> search(String keyword, Pageable pageable) {
        return customerRepository.search(keyword, pageable)
                .map(CustomerResponse::from);
    }
}
