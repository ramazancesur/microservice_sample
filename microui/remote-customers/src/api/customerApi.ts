import type { CreateCustomerPayload, Customer, PageResult } from '../types/customer';

const BASE = (import.meta.env.VITE_GATEWAY_URL ?? 'http://localhost:8080') + '/api/v1/customers';

async function apiFetch<T>(url: string, options?: RequestInit): Promise<T> {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  });
  if (!response.ok) {
    const err = await response.json().catch(() => ({ detail: response.statusText }));
    throw new Error(err.detail ?? 'API request failed');
  }
  return response.json() as Promise<T>;
}

export const customerApi = {
  list: (page = 0, size = 20, q?: string) => {
    const params = new URLSearchParams({ page: String(page), size: String(size) });
    if (q) params.set('q', q);
    return apiFetch<PageResult<Customer>>(`${BASE}?${params}`);
  },

  getById: (id: string) =>
    apiFetch<Customer>(`${BASE}/${id}`),

  create: (payload: CreateCustomerPayload) =>
    apiFetch<Customer>(BASE, { method: 'POST', body: JSON.stringify(payload) }),

  update: (id: string, payload: Partial<CreateCustomerPayload>) =>
    apiFetch<Customer>(`${BASE}/${id}`, { method: 'PUT', body: JSON.stringify(payload) }),
};
