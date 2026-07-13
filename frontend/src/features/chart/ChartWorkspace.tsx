import { Box, Paper, Typography } from '@mui/material';

export function ChartWorkspace() {
  return (
    <Box component="main" sx={{ flex: 1, minWidth: 0, p: 2, bgcolor: 'background.default' }}>
      <Paper variant="outlined" sx={{ height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <Box sx={{ textAlign: 'center' }}>
          <Typography variant="h5">Chart Workspace</Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>Search and select a stock to begin analysis.</Typography>
        </Box>
      </Paper>
    </Box>
  );
}
