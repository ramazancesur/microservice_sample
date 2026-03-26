package com.banking.customer.api.controller;

import com.banking.customer.application.dto.CreateCustomerRequest;
import com.banking.customer.application.dto.CustomerResponse;
import com.banking.customer.application.dto.UpdateCustomerRequest;
import com.banking.customer.application.usecase.CreateCustomerUseCase;
import com.banking.customer.application.usecase.GetCustomerUseCase;
import com.banking.customer.application.usecase.ListCustomersUseCase;
import com.banking.customer.application.usecase.UpdateCustomerUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Customers", description = "Customer management API")
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final ListCustomersUseCase listCustomersUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;

    @Operation(summary = "Create a new customer")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return createCustomerUseCase.execute(request);
    }

    @Operation(summary = "Get customer by ID")
    @GetMapping("/{id}")
    public CustomerResponse getCustomer(@PathVariable UUID id) {
        return getCustomerUseCase.execute(id);
    }

    @Operation(summary = "List/search customers (paginated). Use ?q=keyword to filter by name or email.")
    @GetMapping
    public Page<CustomerResponse> listCustomers(
            @RequestParam(required = false) String q,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        if (q != null && !q.isBlank()) {
            return listCustomersUseCase.search(q.trim(), pageable);
        }
        return listCustomersUseCase.execute(pageable);
    }

    @Operation(summary = "Update customer profile")
    @PutMapping("/{id}")
    public CustomerResponse updateCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCustomerRequest request) {
        return updateCustomerUseCase.execute(id, request);
    }
}
