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
