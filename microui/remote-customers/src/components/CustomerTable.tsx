import React from 'react';
import { DataGrid, GridColDef, GridPaginationModel } from '@mui/x-data-grid';
import { Chip, Box } from '@mui/material';
import type { Customer } from '../types/customer';

// MUI x-data-grid v7: valueFormatter receives (value) directly, not ({ value })
const statusColor = (status: string) =>
  status === 'ACTIVE' ? ('success' as const) : ('error' as const);

const columns: GridColDef<Customer>[] = [
  { field: 'firstName', headerName: 'First Name', flex: 1 },
  { field: 'lastName', headerName: 'Last Name', flex: 1 },
  { field: 'email', headerName: 'Email', flex: 1.5 },
  { field: 'phone', headerName: 'Phone', flex: 1 },
  {
    field: 'status',
    headerName: 'Status',
    width: 120,
    renderCell: ({ value }) => (
      <Chip
        label={value}
        color={statusColor(value as string)}
        size="small"
        variant="outlined"
      />
    ),
  },
  {
    field: 'createdAt',
    headerName: 'Created',
    width: 160,
    valueFormatter: (value: string) => new Date(value).toLocaleDateString(),
  },
];

interface CustomerTableProps {
  rows: Customer[];
  rowCount: number;
  loading: boolean;
  paginationModel: GridPaginationModel;
  onPaginationModelChange: (model: GridPaginationModel) => void;
  onRowClick: (customer: Customer) => void;
}

export function CustomerTable({
  rows,
  rowCount,
  loading,
  paginationModel,
  onPaginationModelChange,
  onRowClick,
}: CustomerTableProps) {
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
        onRowClick={({ row }) => onRowClick(row)}
        pageSizeOptions={[10, 20, 50]}
        disableRowSelectionOnClick
        sx={{ cursor: 'pointer' }}
      />
    </Box>
  );
}
