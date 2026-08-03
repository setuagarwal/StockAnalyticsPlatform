import { Box } from '@mui/material';
import { useState } from 'react';

import { TopToolbar } from './TopToolbar';
import { LeftPanel } from './LeftPanel';
import { RightPanel } from './RightPanel';
import { BottomPanel } from './BottomPanel';

import { ChartWorkspace } from '../../features/chart/ChartWorkspace';

import type { Instrument } from '../../types/instrument';

export function MainLayout() {
  const [selectedInstrument, setSelectedInstrument] =
    useState<Instrument>({
      exchange: 'NSE',
      symbol: 'RELIANCE.NS',
      name: 'Reliance Industries',
      instrumentType: 'EQUITY',
      country: 'India'
    });

  return (
    <Box
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column'
      }}
    >
      <TopToolbar
        onInstrumentSelected={
          setSelectedInstrument
        }
      />

      <Box
        sx={{
          flex: 1,
          display: 'flex',
          minHeight: 0
        }}
      >
        <LeftPanel />

        <ChartWorkspace
          symbol={selectedInstrument.symbol}
          companyName={selectedInstrument.name}
        />

        <RightPanel />
      </Box>

      <BottomPanel />
    </Box>
  );
}