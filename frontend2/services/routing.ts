export async function getRoute(lat1:number, lon1:number, lat2:number, lon2:number) {
  // Using public OSRM demo server (not for heavy production use)
  const url = `https://router.project-osrm.org/route/v1/driving/${lon1},${lat1};${lon2},${lat2}?overview=full&geometries=geojson`
  const res = await fetch(url)
  if (!res.ok) throw new Error('Routing failed')
  const data = await res.json()
  return data
}
