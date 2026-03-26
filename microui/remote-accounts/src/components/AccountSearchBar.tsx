import React, { useState } from 'react';
import { Box, TextField, Button } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';

interface AccountSearchBarProps {
  onSearch: (customerId: string) => void;
}

export function AccountSearchBar({ onSearch }: AccountSearchBarProps) {
  const [customerId, setCustomerId] = useState('');

  const handleSearch = () => {
    if (customerId.trim()) onSearch(customerId.trim());
  };

  return (
    <Box display="flex" gap={1} mb={2}>
      <TextField
        label="Search by Customer ID"
        value={customerId}
        onChange={(e) => setCustomerId(e.target.value)}
        size="small"
        sx={{ minWidth: 300 }}
        onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
      />
      <Button variant="contained" startIcon={<SearchIcon />} onClick={handleSearch}>
        Search
      </Button>
    </Box>
  );
}
