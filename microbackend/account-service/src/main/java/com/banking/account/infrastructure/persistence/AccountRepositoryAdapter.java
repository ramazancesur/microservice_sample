package com.banking.account.infrastructure.persistence;

import com.banking.account.domain.model.Account;
import com.banking.account.domain.model.Money;
import com.banking.account.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepository {

    private final AccountJpaRepository jpaRepository;

    @Override
    public Account save(Account account) {
        return toDomain(jpaRepository.save(toEntity(account)));
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return jpaRepository.findByAccountNumber(accountNumber).map(this::toDomain);
    }

    @Override
    public List<Account> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Page<Account> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Page<Account> search(String keyword, Pageable pageable) {
        return jpaRepository.search(keyword, pageable).map(this::toDomain);
    }

    private AccountJpaEntity toEntity(Account account) {
        AccountJpaEntity entity = new AccountJpaEntity();
        entity.setId(account.getId());
        entity.setCustomerId(account.getCustomerId());
        entity.setAccountNumber(account.getAccountNumber());
        entity.setType(account.getType());
        entity.setCurrency(account.getCurrency());
        entity.setBalance(account.getBalance().amount());
        entity.setStatus(account.getStatus());
        entity.setCreatedAt(account.getCreatedAt());
        entity.setUpdatedAt(account.getUpdatedAt());
        return entity;
    }

    private Account toDomain(AccountJpaEntity entity) {
        return Account.reconstitute(
                entity.getId(),
                entity.getCustomerId(),
                entity.getAccountNumber(),
                entity.getType(),
                entity.getCurrency(),
                Money.of(entity.getBalance(), entity.getCurrency()),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
