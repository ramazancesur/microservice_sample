package com.banking.ledger.domain.model;

/**
 * Classic double-entry bookkeeping sides.
 * For customer deposit accounts (liability accounts):
 *   CREDIT increases the balance, DEBIT decreases it.
 */
public enum EntryType {
    DEBIT,
    CREDIT
}
