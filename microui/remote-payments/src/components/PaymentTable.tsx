import React from 'react';
import { DataGrid, GridColDef, GridPaginationModel } from '@mui/x-data-grid';
import { Chip, Box } from '@mui/material';
import type { Payment, PaymentState } from '../types/payment';

// MUI x-data-grid v7: valueFormatter receives (value) directly, not ({ value })
const stateColorMap: Record<PaymentState, 'warning' | 'success' | 'error' | 'default'> = {
  PENDING: 'warning',
  POSTED: 'success',
  FAILED: 'error',
  REVERSED: 'default',
};

const columns: GridColDef<Payment>[] = [
  { field: 'idempotencyKey', headerName: 'Reference', flex: 1 },
  { field: 'sourceAccountId', headerName: 'Source', flex: 1 },
  { field: 'targetAccountId', headerName: 'Target', flex: 1 },
  {
    field: 'amount',
    headerName: 'Amount',
    width: 130,
    type: 'number',
    valueFormatter: (value: number) =>
      new Intl.NumberFormat('tr-TR', { minimumFractionDigits: 2 }).format(value),
  },
  { field: 'currency', headerName: 'CCY', width: 70 },
  {
    field: 'state',
    headerName: 'Status',
    width: 110,
    renderCell: ({ value }) => (
      <Chip
        label={value}
        color={stateColorMap[value as PaymentState] ?? 'default'}
        size="small"
        variant="outlined"
      />
    ),
  },
  {
    field: 'createdAt',
    headerName: 'Created',
    width: 160,
    valueFormatter: (value: string) => new Date(value).toLocaleString(),
  },
];

interface PaymentTableProps {
  rows: Payment[];
  rowCount: number;
  loading: boolean;
  paginationModel: GridPaginationModel;
  onPaginationModelChange: (model: GridPaginationModel) => void;
}

export function PaymentTable({
  rows,
  rowCount,
  loading,
  paginationModel,
  onPaginationModelChange,
}: PaymentTableProps) {
  return (
    <Box sx={{ height: 480, width: '100%' }}>
      <DataGrid
        rows={rows}
        columns={columns}
        rowCount={rowCount}
        loading={loading}
        paginationMode="server"
        paginationModel={paginationModel}
        onPaginationModelChange={onPaginationModelChange}
        pageSizeOptions={[10, 20, 50]}
        disableRowSelectionOnClick
      />
    </Box>
  );
}
