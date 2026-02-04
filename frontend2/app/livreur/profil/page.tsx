'use client'

import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useRouter } from 'next/navigation'
import {
  ArrowLeft,
  User,
  Mail,
  Phone,
  MapPin,
  Star,
  Package,
  Calendar,
  Edit,
  LogOut,
  Trash2,
  AlertTriangle,
  CheckCircle2,
  Settings
} from 'lucide-react'
import { useToast } from '@/hooks/use-toast'

export default function LivreurProfile() {
  const router = useRouter()
  const { toast } = useToast()
  const [isEditing, setIsEditing] = useState(false)
  const [showDeleteDialog, setShowDeleteDialog] = useState(false)

  // Données fictives du livreur
  const [livreurData, setLivreurData] = useState({
    firstName: 'Kouamé',
    lastName: 'Jean',
    email: 'kouame.jean@email.com',
    phone: '+225 07 00 00 00 00',
    address: 'Cocody, Rue des Jardins',
    city: 'Abidjan',
    rating: 4.8,
    totalDeliveries: 156,
    memberSince: '2023-03-20'
  })

  const [editedData, setEditedData] = useState({ ...livreurData })

  const handleEdit = () => {
    setIsEditing(true)
    setEditedData({ ...livreurData })
  }

  const handleCancel = () => {
    setIsEditing(false)
    setEditedData({ ...livreurData })
  }

  const handleSave = () => {
    setLivreurData({ ...editedData })
    setIsEditing(false)
    toast({
      title: 'Profil mis à jour',
      description: 'Vos informations ont été enregistrées avec succès',
    })
  }

  const handleLogout = () => {
    toast({
      title: 'Déconnexion',
      description: 'Vous avez été déconnecté avec succès',
    })
    router.push('/livreur')
  }

  const handleDeleteAccount = () => {
    toast({
      title: 'Compte supprimé',
      description: 'Votre compte a été supprimé définitivement',
    })
    router.push('/')
  }

  return (
    <div className="min-h-screen flex flex-col bg-gray-50">
      {/* Header */}
      <header className="sticky top-0 z-50 bg-white border-b border-gray-200 shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center gap-4 h-16">
            <Button
              variant="ghost"
              size="icon"
              onClick={() => router.back()}
              className="text-gray-700 hover:text-orange-600"
            >
              <ArrowLeft className="w-6 h-6" />
            </Button>
            <div>
              <h1 className="text-xl font-bold text-gray-900">Mon Profil</h1>
              <p className="text-sm text-gray-600">Gérez votre compte</p>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex-1 p-4 sm:p-6 lg:p-8">
        <div className="max-w-4xl mx-auto space-y-6">
          {/* Profile Overview Card */}
          <Card className="bg-gradient-to-r from-orange-500 to-amber-500 text-white border-0 shadow-lg">
            <CardContent className="p-6">
              <div className="flex items-center gap-6">
                <div className="w-20 h-20 bg-white/20 rounded-full flex items-center justify-center border-4 border-white/30">
                  <User className="w-10 h-10 text-white" />
                </div>
                <div className="flex-1">
                  <h2 className="text-2xl font-bold mb-1">{livreurData.lastName} {livreurData.firstName}</h2>
                  <div className="flex items-center gap-4 text-sm opacity-90">
                    <div className="flex items-center gap-1">
                      <Star className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                      <span className="font-semibold">{livreurData.rating}</span>
                    </div>
                    <span>•</span>
                    <span>Membre depuis {new Date(livreurData.memberSince).toLocaleDateString('fr-FR', { month: 'long', year: 'numeric' })}</span>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Personal Information Card */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <User className="w-5 h-5 text-orange-600" />
                Informations personnelles
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="lastName">Nom</Label>
                  {isEditing ? (
                    <Input
                      id="lastName"
                      value={editedData.lastName}
                      onChange={(e) => setEditedData({ ...editedData, lastName: e.target.value })}
                      className="mt-1"
                    />
                  ) : (
                    <div className="flex items-center gap-2 p-3 bg-gray-50 rounded-lg mt-1">
                      <User className="w-5 h-5 text-gray-400" />
                      <span className="font-medium">{livreurData.lastName}</span>
                    </div>
                  )}
                </div>

                <div>
                  <Label htmlFor="firstName">Prénom</Label>
                  {isEditing ? (
                    <Input
                      id="firstName"
                      value={editedData.firstName}
                      onChange={(e) => setEditedData({ ...editedData, firstName: e.target.value })}
                      className="mt-1"
                    />
                  ) : (
                    <div className="flex items-center gap-2 p-3 bg-gray-50 rounded-lg mt-1">
                      <User className="w-5 h-5 text-gray-400" />
                      <span className="font-medium">{livreurData.firstName}</span>
                    </div>
                  )}
                </div>

                <div>
                  <Label htmlFor="email">Email</Label>
                  {isEditing ? (
                    <Input
                      id="email"
                      type="email"
                      value={editedData.email}
                      onChange={(e) => setEditedData({ ...editedData, email: e.target.value })}
                      className="mt-1"
                    />
                  ) : (
                    <div className="flex items-center gap-2 p-3 bg-gray-50 rounded-lg mt-1">
                      <Mail className="w-5 h-5 text-gray-400" />
                      <span className="font-medium">{livreurData.email}</span>
                    </div>
                  )}
                </div>

                <div>
                  <Label htmlFor="phone">Téléphone</Label>
                  {isEditing ? (
                    <Input
                      id="phone"
                      type="tel"
                      value={editedData.phone}
                      onChange={(e) => setEditedData({ ...editedData, phone: e.target.value })}
                      className="mt-1"
                    />
                  ) : (
                    <div className="flex items-center gap-2 p-3 bg-gray-50 rounded-lg mt-1">
                      <Phone className="w-5 h-5 text-gray-400" />
                      <span className="font-medium">{livreurData.phone}</span>
                    </div>
                  )}
                </div>

                <div>
                  <Label htmlFor="city">Ville</Label>
                  {isEditing ? (
                    <Input
                      id="city"
                      value={editedData.city}
                      onChange={(e) => setEditedData({ ...editedData, city: e.target.value })}
                      className="mt-1"
                    />
                  ) : (
                    <div className="flex items-center gap-2 p-3 bg-gray-50 rounded-lg mt-1">
                      <MapPin className="w-5 h-5 text-gray-400" />
                      <span className="font-medium">{livreurData.city}</span>
                    </div>
                  )}
                </div>
              </div>

              <div>
                <Label htmlFor="address">Adresse</Label>
                {isEditing ? (
                  <Input
                    id="address"
                    value={editedData.address}
                    onChange={(e) => setEditedData({ ...editedData, address: e.target.value })}
                    className="mt-1"
                  />
                ) : (
                  <div className="flex items-center gap-2 p-3 bg-gray-50 rounded-lg mt-1">
                    <MapPin className="w-5 h-5 text-gray-400" />
                    <span className="font-medium">{livreurData.address}</span>
                  </div>
                )}
              </div>

              {isEditing && (
                <div className="flex gap-3 pt-4">
                  <Button
                    onClick={handleSave}
                    className="flex-1 bg-gradient-to-r from-orange-500 to-amber-500 hover:from-orange-600 hover:to-amber-600 text-white"
                  >
                    <CheckCircle2 className="w-4 h-4 mr-2" />
                    Enregistrer
                  </Button>
                  <Button
                    onClick={handleCancel}
                    variant="outline"
                    className="flex-1"
                  >
                    Annuler
                  </Button>
                </div>
              )}
            </CardContent>
          </Card>

          {/* Account Actions Card */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Settings className="w-5 h-5 text-orange-600" />
                Actions du compte
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <Button
                onClick={() => setIsEditing(true)}
                variant="outline"
                className="w-full justify-start h-12 border-2 border-orange-200 hover:bg-orange-50 hover:border-orange-300 text-orange-700"
              >
                <Edit className="w-5 h-5 mr-3" />
                <span className="font-medium">Modifier mon compte</span>
              </Button>

              <Button
                onClick={handleLogout}
                variant="outline"
                className="w-full justify-start h-12 border-2 border-blue-200 hover:bg-blue-50 hover:border-blue-300 text-blue-700"
              >
                <LogOut className="w-5 h-5 mr-3" />
                <span className="font-medium">Déconnexion</span>
              </Button>

              <Button
                onClick={() => setShowDeleteDialog(true)}
                variant="outline"
                className="w-full justify-start h-12 border-2 border-orange-200 hover:bg-orange-50 hover:border-orange-300 text-orange-600"
              >
                <Trash2 className="w-5 h-5 mr-3" />
                <span className="font-medium">Supprimer le compte</span>
              </Button>
            </CardContent>
          </Card>

          {/* Delete Confirmation Dialog */}
          {showDeleteDialog && (
            <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
              <Card className="max-w-md w-full border-2 border-orange-200">
                <CardHeader>
                  <div className="flex items-center gap-3">
                    <div className="w-12 h-12 bg-orange-100 rounded-full flex items-center justify-center">
                      <AlertTriangle className="w-6 h-6 text-orange-600" />
                    </div>
                    <div>
                      <CardTitle className="text-xl text-orange-900">Supprimer le compte ?</CardTitle>
                      <p className="text-sm text-orange-700 mt-1">Cette action est irréversible</p>
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <p className="text-gray-600 mb-6">
                    Êtes-vous sûr de vouloir supprimer votre compte ? Cette action est irréversible et toutes vos données seront définitivement perdues.
                  </p>
                  <div className="flex gap-3">
                    <Button
                      onClick={() => setShowDeleteDialog(false)}
                      variant="outline"
                      className="flex-1"
                    >
                      Annuler
                    </Button>
                    <Button
                      onClick={handleDeleteAccount}
                      className="flex-1 bg-orange-600 hover:bg-orange-700 text-white"
                    >
                      <Trash2 className="w-4 h-4 mr-2" />
                      Supprimer
                    </Button>
                  </div>
                </CardContent>
              </Card>
            </div>
          )}
        </div>
      </main>
    </div>
  )
}
