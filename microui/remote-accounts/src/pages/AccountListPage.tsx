import { useCallback, useEffect, useState } from 'react';
import { Alert, Paper, TextField, InputAdornment, Box, Typography, Chip } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import { accountApi } from '../api/accountApi';
import type { Account, OpenAccountPayload, PageResult } from '../types/account';
import { AccountTable } from '../components/AccountTable';
import { OpenAccountForm } from '../components/OpenAccountForm';
import { FormDialog } from '../components/FormDialog';
import { PageHeader } from '../components/PageHeader';

export function AccountListPage() {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [dialogOpen, setDialogOpen] = useState(false);
  const [formPayload, setFormPayload] = useState<OpenAccountPayload>({
    customerId: '', type: 'CHECKING', currency: 'TRY',
  });
  const [saving, setSaving] = useState(false);

  const loadAccounts = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await accountApi.search(searchQuery, 0, 50);
      setAccounts(result.content);
      setTotalElements(result.totalElements);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load accounts');
    } finally {
      setLoading(false);
    }
  }, [searchQuery]);

  useEffect(() => { loadAccounts(); }, [loadAccounts]);

  const handleOpen = async () => {
    setSaving(true);
    try {
      await accountApi.open(formPayload);
      setDialogOpen(false);
      loadAccounts();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to open account');
    } finally {
      setSaving(false);
    }
  };

  return (
    <>
      <PageHeader
        title="Accounts"
        subtitle="Search and manage bank accounts"
        onAdd={() => setDialogOpen(true)}
        addLabel="Open Account"
      />

      <TextField
        placeholder="Search by account number..."
        value={searchQuery}
        onChange={(e) => setSearchQuery(e.target.value)}
        size="small"
        fullWidth
        sx={{ mb: 2, maxWidth: 400 }}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <SearchIcon color="action" />
            </InputAdornment>
          ),
        }}
      />

      {totalElements > 0 && (
        <Box mb={1}>
          <Chip label={`${totalElements} account(s) found`} size="small" color="primary" variant="outlined" />
        </Box>
      )}

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Paper>
        <AccountTable rows={accounts} loading={loading} />
      </Paper>

      <FormDialog
        open={dialogOpen}
        title="Open New Account"
        onClose={() => setDialogOpen(false)}
        onSubmit={handleOpen}
        submitLabel="Open Account"
        loading={saving}
      >
        <OpenAccountForm onChange={setFormPayload} />
      </FormDialog>
    </>
  );
}
