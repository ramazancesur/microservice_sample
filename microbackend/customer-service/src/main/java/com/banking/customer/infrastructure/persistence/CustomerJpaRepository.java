package com.banking.customer.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, UUID> {

    Optional<CustomerJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT c FROM CustomerJpaEntity c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :kw, '%'))")
    Page<CustomerJpaEntity> search(@Param("kw") String keyword, Pageable pageable);
}
