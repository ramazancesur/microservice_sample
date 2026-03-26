package com.banking.customer.application.usecase;

import com.banking.customer.application.dto.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListCustomersUseCase {
    Page<CustomerResponse> execute(Pageable pageable);
    Page<CustomerResponse> search(String keyword, Pageable pageable);
}
