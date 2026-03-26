package com.banking.account.application.usecase;

import com.banking.account.application.dto.AccountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface GetAccountUseCase {
    AccountResponse findById(UUID id);
    List<AccountResponse> findByCustomerId(UUID customerId);
    Page<AccountResponse> search(String keyword, Pageable pageable);
    Page<AccountResponse> findAll(Pageable pageable);
}
