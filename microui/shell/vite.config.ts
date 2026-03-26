import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import federation from '@originjs/vite-plugin-federation';

const shared = {
  react: { singleton: true, requiredVersion: '^18.3.1' },
  'react-dom': { singleton: true, requiredVersion: '^18.3.1' },
  'react-router-dom': { singleton: true, requiredVersion: '^6.26.2' },
  '@mui/material': { singleton: true, requiredVersion: '^5.16.7' },
  '@mui/x-data-grid': { singleton: true, requiredVersion: '^7.14.0' },
  '@emotion/react': { singleton: true },
  '@emotion/styled': { singleton: true },
};

export default defineConfig({
  plugins: [
    react(),
    federation({
      name: 'shell',
      remotes: {
        remoteCustomers: 'http://localhost:3001/assets/remoteEntry.js',
        remoteAccounts: 'http://localhost:3002/assets/remoteEntry.js',
        remotePayments: 'http://localhost:3003/assets/remoteEntry.js',
        remoteLedger: 'http://localhost:3004/assets/remoteEntry.js',
      },
      shared,
    }),
  ],
  server: { port: 3000, cors: true },
  preview: { port: 3000, cors: true },
  build: { target: 'esnext', minify: false, cssCodeSplit: false },
});
