export type PaymentState = 'PENDING' | 'POSTED' | 'FAILED' | 'REVERSED';

export interface Payment {
  id: string;
  idempotencyKey: string;
  sourceAccountId: string;
  targetAccountId: string;
  amount: number;
  currency: string;
  state: PaymentState;
  failureReason?: string;
  createdAt: string;
  updatedAt: string;
}

export interface InitiatePaymentPayload {
  idempotencyKey: string;
  sourceAccountId: string;
  targetAccountId: string;
  amount: number;
  currency: string;
}

export interface PageResult<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
