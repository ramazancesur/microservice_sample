CREATE TABLE journal_entries (
    id           UUID         NOT NULL PRIMARY KEY,
    reference_id VARCHAR(100) NOT NULL UNIQUE,
    description  VARCHAR(255) NOT NULL,
    posted_at    TIMESTAMP    NOT NULL
);

CREATE TABLE ledger_entries (
    id               UUID          NOT NULL PRIMARY KEY,
    journal_entry_id UUID          NOT NULL REFERENCES journal_entries(id),
    account_id       UUID          NOT NULL,
    type             VARCHAR(10)   NOT NULL,
    amount           NUMERIC(19,2) NOT NULL,
    currency         VARCHAR(3)    NOT NULL
);

CREATE INDEX idx_journal_reference    ON journal_entries (reference_id);
CREATE INDEX idx_journal_posted_at    ON journal_entries (posted_at);
CREATE INDEX idx_ledger_account_id    ON ledger_entries (account_id);
CREATE INDEX idx_ledger_journal_entry ON ledger_entries (journal_entry_id);
