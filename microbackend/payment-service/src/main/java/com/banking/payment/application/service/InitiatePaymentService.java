package com.banking.payment.application.service;

import com.banking.payment.application.dto.InitiatePaymentRequest;
import com.banking.payment.application.dto.PaymentResponse;
import com.banking.payment.application.usecase.InitiatePaymentUseCase;
import com.banking.payment.domain.model.Payment;
import com.banking.payment.domain.repository.PaymentRepository;
import com.banking.payment.infrastructure.ledger.LedgerClient;
import com.banking.payment.infrastructure.ledger.LedgerPostingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitiatePaymentService implements InitiatePaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final LedgerClient ledgerClient;

    @Override
    @Transactional
    public PaymentResponse execute(InitiatePaymentRequest request) {
        // Idempotency check: return existing payment if key already processed
        return paymentRepository.findByIdempotencyKey(request.idempotencyKey())
                .map(PaymentResponse::from)
                .orElseGet(() -> initiateNewPayment(request));
    }

    private PaymentResponse initiateNewPayment(InitiatePaymentRequest request) {
        Payment payment = Payment.initiate(
                request.idempotencyKey(),
                request.sourceAccountId(),
                request.targetAccountId(),
                request.amount(),
                request.currency()
        );

        Payment saved = paymentRepository.save(payment);
        log.info("Payment initiated id={} key={}", saved.getId(), saved.getIdempotencyKey());

        try {
            ledgerClient.postPaymentEntries(
                    saved.getId(),
                    saved.getSourceAccountId(),
                    saved.getTargetAccountId(),
                    saved.getAmount(),
                    saved.getCurrency()
            );
            saved.markPosted();
        } catch (LedgerPostingException ex) {
            log.error("Ledger posting failed for payment id={}: {}", saved.getId(), ex.getMessage());
            saved.markFailed(ex.getMessage());
        }

        return PaymentResponse.from(paymentRepository.save(saved));
    }
}
