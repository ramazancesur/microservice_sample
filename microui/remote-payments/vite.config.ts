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
      name: 'remotePayments',
      filename: 'remoteEntry.js',
      exposes: { './PaymentsApp': './src/PaymentsApp' },
      shared,
    }),
  ],
  server: { port: 3003, cors: true },
  preview: { port: 3003, cors: true },
  build: { target: 'esnext', minify: false, cssCodeSplit: false },
});
