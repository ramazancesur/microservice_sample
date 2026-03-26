import { useCallback, useEffect, useState } from 'react';
import { GridPaginationModel } from '@mui/x-data-grid';
import { Alert, Paper, TextField, InputAdornment } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import { customerApi } from '../api/customerApi';
import type { CreateCustomerPayload, Customer } from '../types/customer';
import { CustomerTable } from '../components/CustomerTable';
import { CustomerForm } from '../components/CustomerForm';
import { PageHeader } from '../components/PageHeader';
import { FormDialog } from '../components/FormDialog';

export function CustomerListPage() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [rowCount, setRowCount] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [paginationModel, setPaginationModel] = useState<GridPaginationModel>({ page: 0, pageSize: 20 });
  const [dialogOpen, setDialogOpen] = useState(false);
  const [formPayload, setFormPayload] = useState<CreateCustomerPayload>({
    firstName: '', lastName: '', email: '', phone: '',
  });
  const [saving, setSaving] = useState(false);

  const loadCustomers = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await customerApi.list(
        paginationModel.page,
        paginationModel.pageSize,
        searchQuery || undefined,
      );
      setCustomers(result.content);
      setRowCount(result.totalElements);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load customers');
    } finally {
      setLoading(false);
    }
  }, [paginationModel, searchQuery]);

  useEffect(() => { loadCustomers(); }, [loadCustomers]);

  const handleSearchChange = (value: string) => {
    setSearchQuery(value);
    setPaginationModel(prev => ({ ...prev, page: 0 }));
  };

  const handleCreate = async () => {
    setSaving(true);
    try {
      await customerApi.create(formPayload);
      setDialogOpen(false);
      loadCustomers();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create customer');
    } finally {
      setSaving(false);
    }
  };

  return (
    <>
      <PageHeader
        title="Customers"
        subtitle="Manage bank customers"
        onAdd={() => setDialogOpen(true)}
        addLabel="New Customer"
      />

      <TextField
        placeholder="Search by name or email..."
        value={searchQuery}
        onChange={(e) => handleSearchChange(e.target.value)}
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

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Paper>
        <CustomerTable
          rows={customers}
          rowCount={rowCount}
          loading={loading}
          paginationModel={paginationModel}
          onPaginationModelChange={setPaginationModel}
          onRowClick={() => {}}
        />
      </Paper>

      <FormDialog
        open={dialogOpen}
        title="New Customer"
        onClose={() => setDialogOpen(false)}
        onSubmit={handleCreate}
        loading={saving}
      >
        <CustomerForm onChange={setFormPayload} />
      </FormDialog>
    </>
  );
}
