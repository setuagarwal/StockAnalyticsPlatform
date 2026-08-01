import { apiClient } from '../../../services/apiClient';
import type { HistoricalPriceResponse } from '../types/historicalPrice';

export async function getHistoricalPrices(
  symbol: string,
  range = 'ONE_MONTH',
  interval = 'ONE_DAY'
): Promise<HistoricalPriceResponse> {
  const response = await apiClient.get<HistoricalPriceResponse>(
    `/instruments/${encodeURIComponent(symbol)}/historical-prices`,
    {
      params: {
        range,
        interval
      }
    }
  );

  return response.data;
}