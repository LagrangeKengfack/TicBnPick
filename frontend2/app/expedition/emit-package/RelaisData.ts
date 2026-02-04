export interface PointRelais {
  id: string;
  relayPointName: string;
  latitude: number;
  longitude: number;
  relay_point_address?: string;
  relay_point_locality?: string;
}

export const YAOUNDE_CENTER: [number, number] = [3.8480, 11.5021];

const yaoundePointsRelais: PointRelais[] = [
  { id: 'p-1', relayPointName: 'Point Relais Centre', latitude: 3.848, longitude: 11.502 },
  { id: 'p-2', relayPointName: 'Point Relais Est', latitude: 3.854, longitude: 11.510 },
  { id: 'p-3', relayPointName: 'Point Relais Ouest', latitude: 3.842, longitude: 11.494 }
];

export default yaoundePointsRelais;
