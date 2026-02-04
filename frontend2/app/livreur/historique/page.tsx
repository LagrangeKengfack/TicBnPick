'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { Card, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import {
  Package,
  MapPin,
  Clock,
  Star,
  CheckCircle2,
  ArrowLeft,
  Search,
  Calendar as CalendarIcon,
  Map as MapIcon,
  CreditCard,
  TrendingUp,
  Phone,
} from 'lucide-react'
import { cn } from '@/lib/utils'

export default function HistoriqueLivraisons() {
  const router = useRouter()
  const [searchTerm, setSearchTerm] = useState('')
  const [filterPeriod, setFilterPeriod] = useState('all')

  // Historique complet des livraisons
  const allDeliveries = [
    {
      id: 'TBP-2024-156',
      customerName: 'Kouassi Mariam',
      deliveryAddress: 'Marcory, Rue 12',
      distance: 4.5,
      earnings: 1200,
      completedAt: '09:45',
      completedDate: '27/01/2025',
      rating: 5,
      tip: 500,
      packageType: 'Repas à domicile',
      pickupAddress: 'Cocody, Rue des Jardins'
    },
    {
      id: 'TBP-2024-155',
      customerName: 'Diop Abib',
      deliveryAddress: 'Cocody, Deux-Plateaux',
      distance: 7.2,
      earnings: 1800,
      completedAt: '08:30',
      completedDate: '27/01/2025',
      rating: 4,
      tip: 0,
      packageType: 'Documents urgents',
      pickupAddress: 'Plateau, Avenue de la République'
    },
    {
      id: 'TBP-2024-154',
      customerName: 'Yao Esther',
      deliveryAddress: 'Yopougon, Zone 4',
      distance: 10.5,
      earnings: 2500,
      completedAt: '19:20',
      completedDate: '26/01/2025',
      rating: 5,
      tip: 1000,
      packageType: 'Courses urgentes',
      pickupAddress: 'Abobo, Baoulé'
    },
    {
      id: 'TBP-2024-153',
      customerName: 'Koffi Aya',
      deliveryAddress: 'Plateau, Stade Général De Gaulle',
      distance: 3.8,
      earnings: 1000,
      completedAt: '17:45',
      completedDate: '26/01/2025',
      rating: 5,
      tip: 200,
      packageType: 'Pharmacie & Santé',
      pickupAddress: 'Marcory, Biétry'
    },
    {
      id: 'TBP-2024-152',
      customerName: 'Amani Yao',
      deliveryAddress: 'Treichville, Boulevard Mitterrand',
      distance: 8.3,
      earnings: 2000,
      completedAt: '15:30',
      completedDate: '26/01/2025',
      rating: 4,
      tip: 0,
      packageType: 'Colis',
      pickupAddress: 'Cocody, Angre'
    },
    {
      id: 'TBP-2024-151',
      customerName: 'Kouame Paul',
      deliveryAddress: 'Abobo, Derrière le pont',
      distance: 12.7,
      earnings: 3000,
      completedAt: '12:15',
      completedDate: '26/01/2025',
      rating: 5,
      tip: 500,
      packageType: 'Courses urgentes',
      pickupAddress: 'Yopougon, Sicogi'
    },
    {
      id: 'TBP-2024-150',
      customerName: 'Touré Aminata',
      deliveryAddress: 'Marcory, Rue 18',
      distance: 6.2,
      earnings: 1500,
      completedAt: '10:50',
      completedDate: '25/01/2025',
      rating: 4,
      tip: 300,
      packageType: 'Repas à domicile',
      pickupAddress: 'Plateau, Rue 12'
    },
    {
      id: 'TBP-2024-149',
      customerName: 'Koné Ibrahim',
      deliveryAddress: 'Cocody, Riviera',
      distance: 9.5,
      earnings: 2200,
      completedAt: '18:20',
      completedDate: '25/01/2025',
      rating: 5,
      tip: 800,
      packageType: 'Documents urgents',
      pickupAddress: 'Treichville, Marché Central'
    },
    {
      id: 'TBP-2024-148',
      customerName: 'Diallo Fatou',
      deliveryAddress: 'Yopougon, Andokoi',
      distance: 5.8,
      earnings: 1400,
      completedAt: '14:10',
      completedDate: '25/01/2025',
      rating: 4,
      tip: 0,
      packageType: 'Pharmacie & Santé',
      pickupAddress: 'Cocody, Ivoire'
    },
    {
      id: 'TBP-2024-147',
      customerName: 'Bakayoko Souleymane',
      deliveryAddress: 'Plateau, Ministère de la Santé',
      distance: 7.1,
      earnings: 1700,
      completedAt: '11:30',
      completedDate: '24/01/2025',
      rating: 5,
      tip: 400,
      packageType: 'Documents urgents',
      pickupAddress: 'Marcory, Rue 22'
    }
  ]

  // Filtrer les livraisons selon la recherche et la période
  const filteredDeliveries = allDeliveries.filter(delivery => {
    const matchesSearch =
      delivery.id.toLowerCase().includes(searchTerm.toLowerCase()) ||
      delivery.customerName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      delivery.deliveryAddress.toLowerCase().includes(searchTerm.toLowerCase())

    let matchesPeriod = true
    const today = new Date()
    const deliveryDate = new Date(delivery.completedDate.split('/').reverse().join('-'))

    if (filterPeriod === 'today') {
      matchesPeriod = deliveryDate.toDateString() === today.toDateString()
    } else if (filterPeriod === 'week') {
      const weekAgo = new Date(today.getTime() - 7 * 24 * 60 * 60 * 1000)
      matchesPeriod = deliveryDate >= weekAgo
    } else if (filterPeriod === 'month') {
      const monthAgo = new Date(today.getTime() - 30 * 24 * 60 * 60 * 1000)
      matchesPeriod = deliveryDate >= monthAgo
    }

    return matchesSearch && matchesPeriod
  })

  const handleGoBack = () => {
    router.back()
  }

  return (
    <div className="min-h-screen flex flex-col bg-gray-50">
      {/* Header */}
      <header className="sticky top-0 z-50 bg-white border-b shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center gap-4">
              <Button
                variant="ghost"
                size="icon"
                onClick={handleGoBack}
                className="hover:bg-gray-100"
              >
                <ArrowLeft className="w-5 h-5" />
              </Button>
              <div>
                <h1 className="text-xl font-bold text-gray-900">Historique des livraisons</h1>
                <p className="text-sm text-gray-500">Toutes vos livraisons complétées</p>
              </div>
            </div>
            <Button
              variant="outline"
              size="sm"
              className="gap-2"
            >
              <DownloadIcon className="w-4 h-4" />
              Exporter
            </Button>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex-1 p-4 sm:p-6 lg:p-8">
        <div className="max-w-7xl mx-auto space-y-6">
          {/* Search and Filter Section */}
          <Card>
            <CardContent className="p-6">
              <div className="flex flex-col md:flex-row gap-4">
                <div className="flex-1">
                  <Label htmlFor="search" className="text-sm font-semibold text-gray-800 mb-2">
                    Rechercher une livraison
                  </Label>
                  <div className="relative">
                    <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-400" />
                    <Input
                      id="search"
                      placeholder="Numéro, client ou adresse..."
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                      className="pl-10"
                    />
                  </div>
                </div>
                <div className="md:w-64">
                  <Label htmlFor="period" className="text-sm font-semibold text-gray-800 mb-2">
                    Période
                  </Label>
                  <select
                    id="period"
                    value={filterPeriod}
                    onChange={(e) => setFilterPeriod(e.target.value)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500"
                  >
                    <option value="all">Toutes les livraisons</option>
                    <option value="month">30 derniers jours</option>
                    <option value="week">7 derniers jours</option>
                    <option value="today">Aujourd'hui</option>
                  </select>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Deliveries List */}
          {filteredDeliveries.length > 0 && (
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <h2 className="text-xl font-bold text-gray-900">
                  Liste des livraisons
                </h2>
                <p className="text-sm text-gray-500">
                  {filteredDeliveries.length} livraison{filteredDeliveries.length > 1 ? 's' : ''} trouvée{filteredDeliveries.length > 1 ? 's' : ''}
                </p>
              </div>

              {filteredDeliveries.map((delivery) => (
                <Card key={delivery.id} className="hover:shadow-lg transition-shadow">
                  <CardContent className="p-4 sm:p-6">
                    <div className="flex flex-col lg:flex-row lg:items-start justify-between gap-4">
                      <div className="flex-1 space-y-3">
                        {/* Header */}
                        <div className="flex items-center gap-3 mb-2">
                          <h3 className="font-bold text-lg text-gray-900">{delivery.id}</h3>
                          <Badge variant="outline" className="bg-green-100 text-green-700 border-green-300">
                            <CheckCircle2 className="w-3 h-3 mr-1" />
                            Livré
                          </Badge>
                          <Badge variant="outline" className="bg-orange-100 text-orange-700 border-orange-300">
                            {delivery.packageType}
                          </Badge>
                        </div>

                        {/* Customer and Dates */}
                        <div className="flex flex-col sm:flex-row sm:items-center gap-2 sm:gap-6 text-sm text-gray-600">
                          <div className="flex items-center gap-2">
                            <Phone className="w-3 h-3" />
                            <span className="font-medium">Client:</span> {delivery.customerName}
                          </div>
                          <div className="flex items-center gap-2">
                            <CalendarIcon className="w-3 h-3" />
                            <span>{delivery.completedDate}</span>
                          </div>
                          <div className="flex items-center gap-2">
                            <Clock className="w-3 h-3" />
                            <span>{delivery.completedAt}</span>
                          </div>
                        </div>

                        {/* Addresses */}
                        <div className="space-y-2">
                          <div className="flex items-start gap-2 text-sm">
                            <div className="w-2 h-2 bg-green-500 rounded-full mt-1.5 flex-shrink-0" />
                            <p className="text-gray-700">
                              <span className="font-medium">Retrait:</span> {delivery.pickupAddress}
                            </p>
                          </div>
                          <div className="flex items-start gap-2 text-sm">
                            <div className="w-2 h-2 bg-red-500 rounded-full mt-1.5 flex-shrink-0" />
                            <p className="text-gray-700">
                              <span className="font-medium">Livraison:</span> {delivery.deliveryAddress}
                            </p>
                          </div>
                        </div>

                        {/* Stats */}
                        <div className="flex flex-wrap gap-4 text-sm">
                          <div className="flex items-center gap-1 text-gray-600">
                            <MapIcon className="w-3 h-3" />
                            {delivery.distance} km
                          </div>
                          <div className="flex items-center gap-1 text-green-600 font-semibold">
                            <CreditCard className="w-3 h-3" />
                            {delivery.earnings.toLocaleString()} FCFA
                          </div>
                          {delivery.tip > 0 && (
                            <div className="flex items-center gap-1 text-purple-600 font-semibold">
                              <TrendingUp className="w-3 h-3" />
                              +{delivery.tip.toLocaleString()} FCFA (pourboire)
                            </div>
                          )}
                        </div>
                      </div>

                      {/* Rating */}
                      <div className="flex items-center gap-2 bg-yellow-50 px-4 py-3 rounded-lg">
                        <span className="text-sm font-semibold text-gray-700">Note:</span>
                        <div className="flex items-center gap-1">
                          {[...Array(5)].map((_, i) => (
                            <Star
                              key={i}
                              className={cn(
                                "w-5 h-5",
                                i < delivery.rating ? "text-yellow-500 fill-current" : "text-gray-300"
                              )}
                            />
                          ))}
                        </div>
                        <span className="text-2xl font-bold text-yellow-600 ml-1">{delivery.rating}</span>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}

          {/* No Results Message */}
          {filteredDeliveries.length === 0 && (
            <div className="text-center py-12">
              <Package className="w-16 h-16 mx-auto text-gray-300 mb-4" />
              <h3 className="text-lg font-semibold text-gray-900 mb-2">
                Aucune livraison trouvée
              </h3>
              <p className="text-gray-500">
                Essayez de modifier vos critères de recherche ou de filtre
              </p>
            </div>
          )}
        </div>
      </main>

      {/* Footer */}
      <footer className="w-full bg-white border-t py-4 mt-auto">
        <div className="max-w-7xl mx-auto px-4 text-center text-sm text-gray-500">
          <p>© 2025 TiiBnPick - Historique des livraisons</p>
        </div>
      </footer>
    </div>
  )
}

function DownloadIcon(props: React.SVGProps<SVGSVGElement>) {
  return (
    <svg
      {...props}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
      <polyline points="7 10 12 15 17 10" />
      <line x1="12" y1="15" x2="12" y2="3" />
    </svg>
  )
}
