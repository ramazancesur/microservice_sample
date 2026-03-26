CREATE TABLE payments (
    id                UUID          NOT NULL PRIMARY KEY,
    idempotency_key   VARCHAR(100)  NOT NULL UNIQUE,
    source_account_id UUID          NOT NULL,
    target_account_id UUID          NOT NULL,
    amount            NUMERIC(19,2) NOT NULL,
    currency          VARCHAR(3)    NOT NULL,
    state             VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    failure_reason    VARCHAR(500),
    created_at        TIMESTAMP     NOT NULL,
    updated_at        TIMESTAMP     NOT NULL
);

CREATE INDEX idx_payments_idempotency_key   ON payments (idempotency_key);
CREATE INDEX idx_payments_source_account_id ON payments (source_account_id);
CREATE INDEX idx_payments_state             ON payments (state);
