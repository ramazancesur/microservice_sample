import { useEffect, useState } from 'react';
import { Autocomplete, TextField, Stack, MenuItem, CircularProgress } from '@mui/material';
import type { InitiatePaymentPayload } from '../types/payment';
import { paymentApi, type AccountOption } from '../api/paymentApi';

const CURRENCIES = ['TRY', 'USD', 'EUR'];

interface InitiatePaymentFormProps {
  onChange: (payload: InitiatePaymentPayload) => void;
}

function AccountSearchField({
  label,
  value,
  onSelect,
}: {
  label: string;
  value: AccountOption | null;
  onSelect: (account: AccountOption | null) => void;
}) {
  const [inputValue, setInputValue] = useState('');
  const [options, setOptions] = useState<AccountOption[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (inputValue.length < 2) {
      setOptions([]);
      return;
    }
    const timer = setTimeout(async () => {
      setLoading(true);
      try {
        const results = await paymentApi.searchAccounts(inputValue);
        setOptions(results);
      } catch {
        setOptions([]);
      } finally {
        setLoading(false);
      }
    }, 300);
    return () => clearTimeout(timer);
  }, [inputValue]);

  return (
    <Autocomplete
      options={options}
      getOptionLabel={(opt) =>
        `${opt.accountNumber} (${opt.currency} - bal: ${opt.balance.toLocaleString('tr-TR', { minimumFractionDigits: 2 })})`
      }
      isOptionEqualToValue={(opt, val) => opt.id === val.id}
      value={value}
      onChange={(_e, val) => onSelect(val)}
      inputValue={inputValue}
      onInputChange={(_e, val) => setInputValue(val)}
      loading={loading}
      noOptionsText={inputValue.length < 2 ? 'Type to search...' : 'No accounts found'}
      renderInput={(params) => (
        <TextField
          {...params}
          label={label}
          required
          fullWidth
          placeholder="Account number..."
          InputProps={{
            ...params.InputProps,
            endAdornment: (
              <>
                {loading && <CircularProgress color="inherit" size={18} />}
                {params.InputProps.endAdornment}
              </>
            ),
          }}
        />
      )}
    />
  );
}

export function InitiatePaymentForm({ onChange }: InitiatePaymentFormProps) {
  const [sourceAccount, setSourceAccount] = useState<AccountOption | null>(null);
  const [targetAccount, setTargetAccount] = useState<AccountOption | null>(null);
  const [amount, setAmount] = useState(0);
  const [currency, setCurrency] = useState('TRY');

  useEffect(() => {
    onChange({
      idempotencyKey: crypto.randomUUID(),
      sourceAccountId: sourceAccount?.id ?? '',
      targetAccountId: targetAccount?.id ?? '',
      amount,
      currency,
    });
  }, [sourceAccount, targetAccount, amount, currency, onChange]);

  return (
    <Stack spacing={2} mt={1}>
      <AccountSearchField
        label="Source Account"
        value={sourceAccount}
        onSelect={setSourceAccount}
      />
      <AccountSearchField
        label="Target Account"
        value={targetAccount}
        onSelect={setTargetAccount}
      />
      <TextField
        label="Amount"
        type="number"
        value={amount}
        onChange={(e) => setAmount(parseFloat(e.target.value) || 0)}
        required
        fullWidth
        inputProps={{ min: 0.01, step: 0.01 }}
      />
      <TextField
        select
        label="Currency"
        value={currency}
        onChange={(e) => setCurrency(e.target.value)}
        required
        fullWidth
      >
        {CURRENCIES.map((c) => (
          <MenuItem key={c} value={c}>{c}</MenuItem>
        ))}
      </TextField>
    </Stack>
  );
}
