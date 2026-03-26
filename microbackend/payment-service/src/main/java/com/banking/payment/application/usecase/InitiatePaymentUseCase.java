package com.banking.payment.application.usecase;

import com.banking.payment.application.dto.InitiatePaymentRequest;
import com.banking.payment.application.dto.PaymentResponse;

public interface InitiatePaymentUseCase {
    PaymentResponse execute(InitiatePaymentRequest request);
}
