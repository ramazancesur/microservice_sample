import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { LedgerPage } from './pages/LedgerPage';

export default function LedgerApp() {
  return (
    <Routes>
      <Route index element={<LedgerPage />} />
    </Routes>
  );
}
