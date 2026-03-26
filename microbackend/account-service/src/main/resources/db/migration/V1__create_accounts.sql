CREATE TABLE accounts (
    id             UUID         NOT NULL PRIMARY KEY,
    customer_id    UUID         NOT NULL,
    account_number VARCHAR(30)  NOT NULL UNIQUE,
    type           VARCHAR(20)  NOT NULL,
    currency       VARCHAR(3)   NOT NULL,
    balance        NUMERIC(19,2) NOT NULL DEFAULT 0.00,
    status         VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at     TIMESTAMP    NOT NULL,
    updated_at     TIMESTAMP    NOT NULL
);

CREATE INDEX idx_accounts_customer_id    ON accounts (customer_id);
CREATE INDEX idx_accounts_account_number ON accounts (account_number);
