package com.banking.ledger.domain.model;

import java.math.BigDecimal;

public class UnbalancedJournalException extends RuntimeException {

    public UnbalancedJournalException(BigDecimal debits, BigDecimal credits) {
        super(String.format(
                "Journal entry is not balanced: debits=%s, credits=%s", debits, credits));
    }
}
