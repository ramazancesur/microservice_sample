package com.banking.customer.domain.repository;

import com.banking.customer.domain.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/** Domain port — infrastructure adapts to this interface. */
public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(UUID id);

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Customer> findAll(Pageable pageable);

    Page<Customer> search(String keyword, Pageable pageable);
}
