import { Box } from '@mui/material';
import { TopToolbar } from './TopToolbar';
import { LeftPanel } from './LeftPanel';
import { RightPanel } from './RightPanel';
import { BottomPanel } from './BottomPanel';
import { ChartWorkspace } from '../../features/chart/ChartWorkspace';

export function MainLayout() {
  return (
    <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <TopToolbar />
      <Box sx={{ flex: 1, display: 'flex', minHeight: 0 }}>
        <LeftPanel />
        <ChartWorkspace />
        <RightPanel />
      </Box>
      <BottomPanel />
    </Box>
  );
}
