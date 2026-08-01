import { useEffect, useState } from 'react';
import { Box, Paper, Typography } from '@mui/material';
import { getHistoricalPrices } from './api/historicalPriceApi';
import type { HistoricalPriceResponse } from './types/historicalPrice';

export function ChartWorkspace() {

  const [response, setResponse] =
    useState<HistoricalPriceResponse | null>(null);

  const [error, setError] =
    useState<string | null>(null);

  useEffect(() => {

    getHistoricalPrices(
      'RELIANCE.NS',
      'ONE_MONTH',
      'ONE_DAY'
    )
      .then((data) => {
        setResponse(data);
      })
      .catch((error) => {
        console.error(error);
        setError('Unable to load historical prices');
      });

  }, []);

  return (

    <Box
      component="main"
      sx={{
        flex: 1,
        minWidth: 0,
        p: 2,
        bgcolor: 'background.default'
      }}
    >

      <Paper
        variant="outlined"
        sx={{
          height: '100%',
          p: 2
        }}
      >

        <Typography variant="h5">
          Chart Workspace
        </Typography>

        {!response && !error && (

          <Typography sx={{ mt: 2 }}>
            Loading historical prices...
          </Typography>

        )}

        {error && (

          <Typography color="error" sx={{ mt: 2 }}>
            {error}
          </Typography>

        )}

        {response && (

          <>
            <Typography sx={{ mt: 2 }}>
              Provider: {response.providerDisplayName}
            </Typography>

            <Typography>
              Records received: {response.data.length}
            </Typography>

            <Typography sx={{ mt: 2 }}>
              First record:
            </Typography>

            <pre>
              {JSON.stringify(
                response.data[0],
                null,
                2
              )}
            </pre>
          </>

        )}

      </Paper>

    </Box>

  );
}