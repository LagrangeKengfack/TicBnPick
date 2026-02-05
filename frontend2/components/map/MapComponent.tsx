"use client"

import React, { useState } from 'react'
import MapLeaflet from '@/components/MapLeaflet'
import { geocode } from '@/services/geocoding'
import { getRoute } from '@/services/routing'

type Props = {
  onMapReady?: (map: any) => void
  initialCenter?: [number, number]
  initialZoom?: number
}

export default function MapComponent({ onMapReady, initialCenter = [5.33, -4.03], initialZoom = 12 }: Props) {
  const [center, setCenter] = useState<[number, number]>(initialCenter)
  const [pickup, setPickup] = useState('')
  const [delivery, setDelivery] = useState('')
  const [markers, setMarkers] = useState<any[]>([])
  const [route, setRoute] = useState<any | null>(null)

  const handleGeocodePickup = async () => {
    if (!pickup) return alert('Entrez une adresse de retrait')
    try {
      const res = await geocode(pickup)
      if (res && res.length > 0) {
        const first = res[0]
        const lat = parseFloat(first.lat)
        const lon = parseFloat(first.lon)
        setMarkers((m) => [{ position: [lat, lon], label: 'Retrait' }, ...m])
        setCenter([lat, lon])
      } else {
        alert('Aucun résultat')
      }
    } catch (e) {
      console.error(e)
      alert('Erreur de géocodage')
    }
  }

  const handleGeocodeDelivery = async () => {
    if (!delivery) return alert('Entrez une adresse de livraison')
    try {
      const res = await geocode(delivery)
      if (res && res.length > 0) {
        const first = res[0]
        const lat = parseFloat(first.lat)
        const lon = parseFloat(first.lon)
        setMarkers((m) => [...m, { position: [lat, lon], label: 'Livraison' }])
        setCenter([lat, lon])
      } else {
        alert('Aucun résultat')
      }
    } catch (e) {
      console.error(e)
      alert('Erreur de géocodage')
    }
  }

  const handleRoute = async () => {
    if (markers.length < 2) return alert('Placez au moins deux points (retrait et livraison)')
    const [from, to] = [markers[0].position, markers[markers.length - 1].position]
    try {
      // markers store [lat, lon], but getRoute wants lat, lon order
      const data = await getRoute(from[0], from[1], to[0], to[1])
      if (data && data.routes && data.routes.length > 0) {
        const geom = data.routes[0].geometry
        // geom is a GeoJSON LineString
        setRoute({ type: 'Feature', geometry: geom, properties: {} })
      } else {
        alert('Aucun itinéraire trouvé')
      }
    } catch (e) {
      console.error(e)
      alert('Erreur de routage')
    }
  }

  return (
    <div className="space-y-4">
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
        <div className="flex gap-2">
          <input value={pickup} onChange={(e) => setPickup(e.target.value)} placeholder="Adresse de retrait" className="flex-1 px-3 py-2 rounded border" />
          <button onClick={handleGeocodePickup} className="px-3 py-2 bg-orange-500 text-white rounded">Chercher</button>
        </div>
        <div className="flex gap-2">
          <input value={delivery} onChange={(e) => setDelivery(e.target.value)} placeholder="Adresse de livraison" className="flex-1 px-3 py-2 rounded border" />
          <button onClick={handleGeocodeDelivery} className="px-3 py-2 bg-orange-500 text-white rounded">Chercher</button>
        </div>
      </div>

      <div className="flex gap-2">
        <button onClick={handleRoute} className="px-4 py-2 bg-green-600 text-white rounded">Tracer itinéraire</button>
        <button onClick={() => { setMarkers([]); setRoute(null); }} className="px-4 py-2 bg-gray-200 rounded">Réinitialiser</button>
      </div>

      <MapLeaflet center={center} zoom={initialZoom} markers={markers} route={route} />
    </div>
  )
}
