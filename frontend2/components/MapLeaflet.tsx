"use client"

import React from 'react'
import { MapContainer, TileLayer, Marker, Popup, Polyline } from 'react-leaflet'
import type { LatLngExpression } from 'leaflet'
import type { GeoJSON } from 'geojson'

type Props = {
  center?: LatLngExpression
  zoom?: number
  markers?: { position: LatLngExpression; label?: string }[]
  route?: GeoJSON.Feature | null
}

export default function MapLeaflet({ center = [5.33, -4.03], zoom = 12, markers = [], route = null }: Props) {
  return (
    <div className="w-full h-96">
      <MapContainer center={center} zoom={zoom} style={{ height: '100%', width: '100%' }}>
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        {markers.map((m, idx) => (
          <Marker position={m.position} key={idx}>
            <Popup>{m.label || 'Point'}</Popup>
          </Marker>
        ))}
        {route && (() => {
          try {
            // Expect LineString coordinates in [lon, lat]
            // Convert to [lat, lon] for Leaflet Polyline
            // @ts-ignore
            const coords = route.type === 'Feature' && route.geometry && (route.geometry as any).coordinates
            const latlngs = coords?.map((c: any) => [c[1], c[0]]) || []
            return <Polyline positions={latlngs} pathOptions={{ color: '#f97316', weight: 5 }} />
          } catch (e) {
            return null
          }
        })()}
      </MapContainer>
    </div>
  )
}
