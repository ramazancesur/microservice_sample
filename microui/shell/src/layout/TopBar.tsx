import { AppBar, Toolbar, Typography } from '@mui/material';
import { useLocation } from 'react-router-dom';

const pageTitles: Record<string, string> = {
  '/customers': 'Customers',
  '/accounts': 'Accounts',
  '/payments': 'Payments',
  '/ledger': 'Ledger',
};

function getPageTitle(pathname: string): string {
  const match = Object.entries(pageTitles).find(([key]) => pathname.startsWith(key));
  return match ? match[1] : 'Banking Platform';
}

export function TopBar() {
  const location = useLocation();
  const title = getPageTitle(location.pathname);

  return (
    <AppBar position="static" color="inherit" elevation={0}
      sx={{ borderBottom: '1px solid', borderColor: 'divider' }}>
      <Toolbar>
        <Typography variant="h6" color="text.primary">
          {title}
        </Typography>
      </Toolbar>
    </AppBar>
  );
}
