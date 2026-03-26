import React from 'react';
import { Box, Typography, Button } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';

interface PageHeaderProps {
  title: string; subtitle?: string; onAdd?: () => void; addLabel?: string;
}

export function PageHeader({ title, subtitle, onAdd, addLabel = 'Add New' }: PageHeaderProps) {
  return (
    <Box display="flex" justifyContent="space-between" alignItems="flex-start" mb={3}>
      <Box>
        <Typography variant="h5">{title}</Typography>
        {subtitle && <Typography variant="body2" color="text.secondary" mt={0.5}>{subtitle}</Typography>}
      </Box>
      {onAdd && <Button variant="contained" startIcon={<AddIcon />} onClick={onAdd}>{addLabel}</Button>}
    </Box>
  );
}
