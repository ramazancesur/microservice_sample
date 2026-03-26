import type { Account, OpenAccountPayload, PageResult } from '../types/account';

const GATEWAY = import.meta.env.VITE_GATEWAY_URL ?? 'http://localhost:8080';
const BASE = GATEWAY + '/api/v1/accounts';
const CUSTOMERS_BASE = GATEWAY + '/api/v1/customers';

async function apiFetch<T>(url: string, options?: RequestInit): Promise<T> {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json' }, ...options,
  });
  if (!response.ok) {
    const err = await response.json().catch(() => ({ detail: response.statusText }));
    throw new Error(err.detail ?? 'API request failed');
  }
  return response.json() as Promise<T>;
}

export interface CustomerOption {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
}

export const accountApi = {
  getById: (id: string) => apiFetch<Account>(`${BASE}/${id}`),
  getByCustomer: (customerId: string) => apiFetch<Account[]>(`${BASE}/customer/${customerId}`),
  search: (q: string, page = 0, size = 20) => {
    const params = new URLSearchParams({ page: String(page), size: String(size) });
    if (q) params.set('q', q);
    return apiFetch<PageResult<Account>>(`${BASE}?${params}`);
  },
  open: (payload: OpenAccountPayload) =>
    apiFetch<Account>(BASE, { method: 'POST', body: JSON.stringify(payload) }),
  searchCustomers: async (q: string): Promise<CustomerOption[]> => {
    if (!q) return [];
    const result = await apiFetch<{ content: CustomerOption[] }>(`${CUSTOMERS_BASE}?q=${encodeURIComponent(q)}&size=10`);
    return result.content;
  },
};
