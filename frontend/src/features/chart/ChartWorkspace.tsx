import { useEffect, useRef, useState } from 'react';
import {
  Box,
  Button,
  ButtonGroup,
  CircularProgress,
  Paper,
  Typography
} from '@mui/material';
import {
  ColorType,
  createChart,
  type CandlestickData,
  type IChartApi,
  type Time
} from 'lightweight-charts';

import { getHistoricalPrices } from './api/historicalPriceApi';
import type {
  HistoricalPriceResponse
} from './types/historicalPrice';

type HistoricalRange =
  | 'ONE_MONTH'
  | 'ONE_YEAR'
  | 'TWO_YEARS'
  | 'FIVE_YEARS';

interface TimeframeOption {
  label: string;
  description: string;
  range: HistoricalRange;
}

const TIMEFRAME_OPTIONS: TimeframeOption[] = [
  {
    label: '1M',
    description: '1 Month',
    range: 'ONE_MONTH'
  },
  {
    label: '1Y',
    description: '1 Year',
    range: 'ONE_YEAR'
  },
  {
    label: '2Y',
    description: '2 Years',
    range: 'TWO_YEARS'
  },
  {
    label: '5Y',
    description: '5 Years',
    range: 'FIVE_YEARS'
  }
];

// remove these constants

interface ChartWorkspaceProps {
  symbol: string;
  companyName: string;
}

export function ChartWorkspace({
  symbol,
  companyName
}: ChartWorkspaceProps) {
  const chartContainerRef =
    useRef<HTMLDivElement | null>(null);

  const chartRef =
    useRef<IChartApi | null>(null);

  const [response, setResponse] =
    useState<HistoricalPriceResponse | null>(null);

  const [selectedRange, setSelectedRange] =
    useState<HistoricalRange>('ONE_MONTH');

  const [error, setError] =
    useState<string | null>(null);

  const [isLoading, setIsLoading] =
    useState(true);

  const selectedTimeframe =
    TIMEFRAME_OPTIONS.find(
      (option) => option.range === selectedRange
    );

  useEffect(() => {
    let isCancelled = false;

    setIsLoading(true);
    setError(null);

    getHistoricalPrices(
      symbol,
      selectedRange,
      'ONE_DAY'
    )
      .then((data) => {
        if (!isCancelled) {
          setResponse(data);
        }
      })
      .catch((requestError: unknown) => {
        console.error(
          'Historical-price request failed:',
          requestError
        );

        if (!isCancelled) {
          setResponse(null);
          setError(
            'Unable to load historical prices'
          );
        }
      })
      .finally(() => {
        if (!isCancelled) {
          setIsLoading(false);
        }
      });

    return () => {
      isCancelled = true;
    };
}, [symbol, selectedRange]);

  useEffect(() => {
    const container =
      chartContainerRef.current;

    if (
      !container
      || !response
      || response.data.length === 0
    ) {
      return;
    }

    const chart = createChart(container, {
      width: container.clientWidth,
      height: container.clientHeight,
      layout: {
        background: {
          type: ColorType.Solid,
          color: '#121212'
        },
        textColor: '#d1d4dc'
      },
      grid: {
        vertLines: {
          color: '#2b2b2b'
        },
        horzLines: {
          color: '#2b2b2b'
        }
      },
      rightPriceScale: {
        borderColor: '#444444'
      },
      timeScale: {
        borderColor: '#444444',
        timeVisible: false
      }
    });

    chartRef.current = chart;

    const candlestickSeries =
      chart.addCandlestickSeries({
        upColor: '#26a69a',
        downColor: '#ef5350',
        borderVisible: false,
        wickUpColor: '#26a69a',
        wickDownColor: '#ef5350'
      });

    const candleData:
      CandlestickData<Time>[] =
        response.data.map((point) => ({
          time: point.date as Time,
          open: point.open,
          high: point.high,
          low: point.low,
          close: point.close
        }));

    candlestickSeries.setData(candleData);
    chart.timeScale().fitContent();

    let resizeFrameId: number | null = null;

    const resizeChart = () => {
      if (resizeFrameId !== null) {
        cancelAnimationFrame(resizeFrameId);
      }

      resizeFrameId = requestAnimationFrame(() => {
        const width =
          container.clientWidth;

        const height =
          container.clientHeight;

        if (width > 0 && height > 0) {
          chart.applyOptions({
            width,
            height
          });

          chart.timeScale().fitContent();
        }
      });
    };

    resizeChart();

    const resizeObserver =
      new ResizeObserver(() => {
        resizeChart();
      });

    resizeObserver.observe(container);

    window.addEventListener(
      'resize',
      resizeChart
    );

    return () => {
      resizeObserver.disconnect();

      window.removeEventListener(
        'resize',
        resizeChart
      );

      if (resizeFrameId !== null) {
        cancelAnimationFrame(resizeFrameId);
      }

      chart.remove();
      chartRef.current = null;
    };
  }, [response]);

  return (
    <Box
      component="main"
      sx={{
        flex: 1,
        minWidth: 0,
        minHeight: 0,
        p: 2,
        bgcolor: 'background.default'
      }}
    >
      <Paper
        variant="outlined"
        sx={{
          height: '100%',
          minHeight: 0,
          minWidth: 0,
          display: 'flex',
          flexDirection: 'column',
          overflow: 'hidden'
        }}
      >
        <Box
          sx={{
            px: 2,
            py: 1.5,
            flexShrink: 0,
            borderBottom: '1px solid',
            borderColor: 'divider'
          }}
        >
          <Box
            sx={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              gap: 2
            }}
          >
            <Typography variant="h6">
              {companyName}
            </Typography>

            <ButtonGroup
              size="small"
              aria-label="Historical timeframe"
            >
              {TIMEFRAME_OPTIONS.map((option) => (
                <Button
                  key={option.range}
                  variant={
                    selectedRange === option.range
                      ? 'contained'
                      : 'outlined'
                  }
                  disabled={isLoading}
                  onClick={() => {
                    setSelectedRange(option.range);
                  }}
                >
                  {option.label}
                </Button>
              ))}
            </ButtonGroup>
          </Box>

          {response && (
            <Typography
              variant="body2"
              color="text.secondary"
              sx={{ mt: 0.5 }}
            >
              {symbol} ·{' '}
              {selectedTimeframe?.description
                ?? selectedRange}{' '}
              · {response.providerDisplayName}
            </Typography>
          )}
        </Box>

        <Box
          sx={{
            flex: 1,
            minHeight: 0,
            minWidth: 0,
            position: 'relative'
          }}
        >
          {isLoading && (
            <Box
              sx={{
                position: 'absolute',
                inset: 0,
                zIndex: 2,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                bgcolor: 'rgba(18, 18, 18, 0.45)'
              }}
            >
              <CircularProgress />
            </Box>
          )}

          {error && (
            <Box
              sx={{
                position: 'absolute',
                inset: 0,
                zIndex: 2,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
              }}
            >
              <Typography color="error">
                {error}
              </Typography>
            </Box>
          )}

          {!isLoading
            && !error
            && response?.data.length === 0 && (
              <Box
                sx={{
                  position: 'absolute',
                  inset: 0,
                  zIndex: 2,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center'
                }}
              >
                <Typography
                  color="text.secondary"
                >
                  No historical prices found.
                </Typography>
              </Box>
            )}

          <Box
            ref={chartContainerRef}
            sx={{
              position: 'absolute',
              inset: 0,
              minWidth: 0,
              minHeight: 0
            }}
          />
        </Box>
      </Paper>
    </Box>
  );
}