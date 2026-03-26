package com.banking.account.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountJpaRepository extends JpaRepository<AccountJpaEntity, UUID> {

    Optional<AccountJpaEntity> findByAccountNumber(String accountNumber);

    List<AccountJpaEntity> findByCustomerId(UUID customerId);

    @Query("SELECT a FROM AccountJpaEntity a WHERE " +
           "LOWER(a.accountNumber) LIKE LOWER(CONCAT('%', :kw, '%'))")
    Page<AccountJpaEntity> search(@Param("kw") String keyword, Pageable pageable);
}
