import React from 'react';
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import { Box } from '@mui/material';
import type { JournalEntry, LedgerEntry } from '../types/ledger';

// MUI x-data-grid v7: valueFormatter and valueGetter receive (value) directly, not ({ value })
const columns: GridColDef<JournalEntry>[] = [
  { field: 'referenceId', headerName: 'Reference', flex: 1 },
  { field: 'description', headerName: 'Description', flex: 1.5 },
  {
    field: 'entries',
    headerName: 'Entries',
    width: 80,
    type: 'number',
    valueGetter: (value: LedgerEntry[]) => value?.length ?? 0,
  },
  {
    field: 'postedAt',
    headerName: 'Posted At',
    width: 180,
    valueFormatter: (value: string) => new Date(value).toLocaleString(),
  },
];

interface MovementsTableProps {
  rows: JournalEntry[];
  loading: boolean;
}

export function MovementsTable({ rows, loading }: MovementsTableProps) {
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
