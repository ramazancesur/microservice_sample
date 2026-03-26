import { useEffect, useState } from 'react';
import { Autocomplete, TextField, Stack, MenuItem, CircularProgress } from '@mui/material';
import type { AccountType, OpenAccountPayload } from '../types/account';
import { accountApi, type CustomerOption } from '../api/accountApi';

const ACCOUNT_TYPES: AccountType[] = ['CHECKING', 'SAVINGS', 'LOAN'];
const CURRENCIES = ['TRY', 'USD', 'EUR'];

interface OpenAccountFormProps {
  onChange: (payload: OpenAccountPayload) => void;
}

export function OpenAccountForm({ onChange }: OpenAccountFormProps) {
  const [inputValue, setInputValue] = useState('');
  const [options, setOptions] = useState<CustomerOption[]>([]);
  const [optionsLoading, setOptionsLoading] = useState(false);
  const [selectedCustomer, setSelectedCustomer] = useState<CustomerOption | null>(null);
  const [type, setType] = useState<AccountType>('CHECKING');
  const [currency, setCurrency] = useState('TRY');

  useEffect(() => {
    if (inputValue.length < 2) {
      setOptions([]);
      return;
    }
    const timer = setTimeout(async () => {
      setOptionsLoading(true);
      try {
        const results = await accountApi.searchCustomers(inputValue);
        setOptions(results);
      } catch {
        setOptions([]);
      } finally {
        setOptionsLoading(false);
      }
    }, 300);
    return () => clearTimeout(timer);
  }, [inputValue]);

  useEffect(() => {
    onChange({
      customerId: selectedCustomer?.id ?? '',
      type,
      currency,
    });
  }, [selectedCustomer, type, currency, onChange]);

  return (
    <Stack spacing={2} mt={1}>
      <Autocomplete
        options={options}
        getOptionLabel={(opt) => `${opt.firstName} ${opt.lastName} (${opt.email})`}
        isOptionEqualToValue={(opt, val) => opt.id === val.id}
        value={selectedCustomer}
        onChange={(_e, val) => setSelectedCustomer(val)}
        inputValue={inputValue}
        onInputChange={(_e, val) => setInputValue(val)}
        loading={optionsLoading}
        noOptionsText={inputValue.length < 2 ? 'Type to search...' : 'No customers found'}
        renderInput={(params) => (
          <TextField
            {...params}
            label="Customer"
            required
            fullWidth
            placeholder="Search by name or email..."
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
      <TextField select label="Account Type" value={type}
        onChange={(e) => setType(e.target.value as AccountType)} required fullWidth>
        {ACCOUNT_TYPES.map(t => <MenuItem key={t} value={t}>{t}</MenuItem>)}
      </TextField>
      <TextField select label="Currency" value={currency}
        onChange={(e) => setCurrency(e.target.value)} required fullWidth>
        {CURRENCIES.map(c => <MenuItem key={c} value={c}>{c}</MenuItem>)}
      </TextField>
    </Stack>
  );
}
