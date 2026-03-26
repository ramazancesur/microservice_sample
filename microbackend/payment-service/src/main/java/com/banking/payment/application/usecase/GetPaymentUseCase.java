package com.banking.payment.application.usecase;

import com.banking.payment.application.dto.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GetPaymentUseCase {
    PaymentResponse findById(UUID id);
    Page<PaymentResponse> findAll(Pageable pageable);
}
