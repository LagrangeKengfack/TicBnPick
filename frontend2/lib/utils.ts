/**
 * @file lib/utils.ts
 */

import { clsx, type ClassValue } from 'clsx'
import { twMerge } from 'tailwind-merge'

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}


// by MELI, for the expedition process


// Données des pays et régions
export const countries = {
  cameroun: {
    name: 'Cameroun',
    regions: {
      'centre': {
        name: 'Centre',
        cities: ['Yaoundé', 'Mbalmayo', 'Akonolinga', 'Bafia', 'Ntui', 'Mfou', 'Obala', 'Okola', 'Soa']
      },
      'littoral': {
        name: 'Littoral', 
        cities: ['Douala', 'Edéa', 'Nkongsamba', 'Yabassi', 'Loum', 'Manjo', 'Mbanga', 'Mouanko']
      },
      'ouest': {
        name: 'Ouest',
        cities: ['Bafoussam', 'Dschang', 'Bandjoun', 'Mbouda', 'Bangangté', 'Foumban', 'Kékem']
      },
      'nord-ouest': {
        name: 'Nord-Ouest',
        cities: ['Bamenda', 'Kumbo', 'Wum', 'Ndop', 'Mbengwi', 'Bali', 'Bafut']
      },
      'sud-ouest': {
        name: 'Sud-Ouest', 
        cities: ['Buéa', 'Limbe', 'Kumba', 'Mamfe', 'Tiko', 'Idenau', 'Fontem']
      },
      'adamaoua': {
        name: 'Adamaoua',
        cities: ['Ngaoundéré', 'Meiganga', 'Tibati', 'Tignère', 'Banyo', 'Kontcha']
      },
      'nord': {
        name: 'Nord',
        cities: ['Garoua', 'Maroua', 'Guider', 'Figuil', 'Poli', 'Rey-Bouba', 'Tcholliré']
      },
      'extreme-nord': {
        name: 'Extrême-Nord',
        cities: ['Maroua', 'Mokolo', 'Kousséri', 'Yagoua', 'Mora', 'Waza', 'Kaélé']
      },
      'est': {
        name: 'Est',
        cities: ['Bertoua', 'Batouri', 'Abong-Mbang', 'Yokadouma', 'Kenzou', 'Garoua-Boulaï']
      },
      'sud': {
        name: 'Sud',
        cities: ['Ebolowa', 'Sangmélima', 'Kribi', 'Ambam', 'Lolodorf', 'Campo', 'Mvangane']
      }
    }
  },
  nigeria: {
    name: 'Nigeria',
    regions: {
      'lagos': {
        name: 'Lagos',
        cities: ['Lagos', 'Ikeja', 'Epe', 'Ikorodu', 'Badagry', 'Mushin', 'Alimosho']
      },
      'abuja': {
        name: 'Abuja FCT',
        cities: ['Abuja', 'Gwagwalada', 'Kuje', 'Abaji', 'Bwari', 'Kwali']
      },
      'kano': {
        name: 'Kano',
        cities: ['Kano', 'Wudil', 'Gwarzo', 'Rano', 'Karaye', 'Rimin Gado']
      },
      'rivers': {
        name: 'Rivers',
        cities: ['Port Harcourt', 'Obio-Akpor', 'Eleme', 'Ikwerre', 'Oyigbo', 'Okrika']
      },
      'oyo': {
        name: 'Oyo',
        cities: ['Ibadan', 'Ogbomoso', 'Oyo', 'Iseyin', 'Saki', 'Igboho', 'Eruwa']
      },
      'kaduna': {
        name: 'Kaduna', 
        cities: ['Kaduna', 'Zaria', 'Kafanchan', 'Kagoro', 'Zonkwa', 'Makarfi']
      },
      'ogun': {
        name: 'Ogun',
        cities: ['Abeokuta', 'Sagamu', 'Ijebu-Ode', 'Ota', 'Ilaro', 'Ayetoro']
      },
      'anambra': {
        name: 'Anambra',
        cities: ['Awka', 'Onitsha', 'Nnewi', 'Ekwulobia', 'Agulu', 'Ihiala']
      }
    }
  }
} as const;

// Add type definitions for better type safety
export type CountryKey = keyof typeof countries;

export const haversineDistance = ([lat1, lon1]: [number, number], [lat2, lon2]: [number, number]): number => {
  const toRad = (x: number) => x * Math.PI / 180;
  const R = 6371; 
  const dLat = toRad(lat2 - lat1);
  const dLon = toRad(lon2 - lon1);
  const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
    Math.sin(dLon / 2) * Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return R * c;
};

// Calcul du prix du trajet
export const calculateTravelPrice = (distance: number) => {
    if (distance <= 0) return 0;
    const baseFee = 500; 
    const pricePerKm = 80; 
    return Math.round(baseFee + distance * pricePerKm);
};

// END MELI