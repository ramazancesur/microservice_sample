import type { AccountBalance, AccountOption, JournalEntry } from '../types/ledger';

const GATEWAY = import.meta.env.VITE_GATEWAY_URL ?? 'http://localhost:8080';
const BASE = GATEWAY + '/api/v1/ledger';
const ACCOUNTS_BASE = GATEWAY + '/api/v1/accounts';

async function apiFetch<T>(url: string, options?: RequestInit): Promise<T> {
  const response = await fetch(url, { headers: { 'Content-Type': 'application/json' }, ...options });
  if (!response.ok) {
    const err = await response.json().catch(() => ({ detail: response.statusText }));
    throw new Error(err.detail ?? 'API request failed');
  }
  return response.json() as Promise<T>;
}

export const ledgerApi = {
  getBalance: (accountId: string, currency = 'TRY') =>
    apiFetch<AccountBalance>(`${BASE}/balance/${accountId}?currency=${currency}`),
  getMovements: (accountId: string, from: string, to: string) =>
    apiFetch<JournalEntry[]>(`${BASE}/movements/${accountId}?from=${from}&to=${to}`),
  searchAccounts: async (q: string): Promise<AccountOption[]> => {
    if (!q) return [];
    const result = await apiFetch<{ content: AccountOption[] }>(`${ACCOUNTS_BASE}?q=${encodeURIComponent(q)}&size=10`);
    return result.content;
  },
};
