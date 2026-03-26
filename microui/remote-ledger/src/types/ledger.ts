export type EntryType = 'DEBIT' | 'CREDIT';

export interface LedgerEntry {
  id: string;
  accountId: string;
  type: EntryType;
  amount: number;
  currency: string;
}

export interface JournalEntry {
  id: string;
  referenceId: string;
  description: string;
  entries: LedgerEntry[];
  postedAt: string;
}

export interface AccountBalance {
  accountId: string;
  balance: number;
  currency: string;
}

export interface AccountOption {
  id: string;
  accountNumber: string;
  currency: string;
  type: string;
  balance: number;
}
