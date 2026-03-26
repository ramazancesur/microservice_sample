import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { CustomerListPage } from './pages/CustomerListPage';

export default function CustomersApp() {
  return (
    <Routes>
      <Route index element={<CustomerListPage />} />
    </Routes>
  );
}
