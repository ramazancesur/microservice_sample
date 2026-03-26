package com.banking.account.application.service;

import com.banking.account.application.dto.AccountResponse;
import com.banking.account.application.dto.OpenAccountRequest;
import com.banking.account.application.usecase.OpenAccountUseCase;
import com.banking.account.domain.model.Account;
import com.banking.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAccountService implements OpenAccountUseCase {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public AccountResponse execute(OpenAccountRequest request) {
        Account account = Account.open(request.customerId(), request.type(), request.currency());
        Account saved = accountRepository.save(account);
        log.info("Opened account id={} type={} currency={}", saved.getId(), saved.getType(), saved.getCurrency());
        return AccountResponse.from(saved);
    }
}
