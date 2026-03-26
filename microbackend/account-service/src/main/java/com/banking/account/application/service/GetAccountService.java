package com.banking.account.application.service;

import com.banking.account.application.dto.AccountResponse;
import com.banking.account.application.usecase.GetAccountUseCase;
import com.banking.account.domain.exception.AccountNotFoundException;
import com.banking.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAccountService implements GetAccountUseCase {

    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public AccountResponse findById(UUID id) {
        return accountRepository.findById(id)
                .map(AccountResponse::from)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> findByCustomerId(UUID customerId) {
        return accountRepository.findByCustomerId(customerId).stream()
                .map(AccountResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountResponse> search(String keyword, Pageable pageable) {
        return accountRepository.search(keyword, pageable)
                .map(AccountResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountResponse> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable)
                .map(AccountResponse::from);
    }
}
