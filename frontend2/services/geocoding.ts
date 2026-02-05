/**
 * @file lib/geocoding.ts
 * @description Utilities for device geolocation and reverse geocoding.
 */

export async function geocode(query: string) {
  const url = `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(query)}&format=json&limit=5&addressdetails=1`
  const res = await fetch(url, {
    headers: {
      'Accept': 'application/json'
    }
  })
  if (!res.ok) throw new Error('Geocoding failed')
  return res.json()
}

export async function reverseGeocode(lat: number, lon: number) {
  const url = `https://nominatim.openstreetmap.org/reverse?lat=${lat}&lon=${lon}&format=json&addressdetails=1`
  const res = await fetch(url, { headers: { 'Accept': 'application/json' } })
  if (!res.ok) throw new Error('Reverse geocoding failed')
  return res.json()
}


// meli

export const getDeviceLocation = (): Promise<GeolocationPosition> => {
  return new Promise((resolve, reject) => {
    if (!navigator.geolocation) {
      reject(
        new Error(
          "La géolocalisation n'est pas supportée par votre navigateur",
        ),
      );
    }
    navigator.geolocation.getCurrentPosition(resolve, reject, {
      enableHighAccuracy: true,
      timeout: 5000,
      maximumAge: 0,
    });
  });
};

export const getAddressFromCoords = async (
  lat: number,
  lng: number,
): Promise<string> => {
  try {
    const response = await fetch(
      `https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}&zoom=18&addressdetails=1`,
      { headers: { "Accept-Language": "fr" } },
    );
    const data = await response.json();
    // Return road/street or a fallback
    return data.display_name || "Adresse introuvable";
  } catch (error) {
    console.error("Geocoding error:", error);
    return "Erreur lors de la récupération de l'adresse";
  }
};

//end meli