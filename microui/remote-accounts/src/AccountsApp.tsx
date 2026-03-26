import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { AccountListPage } from './pages/AccountListPage';

export default function AccountsApp() {
  return (
    <Routes>
      <Route index element={<AccountListPage />} />
    </Routes>
  );
}
