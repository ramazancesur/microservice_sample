import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { PaymentListPage } from './pages/PaymentListPage';

export default function PaymentsApp() {
  return (
    <Routes>
      <Route index element={<PaymentListPage />} />
    </Routes>
  );
}
