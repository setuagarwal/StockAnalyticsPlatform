import { Box, Divider, List, ListItemButton, ListItemText, Typography } from '@mui/material';

export function LeftPanel() {
  return (
    <Box component="aside" sx={{ width: 280, borderRight: '1px solid', borderColor: 'divider', bgcolor: 'background.paper', overflow: 'auto' }}>
      <Box sx={{ p: 2 }}>
        <Typography variant="subtitle2" color="text.secondary">Watchlist</Typography>
      </Box>
      <Divider />
      <List dense>
        {['Recent Searches', 'Saved Workspaces', 'Favourite Stocks'].map((item) => (
          <ListItemButton key={item}>
            <ListItemText primary={item} />
          </ListItemButton>
        ))}
      </List>
    </Box>
  );
}
