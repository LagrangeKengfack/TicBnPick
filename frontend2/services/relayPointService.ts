export type RelayPoint = {
  id: string;
  relayPointName: string;
  latitude: number;
  longitude: number;
  relay_point_address?: string;
  relay_point_locality?: string;
};

const SAMPLE_POINTS: RelayPoint[] = [
  { id: 'r-1', relayPointName: 'Relais Centre', latitude: 3.848, longitude: 11.502 },
  { id: 'r-2', relayPointName: 'Relais Est', latitude: 3.854, longitude: 11.510 },
  { id: 'r-3', relayPointName: 'Relais Ouest', latitude: 3.842, longitude: 11.494 }
];

export const relayPointService = {
  getAllRelayPoints: async (): Promise<RelayPoint[]> => {
    // stub: return sample data
    return Promise.resolve(SAMPLE_POINTS);
  }
};

export default relayPointService;
