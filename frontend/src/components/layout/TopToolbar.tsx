import { useEffect, useState } from 'react';

import SearchIcon from '@mui/icons-material/Search';
import SettingsIcon from '@mui/icons-material/Settings';
import NotificationsIcon from '@mui/icons-material/Notifications';

import {
  Box,
  Button,
  CircularProgress,
  List,
  ListItemButton,
  ListItemText,
  Paper,
  TextField,
  Typography
} from '@mui/material';

import { searchInstruments } from '../../services/instrumentSearchApi';

import type { Instrument } from '../../types/instrument';

interface TopToolbarProps {
  onInstrumentSelected: (
    instrument: Instrument
  ) => void;
}

export function TopToolbar({
  onInstrumentSelected
}: TopToolbarProps) {
  const [query, setQuery] =
    useState('');

  const [results, setResults] =
    useState<Instrument[]>([]);

  const [loading, setLoading] =
    useState(false);

  useEffect(() => {
    if (query.trim().length < 2) {
      setResults([]);
      return;
    }

    const timeoutId = setTimeout(() => {
      setLoading(true);

      searchInstruments(query)
        .then((response) => {
          setResults(response.data);
        })
        .catch((error) => {
          console.error(
            'Search failed:',
            error
          );

          setResults([]);
        })
        .finally(() => {
          setLoading(false);
        });
    }, 300);

    return () => {
      clearTimeout(timeoutId);
    };
  }, [query]);

  const handleSelect = (
    instrument: Instrument
  ) => {
    onInstrumentSelected(instrument);

    setQuery('');

    setResults([]);
  };

  return (
    <Box
      component="header"
      sx={{
        height: 56,
        display: 'flex',
        alignItems: 'center',
        gap: 2,
        px: 2,
        borderBottom: '1px solid',
        borderColor: 'divider',
        bgcolor: 'background.paper',
        position: 'relative'
      }}
    >
      <Typography
        variant="h6"
        sx={{
          whiteSpace: 'nowrap'
        }}
      >
        Stock Analytics
      </Typography>

      <Box
        sx={{
          position: 'relative'
        }}
      >
        <TextField
          size="small"
          value={query}
          placeholder="Search NSE/BSE stock..."
          onChange={(event) => {
            setQuery(
              event.target.value
            );
          }}
          InputProps={{
            startAdornment: (
              <SearchIcon
                fontSize="small"
                sx={{ mr: 1 }}
              />
            ),
            endAdornment: loading ? (
              <CircularProgress
                size={16}
              />
            ) : null
          }}
          sx={{
            width: 360
          }}
        />

        {results.length > 0 && (
          <Paper
            elevation={8}
            sx={{
              position: 'absolute',
              top: 44,
              left: 0,
              width: 360,
              zIndex: 1000,
              maxHeight: 300,
              overflowY: 'auto'
            }}
          >
            <List dense>
              {results.map(
                (instrument) => (
                  <ListItemButton
                    key={
                      instrument.symbol
                    }
                    onClick={() => {
                      handleSelect(
                        instrument
                      );
                    }}
                  >
                    <ListItemText
                      primary={
                        `${instrument.symbol} (${instrument.exchange})`
                      }
                      secondary={
                        instrument.name
                      }
                    />
                  </ListItemButton>
                )
              )}
            </List>
          </Paper>
        )}
      </Box>

      <Button
        variant="outlined"
        size="small"
      >
        1D
      </Button>

      <Button
        variant="outlined"
        size="small"
      >
        Indicators
      </Button>

      <Button
        variant="outlined"
        size="small"
      >
        Workspace
      </Button>

      <Button
        variant="outlined"
        size="small"
        startIcon={
          <NotificationsIcon />
        }
      >
        Alerts
      </Button>

      <Box sx={{ flex: 1 }} />

      <Button
        variant="text"
        size="small"
        startIcon={<SettingsIcon />}
      >
        Settings
      </Button>
    </Box>
  );
}