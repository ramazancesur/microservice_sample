import React from 'react';
import { Card, CardContent, Typography, Box } from '@mui/material';
import AccountBalanceIcon from '@mui/icons-material/AccountBalance';
import type { AccountBalance } from '../types/ledger';

interface BalanceCardProps {
  balance: AccountBalance;
}

export function BalanceCard({ balance }: BalanceCardProps) {
  const formatted = new Intl.NumberFormat('tr-TR', {
    style: 'currency',
    currency: balance.currency,
    minimumFractionDigits: 2,
  }).format(balance.balance);

  return (
    <Card sx={{ mb: 3, bgcolor: 'primary.main', color: 'white' }}>
      <CardContent>
        <Box display="flex" alignItems="center" gap={1} mb={1}>
          <AccountBalanceIcon />
          <Typography variant="body2" sx={{ opacity: 0.85 }}>Account Balance</Typography>
        </Box>
        <Typography variant="h4" fontWeight={700}>{formatted}</Typography>
        <Typography variant="caption" sx={{ opacity: 0.75 }}>
          Account: {balance.accountId}
        </Typography>
      </CardContent>
    </Card>
  );
}
