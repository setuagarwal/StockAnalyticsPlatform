import { Box, Tab, Tabs } from '@mui/material';

export function BottomPanel() {
  return (
    <Box component="footer" sx={{ height: 140, borderTop: '1px solid', borderColor: 'divider', bgcolor: 'background.paper' }}>
      <Tabs value={0}>
        <Tab label="Notes" />
        <Tab label="Alerts" />
        <Tab label="Logs" />
        <Tab label="Status" />
      </Tabs>
    </Box>
  );
}
