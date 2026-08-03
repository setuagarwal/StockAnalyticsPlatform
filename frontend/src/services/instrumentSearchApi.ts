import { apiClient } from './apiClient';
import type {
  InstrumentSearchResponse
} from '../types/instrument';

export async function searchInstruments(
  query: string
): Promise<InstrumentSearchResponse> {
  const trimmedQuery = query.trim();

  if (!trimmedQuery) {
    return {
      data: [],
      providerCode: '',
      providerDisplayName: ''
    };
  }

  const response =
    await apiClient.get<InstrumentSearchResponse>(
      '/instruments/search',
      {
        params: {
          query: trimmedQuery
        }
      }
    );

  return response.data;
}