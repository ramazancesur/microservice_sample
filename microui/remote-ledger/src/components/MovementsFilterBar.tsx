import { useEffect, useState } from 'react';
import { Autocomplete, TextField, Button, Stack, CircularProgress } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import { ledgerApi } from '../api/ledgerApi';
import type { AccountOption } from '../types/ledger';

interface MovementsFilterBarProps {
  onSearch: (accountId: string, from: string, to: string) => void;
}

export function MovementsFilterBar({ onSearch }: MovementsFilterBarProps) {
  const [inputValue, setInputValue] = useState('');
  const [options, setOptions] = useState<AccountOption[]>([]);
  const [selectedAccount, setSelectedAccount] = useState<AccountOption | null>(null);
  const [optionsLoading, setOptionsLoading] = useState(false);
  const [from, setFrom] = useState(
    new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString().slice(0, 16)
  );
  const [to, setTo] = useState(new Date().toISOString().slice(0, 16));

  useEffect(() => {
    if (inputValue.length < 2) {
      setOptions([]);
      return;
    }
    const timer = setTimeout(async () => {
      setOptionsLoading(true);
      try {
        const results = await ledgerApi.searchAccounts(inputValue);
        setOptions(results);
      } catch {
        setOptions([]);
      } finally {
        setOptionsLoading(false);
      }
    }, 300);
    return () => clearTimeout(timer);
  }, [inputValue]);

  const handleSearch = () => {
    if (selectedAccount) {
      onSearch(selectedAccount.id, from + ':00', to + ':00');
    }
  };

  return (
    <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1} mb={2} alignItems="flex-end">
      <Autocomplete
        sx={{ minWidth: 320 }}
        options={options}
        getOptionLabel={(opt) => `${opt.accountNumber} (${opt.currency} - ${opt.type})`}
        isOptionEqualToValue={(opt, val) => opt.id === val.id}
        value={selectedAccount}
        onChange={(_e, val) => setSelectedAccount(val)}
        inputValue={inputValue}
        onInputChange={(_e, val) => setInputValue(val)}
        loading={optionsLoading}
        noOptionsText={inputValue.length < 2 ? 'Type to search...' : 'No accounts found'}
        renderInput={(params) => (
          <TextField
            {...params}
            label="Search Account"
            size="small"
            placeholder="Account number..."
            InputProps={{
              ...params.InputProps,
              endAdornment: (
                <>
                  {optionsLoading && <CircularProgress color="inherit" size={18} />}
                  {params.InputProps.endAdornment}
                </>
              ),
            }}
          />
        )}
      />
      <TextField label="From" type="datetime-local" value={from}
        onChange={(e) => setFrom(e.target.value)} size="small"
        InputLabelProps={{ shrink: true }} />
      <TextField label="To" type="datetime-local" value={to}
        onChange={(e) => setTo(e.target.value)} size="small"
        InputLabelProps={{ shrink: true }} />
      <Button variant="contained" startIcon={<SearchIcon />} onClick={handleSearch}
        disabled={!selectedAccount}>
        Search
      </Button>
    </Stack>
  );
}
