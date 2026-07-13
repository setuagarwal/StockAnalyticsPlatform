import SearchIcon from '@mui/icons-material/Search';
import SettingsIcon from '@mui/icons-material/Settings';
import NotificationsIcon from '@mui/icons-material/Notifications';
import { Box, Button, TextField, Typography } from '@mui/material';

export function TopToolbar() {
  return (
    <Box component="header" sx={{ height: 56, display: 'flex', alignItems: 'center', gap: 2, px: 2, borderBottom: '1px solid', borderColor: 'divider', bgcolor: 'background.paper' }}>
      <Typography variant="h6" sx={{ whiteSpace: 'nowrap' }}>Stock Analytics</Typography>
      <TextField size="small" placeholder="Search NSE/BSE stock..." InputProps={{ startAdornment: <SearchIcon fontSize="small" sx={{ mr: 1 }} /> }} sx={{ width: 360 }} />
      <Button variant="outlined" size="small">1D</Button>
      <Button variant="outlined" size="small">Indicators</Button>
      <Button variant="outlined" size="small">Workspace</Button>
      <Button variant="outlined" size="small" startIcon={<NotificationsIcon />}>Alerts</Button>
      <Box sx={{ flex: 1 }} />
      <Button variant="text" size="small" startIcon={<SettingsIcon />}>Settings</Button>
    </Box>
  );
}
