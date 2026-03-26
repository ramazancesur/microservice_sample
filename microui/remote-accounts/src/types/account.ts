export type AccountType = 'CHECKING' | 'SAVINGS' | 'LOAN';
export type AccountStatus = 'ACTIVE' | 'SUSPENDED' | 'CLOSED';

export interface Account {
  id: string;
  customerId: string;
  accountNumber: string;
  type: AccountType;
  currency: string;
  balance: number;
  status: AccountStatus;
  createdAt: string;
  updatedAt: string;
}

export interface OpenAccountPayload {
  customerId: string;
  type: AccountType;
  currency: string;
}

export interface PageResult<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
