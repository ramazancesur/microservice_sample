import React from 'react';
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import { Chip, Box } from '@mui/material';
import type { Account } from '../types/account';

// MUI x-data-grid v7: valueFormatter receives (value) directly, not ({ value })
const columns: GridColDef<Account>[] = [
  { field: 'accountNumber', headerName: 'Account Number', flex: 1.5 },
  { field: 'type', headerName: 'Type', width: 110 },
  { field: 'currency', headerName: 'Currency', width: 90 },
  {
    field: 'balance',
    headerName: 'Balance',
    width: 130,
    type: 'number',
    valueFormatter: (value: number) =>
      new Intl.NumberFormat('tr-TR', { minimumFractionDigits: 2 }).format(value),
  },
  {
    field: 'status',
    headerName: 'Status',
    width: 120,
    renderCell: ({ value }) => (
      <Chip
        label={value}
        color={value === 'ACTIVE' ? 'success' : 'default'}
        size="small"
        variant="outlined"
      />
    ),
  },
  {
    field: 'createdAt',
    headerName: 'Opened',
    width: 130,
    valueFormatter: (value: string) => new Date(value).toLocaleDateString(),
  },
];

interface AccountTableProps {
  rows: Account[];
  loading: boolean;
}

export function AccountTable({ rows, loading }: AccountTableProps) {
  return (
    <Box sx={{ height: 460, width: '100%' }}>
      <DataGrid
        rows={rows}
        columns={columns}
        loading={loading}
        pageSizeOptions={[10, 20, 50]}
        disableRowSelectionOnClick
      />
    </Box>
  );
}
