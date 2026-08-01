export interface HistoricalPricePoint {
    timestamp: string;
    open: number;
    high: number;
    low: number;
    close: number;
    adjustedClose: number | null;
    volume: number | null;
}

export interface HistoricalPriceResponse {
    data: HistoricalPricePoint[];
    providerCode: string;
    providerDisplayName: string;
}

export interface HistoricalPricePoint {
  date: string;
  open: number;
  high: number;
  low: number;
  close: number;
  adjustedClose: number | null;
  volume: number | null;
}

export interface HistoricalPriceResponse {
  data: HistoricalPricePoint[];
  providerCode: string;
  providerDisplayName: string;
}