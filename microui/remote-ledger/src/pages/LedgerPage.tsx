import { useState } from 'react';
import { Alert, Box, Paper, Typography } from '@mui/material';
import { ledgerApi } from '../api/ledgerApi';
import type { AccountBalance, JournalEntry } from '../types/ledger';
import { BalanceCard } from '../components/BalanceCard';
import { MovementsFilterBar } from '../components/MovementsFilterBar';
import { MovementsTable } from '../components/MovementsTable';

export function LedgerPage() {
  const [balance, setBalance] = useState<AccountBalance | null>(null);
  const [movements, setMovements] = useState<JournalEntry[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSearch = async (accountId: string, from: string, to: string) => {
    setLoading(true);
    setError(null);
    try {
      const [balanceResult, movementsResult] = await Promise.all([
        ledgerApi.getBalance(accountId, 'TRY'),
        ledgerApi.getMovements(accountId, from, to),
      ]);
      setBalance(balanceResult);
      setMovements(movementsResult);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load ledger data');
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Box mb={3}>
        <Typography variant="h5">Ledger</Typography>
        <Typography variant="body2" color="text.secondary" mt={0.5}>
          Account balance and movement history
        </Typography>
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      {balance && <BalanceCard balance={balance} />}

      <MovementsFilterBar onSearch={handleSearch} />

      <Paper>
        <MovementsTable rows={movements} loading={loading} />
      </Paper>
    </>
  );
}
