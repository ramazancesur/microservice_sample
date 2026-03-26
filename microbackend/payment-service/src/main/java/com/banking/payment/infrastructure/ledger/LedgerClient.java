package com.banking.payment.infrastructure.ledger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * HTTP adapter to ledger-service.
 * Posts a balanced double-entry for each payment:
 *   DEBIT source account (money leaving)
 *   CREDIT target account (money arriving)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LedgerClient {

    private final RestTemplate restTemplate;

    @Value("${banking.ledger-service.url}")
    private String ledgerServiceUrl;

    public void postPaymentEntries(UUID paymentId, UUID sourceAccountId,
                                   UUID targetAccountId, BigDecimal amount, String currency) {
        Map<String, Object> request = Map.of(
                "referenceId", "PMT-" + paymentId,
                "description", "Payment " + paymentId,
                "entries", List.of(
                        Map.of("accountId", sourceAccountId, "type", "DEBIT",
                               "amount", amount, "currency", currency),
                        Map.of("accountId", targetAccountId, "type", "CREDIT",
                               "amount", amount, "currency", currency)
                )
        );

        ResponseEntity<Void> response = restTemplate.postForEntity(
                ledgerServiceUrl + "/api/v1/ledger/journal", request, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new LedgerPostingException("Ledger rejected payment: " + paymentId);
        }
        log.info("Ledger entries posted for payment id={}", paymentId);
    }
}
