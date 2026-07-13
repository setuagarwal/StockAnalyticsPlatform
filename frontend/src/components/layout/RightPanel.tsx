import { Box, Divider, Tab, Tabs, Typography } from '@mui/material';

export function RightPanel() {
  return (
    <Box component="aside" sx={{ width: 340, borderLeft: '1px solid', borderColor: 'divider', bgcolor: 'background.paper', overflow: 'auto' }}>
      <Tabs value={0} variant="fullWidth">
        <Tab label="AI" />
        <Tab label="News" />
        <Tab label="Patterns" />
      </Tabs>
      <Divider />
      <Box sx={{ p: 2 }}>
        <Typography variant="subtitle1">AI Insights</Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>Select a stock to generate technical insights.</Typography>
      </Box>
    </Box>
  );
}
