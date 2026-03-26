import React from 'react';
import { Box } from '@mui/material';
import { Sidebar } from './Sidebar';
import { TopBar } from './TopBar';

interface AppLayoutProps {
  children: React.ReactNode;
}

export function AppLayout({ children }: AppLayoutProps) {
  return (
    <Box display="flex" minHeight="100vh" bgcolor="background.default">
      <Sidebar />
      <Box display="flex" flexDirection="column" flexGrow={1} overflow="hidden">
        <TopBar />
        <Box component="main" flexGrow={1} p={3} overflow="auto">
          {children}
        </Box>
      </Box>
    </Box>
  );
}
