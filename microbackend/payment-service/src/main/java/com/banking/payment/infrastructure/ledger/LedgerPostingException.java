package com.banking.payment.infrastructure.ledger;

public class LedgerPostingException extends RuntimeException {

    public LedgerPostingException(String message) {
        super(message);
    }

    public LedgerPostingException(String message, Throwable cause) {
        super(message, cause);
    }
}
