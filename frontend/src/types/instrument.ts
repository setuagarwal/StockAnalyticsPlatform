export interface Instrument {
  exchange: string;
  symbol: string;
  name: string;
  instrumentType: string;
  country: string;
}

export interface InstrumentSearchResponse {
  data: Instrument[];
  providerCode: string;
  providerDisplayName: string;
}