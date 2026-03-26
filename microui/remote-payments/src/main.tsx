import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import PaymentsApp from './PaymentsApp';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <BrowserRouter>
      <PaymentsApp />
    </BrowserRouter>
  </React.StrictMode>
);
