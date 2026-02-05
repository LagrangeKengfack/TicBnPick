"use client"

import React, { useState, useEffect } from 'react'
import { motion } from 'framer-motion'
import { ArrowLeft, Loader2 } from 'lucide-react'
import MapLeaflet from '@/components/MapLeaflet'
import { geocode } from '@/services/geocoding'
import { getRoute } from '@/services/routing'
import type { GeoJSON } from 'geojson'
import { RouteData, RouteSelectionStepProps } from '@/types/package'
import { calculateTravelPrice } from '@/lib/utils'


export default function RouteSelectionStep({ onContinue, onBack, initialDepartureAddress, initialArrivalAddress }: RouteSelectionStepProps) {
  const [routeData, setRouteData] = useState<RouteData>({
    departurePointId: null,
    arrivalPointId: null,
    departurePointName: initialDepartureAddress || '',
    arrivalPointName: initialArrivalAddress || '',
    distanceKm: 0,
  })

  const [travelPrice, setTravelPrice] = useState(0)
  const [isLoading, setIsLoading] = useState(false)
  const [markers, setMarkers] = useState<any[]>([])
  const [routeGeoJSON, setRouteGeoJSON] = useState<GeoJSON.Feature | null>(null)

  useEffect(() => {
    const setupFromAddresses = async () => {
      if (!initialDepartureAddress || !initialArrivalAddress) return
      setIsLoading(true)
      try {
        const dep = await geocode(initialDepartureAddress)
        const arr = await geocode(initialArrivalAddress)
        if (dep && dep.length > 0 && arr && arr.length > 0) {
          const depLat = parseFloat(dep[0].lat)
          const depLon = parseFloat(dep[0].lon)
          const arrLat = parseFloat(arr[0].lat)
          const arrLon = parseFloat(arr[0].lon)

          setMarkers([
            { position: [depLat, depLon], label: 'Retrait' },
            { position: [arrLat, arrLon], label: 'Livraison' },
          ])

          const data = await getRoute(depLat, depLon, arrLat, arrLon)
          if (data && data.routes && data.routes.length > 0) {
            const route = data.routes[0]
            const distanceKm = Math.round((route.distance / 1000) * 100) / 100
            setRouteData(prev => ({ ...prev, distanceKm, departurePointName: initialDepartureAddress || '', arrivalPointName: initialArrivalAddress || '' }))
            setTravelPrice(calculateTravelPrice(distanceKm))
            setRouteGeoJSON({ type: 'Feature', geometry: route.geometry as any, properties: {} })
            setRouteData(prev => ({ ...prev, departurePointId: 'from-address', arrivalPointId: 'to-address' }))
          } else {
            alert('Aucun itinéraire trouvé entre ces adresses.')
          }
        } else {
          alert('Impossible de géocoder une ou plusieurs adresses fournies.')
        }
      } catch (e) {
        console.error('Erreur geocode/route:', e)
        alert('Erreur lors du traitement des adresses.')
      } finally {
        setIsLoading(false)
      }
    }

    setupFromAddresses()
  }, [initialDepartureAddress, initialArrivalAddress])

  const handleReset = () => {
    setMarkers([])
    setRouteGeoJSON(null)
    setRouteData({ departurePointId: null, arrivalPointId: null, departurePointName: '', arrivalPointName: '', distanceKm: 0 })
    setTravelPrice(0)
  }

  const handleSubmit = () => {
    if (routeData.distanceKm > 0) {
      onContinue(routeData, travelPrice)
    } else {
      alert('Aucun itinéraire calculé.')
    }
  }

  return (
    <motion.div initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }} className="h-screen flex flex-col bg-gray-50 dark:bg-transparent">
      <div className="bg-white dark:bg-gray-800 shadow-sm border-b border-gray-200 dark:border-gray-700 px-4 py-4 flex-shrink-0">
        <div className="flex items-center gap-4">
          <button onClick={onBack} className="p-2 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-lg transition-colors">
            <ArrowLeft className="w-5 h-5 text-gray-600 dark:text-gray-300" />
          </button>
          <div>
            <h2 className="text-xl font-bold text-gray-800 dark:text-gray-100">Sélection de l'itinéraire</h2>
            <p className="text-sm text-gray-500 dark:text-gray-400">Itinéraire calculé à partir des adresses fournies.</p>
          </div>
        </div>
      </div>

      <div className="flex-1 relative">
        <MapLeaflet center={markers[0]?.position ?? [5.33, -4.03]} markers={markers} route={routeGeoJSON} />

        {isLoading && (
          <div className="absolute inset-0 flex items-center justify-center z-20 bg-black/10">
            <Loader2 className="animate-spin text-orange-500" />
          </div>
        )}

        <div className="absolute top-4 left-4 right-4 lg:left-auto lg:w-80">
          <div className="bg-white dark:bg-gray-800 rounded-xl shadow-lg border border-gray-200 dark:border-gray-700 p-4">
            <div className="flex items-center justify-between mb-3">
              <h4 className="font-semibold text-gray-800 dark:text-gray-100">Coût du trajet</h4>
              <button onClick={handleReset} className="p-1 hover:bg-gray-100 dark:hover:bg-gray-700 rounded text-gray-500 dark:text-gray-400">
                Réinitialiser
              </button>
            </div>

            <div className="space-y-3">
              <div className="flex items-center gap-3">
                <div className="w-3 h-3 bg-orange-500 rounded-full flex-shrink-0" />
                <div className="flex-1 min-w-0">
                  <p className="text-xs text-gray-500 dark:text-gray-400">Départ</p>
                  <p className="font-medium text-sm truncate text-gray-800 dark:text-gray-100">{routeData.departurePointName || 'À sélectionner'}</p>
                </div>
              </div>

              <div className="flex items-center gap-3">
                <div className="w-3 h-3 bg-green-500 rounded-full flex-shrink-0" />
                <div className="flex-1 min-w-0">
                  <p className="text-xs text-gray-500 dark:text-gray-400">Arrivée</p>
                  <p className="font-medium text-sm truncate text-gray-800 dark:text-gray-100">{routeData.arrivalPointName || 'À sélectionner'}</p>
                </div>
              </div>

              {routeData.distanceKm > 0 && (
                <div className="pt-3 border-t border-gray-200 dark:border-gray-700">
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600 dark:text-gray-300">Distance: {routeData.distanceKm.toFixed(1)} km</span>
                    <span className="font-bold text-orange-600 dark:text-orange-400">{travelPrice.toLocaleString()} FCFA</span>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>

      <div className="bg-white dark:bg-gray-800 border-t border-gray-200 dark:border-gray-700 px-4 py-4 flex-shrink-0">
        <div className="flex justify-end">
          <motion.button onClick={handleSubmit} whileHover={{ scale: 1.02 }} whileTap={{ scale: 0.98 }} className="inline-flex items-center justify-center bg-gradient-to-r from-orange-500 to-amber-500 text-white font-semibold py-3 px-6 rounded-xl shadow-lg hover:shadow-xl transition-all disabled:opacity-50 disabled:cursor-not-allowed">
            Continuer
          </motion.button>
        </div>
      </div>
    </motion.div>
  )
}

