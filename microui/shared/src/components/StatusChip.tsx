import React from 'react';
import { Chip } from '@mui/material';

type StatusColor = 'success' | 'error' | 'warning' | 'default' | 'info';

const statusColorMap: Record<string, StatusColor> = {
  ACTIVE: 'success',
  INACTIVE: 'error',
  PENDING: 'warning',
  POSTED: 'success',
  FAILED: 'error',
  REVERSED: 'default',
  SUSPENDED: 'warning',
  CLOSED: 'default',
};

interface StatusChipProps {
  status: string;
}

export function StatusChip({ status }: StatusChipProps) {
  const color = statusColorMap[status] ?? 'default';
  return (
    <Chip
      label={status}
      color={color}
      size="small"
      variant="outlined"
    />
  );
}
