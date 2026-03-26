package com.banking.payment.api.controller;

import com.banking.payment.application.dto.InitiatePaymentRequest;
import com.banking.payment.application.dto.PaymentResponse;
import com.banking.payment.application.usecase.GetPaymentUseCase;
import com.banking.payment.application.usecase.InitiatePaymentUseCase;
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

@Tag(name = "Payments", description = "Payment initiation and status API")
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final InitiatePaymentUseCase initiatePaymentUseCase;
    private final GetPaymentUseCase getPaymentUseCase;

    @Operation(summary = "Initiate a payment (idempotent)")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse initiatePayment(@Valid @RequestBody InitiatePaymentRequest request) {
        return initiatePaymentUseCase.execute(request);
    }

    @Operation(summary = "Get payment status by ID")
    @GetMapping("/{id}")
    public PaymentResponse getPayment(@PathVariable UUID id) {
        return getPaymentUseCase.findById(id);
    }

    @Operation(summary = "List all payments (paginated)")
    @GetMapping
    public Page<PaymentResponse> listPayments(
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return getPaymentUseCase.findAll(pageable);
    }
}
