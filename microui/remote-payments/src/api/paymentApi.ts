import type { InitiatePaymentPayload, PageResult, Payment } from '../types/payment';

const GATEWAY = import.meta.env.VITE_GATEWAY_URL ?? 'http://localhost:8080';
const BASE = GATEWAY + '/api/v1/payments';
const ACCOUNTS_BASE = GATEWAY + '/api/v1/accounts';

async function apiFetch<T>(url: string, options?: RequestInit): Promise<T> {
  const response = await fetch(url, { headers: { 'Content-Type': 'application/json' }, ...options });
  if (!response.ok) {
    const err = await response.json().catch(() => ({ detail: response.statusText }));
    throw new Error(err.detail ?? 'API request failed');
  }
  return response.json() as Promise<T>;
}

export interface AccountOption {
  id: string;
  accountNumber: string;
  currency: string;
  type: string;
  balance: number;
}

export const paymentApi = {
  list: (page = 0, size = 20) => apiFetch<PageResult<Payment>>(`${BASE}?page=${page}&size=${size}`),
  getById: (id: string) => apiFetch<Payment>(`${BASE}/${id}`),
  initiate: (payload: InitiatePaymentPayload) =>
    apiFetch<Payment>(BASE, { method: 'POST', body: JSON.stringify(payload) }),
  searchAccounts: async (q: string): Promise<AccountOption[]> => {
    if (!q) return [];
    const result = await apiFetch<{ content: AccountOption[] }>(`${ACCOUNTS_BASE}?q=${encodeURIComponent(q)}&size=10`);
    return result.content;
  },
};
