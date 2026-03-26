import { useCallback, useEffect, useState } from 'react';
import { GridPaginationModel } from '@mui/x-data-grid';
import { Alert, Box, Button, Paper, Typography } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { paymentApi } from '../api/paymentApi';
import type { InitiatePaymentPayload, Payment } from '../types/payment';
import { PaymentTable } from '../components/PaymentTable';
import { InitiatePaymentForm } from '../components/InitiatePaymentForm';
import { FormDialog } from '../components/FormDialog';

export function PaymentListPage() {
  const [payments, setPayments] = useState<Payment[]>([]);
  const [rowCount, setRowCount] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [paginationModel, setPaginationModel] = useState<GridPaginationModel>({ page: 0, pageSize: 20 });
  const [dialogOpen, setDialogOpen] = useState(false);
  const [formPayload, setFormPayload] = useState<InitiatePaymentPayload>({
    idempotencyKey: '', sourceAccountId: '', targetAccountId: '', amount: 0, currency: 'TRY',
  });
  const [saving, setSaving] = useState(false);

  const loadPayments = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await paymentApi.list(paginationModel.page, paginationModel.pageSize);
      setPayments(result.content);
      setRowCount(result.totalElements);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load payments');
    } finally {
      setLoading(false);
    }
  }, [paginationModel]);

  useEffect(() => { loadPayments(); }, [loadPayments]);

  const handleInitiate = async () => {
    setSaving(true);
    try {
      await paymentApi.initiate(formPayload);
      setDialogOpen(false);
      loadPayments();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to initiate payment');
    } finally {
      setSaving(false);
    }
  };

  return (
    <>
      <Box display="flex" justifyContent="space-between" alignItems="flex-start" mb={3}>
        <Box>
          <Typography variant="h5">Payments</Typography>
          <Typography variant="body2" color="text.secondary" mt={0.5}>
            Initiate and track payment transactions
          </Typography>
        </Box>
        <Button variant="contained" startIcon={<AddIcon />} onClick={() => setDialogOpen(true)}>
          New Payment
        </Button>
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Paper>
        <PaymentTable
          rows={payments}
          rowCount={rowCount}
          loading={loading}
          paginationModel={paginationModel}
          onPaginationModelChange={setPaginationModel}
        />
      </Paper>

      <FormDialog
        open={dialogOpen}
        title="Initiate Payment"
        onClose={() => setDialogOpen(false)}
        onSubmit={handleInitiate}
        submitLabel="Send Payment"
        loading={saving}
      >
        <InitiatePaymentForm onChange={setFormPayload} />
      </FormDialog>
    </>
  );
}
