import { Suspense, lazy } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, CssBaseline, CircularProgress, Box } from '@mui/material';
import { theme } from './theme/theme';
import { AppLayout } from './layout/AppLayout';

const CustomersApp = lazy(() => import('remoteCustomers/CustomersApp'));
const AccountsApp = lazy(() => import('remoteAccounts/AccountsApp'));
const PaymentsApp = lazy(() => import('remotePayments/PaymentsApp'));
const LedgerApp = lazy(() => import('remoteLedger/LedgerApp'));

function RemoteLoader() {
  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="60vh">
      <CircularProgress />
    </Box>
  );
}

export default function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <BrowserRouter>
        <AppLayout>
          <Suspense fallback={<RemoteLoader />}>
            <Routes>
              <Route path="/" element={<Navigate to="/customers" replace />} />
              <Route path="/customers/*" element={<CustomersApp />} />
              <Route path="/accounts/*" element={<AccountsApp />} />
              <Route path="/payments/*" element={<PaymentsApp />} />
              <Route path="/ledger/*" element={<LedgerApp />} />
            </Routes>
          </Suspense>
        </AppLayout>
      </BrowserRouter>
    </ThemeProvider>
  );
}
