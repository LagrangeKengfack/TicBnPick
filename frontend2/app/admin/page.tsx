'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle } from '@/components/ui/alert-dialog'
import { useToast } from '@/hooks/use-toast'
import { cn } from '@/lib/utils'
import {
  Package,
  Users,
  ShieldCheck,
  AlertTriangle,
  CheckCircle,
  XCircle,
  Ban,
  RefreshCw,
  Clock,
  Phone,
  Mail,
  LayoutDashboard,
  UserCheck,
  LogOut,
  X,
  MoreHorizontal,
  MapPin,
  Bike,
  Car,
} from 'lucide-react'
import { TruckIcon } from '@heroicons/react/24/outline'
import { MdDeliveryDining } from 'react-icons/md'
import { BsBicycle } from 'react-icons/bs'

// Types
interface DeliveryPersonRequest {
  id: string
  name: string
  email: string
  phone: string
  location: string
  vehicleType: string
  vehicleBrand: string
  vehicleModel: string
  vehicleRegNumber: string
  status: 'PENDING' | 'APPROVED' | 'REJECTED'
  idCardVerified: boolean
  vehicleRegVerified: boolean
  insuranceVerified: boolean
  createdAt: string
  updatedAt: string
}

interface Account {
  id: string
  name: string
  email: string
  phone: string
  role: 'DELIVERY' | 'AGENCY' | 'POINT'
  status: 'ACTIVE' | 'SUSPENDED' | 'REVOKED'
  deliveriesCount: number
  lastActivityAt: string | null
  createdAt: string
  updatedAt: string
}

type ActiveView = 'dashboard' | 'registrations' | 'accounts'

export default function SuperAdminDashboard() {
  const { toast } = useToast()
  const router = useRouter()
  const [loading, setLoading] = useState(true)
  const [selectedRequest, setSelectedRequest] = useState<DeliveryPersonRequest | null>(null)
  const [selectedAccount, setSelectedAccount] = useState<Account | null>(null)
  const [actionDialog, setActionDialog] = useState<'approve' | 'reject' | 'suspend' | 'revoke' | 'restore' | null>(null)
  const [activeView, setActiveView] = useState<ActiveView>('dashboard')
  const [sidebarOpen, setSidebarOpen] = useState(true)
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)

  const [registrationRequests, setRegistrationRequests] = useState<DeliveryPersonRequest[]>([])
  const [accounts, setAccounts] = useState<Account[]>([])
  
  // Filtres pour la page comptes
  const [accountTypeFilter, setAccountTypeFilter] = useState<'all' | 'DELIVERY' | 'CLIENT'>('all')
  const [searchQuery, setSearchQuery] = useState('')

  // Super admin info
  const superAdminName = 'Charles Henry'

  const handleLogout = () => {
    router.push('/')
  }

  const sidebarItems = [
    { id: 'dashboard' as const, icon: LayoutDashboard, label: 'Dashboard' },
    { id: 'registrations' as const, icon: UserCheck, label: 'Inscriptions' },
    { id: 'accounts' as const, icon: ShieldCheck, label: 'Comptes' },
  ]

  // Fetch data on component mount
  useEffect(() => {
    fetchRegistrations()
    fetchAccounts()
  }, [])

  // Fetch registration requests
  const fetchRegistrations = async () => {
    try {
      const response = await fetch('/api/registrations')
      if (response.ok) {
        const data = await response.json()
        setRegistrationRequests(data)
      }
    } catch (error) {
      console.error('Error fetching registrations:', error)
    } finally {
      setLoading(false)
    }
  }

  // Fetch accounts
  const fetchAccounts = async () => {
    try {
      const response = await fetch('/api/accounts')
      if (response.ok) {
        const data = await response.json()
        setAccounts(data)
      }
    } catch (error) {
      console.error('Error fetching accounts:', error)
    }
  }

  // Stats calculations
  const stats = {
    pendingRegistrations: registrationRequests.filter(r => r.status === 'PENDING').length,
    activeDeliveryPersons: accounts.filter(a => a.role === 'DELIVERY' && a.status === 'ACTIVE').length,
    suspendedAccounts: accounts.filter(a => a.status === 'SUSPENDED').length,
    revokedAccounts: accounts.filter(a => a.status === 'REVOKED').length
  }

  // Handler functions
  const handleApproveRegistration = (request: DeliveryPersonRequest) => {
    setSelectedRequest(request)
    setActionDialog('approve')
  }

  const handleRejectRegistration = (request: DeliveryPersonRequest) => {
    setSelectedRequest(request)
    setActionDialog('reject')
  }

  const handleSuspendAccount = (account: Account) => {
    setSelectedAccount(account)
    setActionDialog('suspend')
  }

  const handleRevokeAccount = (account: Account) => {
    setSelectedAccount(account)
    setActionDialog('revoke')
  }

  const handleRestoreAccount = (account: Account) => {
    setSelectedAccount(account)
    setActionDialog('restore')
  }

  const confirmAction = async () => {
    try {
      if (actionDialog === 'approve' && selectedRequest) {
        const response = await fetch(`/api/registrations/${selectedRequest.id}/approve`, {
          method: 'POST'
        })

        if (response.ok) {
          toast({
            title: 'Inscription approuvée',
            description: `Le livreur ${selectedRequest.name} a été approuvé avec succès.`
          })
          await fetchRegistrations()
          await fetchAccounts()
        } else {
          throw new Error('Failed to approve')
        }
      } else if (actionDialog === 'reject' && selectedRequest) {
        const response = await fetch(`/api/registrations/${selectedRequest.id}/reject`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ reason: 'Rejeté par l\'administrateur' })
        })

        if (response.ok) {
          toast({
            title: 'Inscription rejetée',
            description: `La demande de ${selectedRequest.name} a été rejetée.`,
            variant: 'destructive'
          })
          await fetchRegistrations()
        } else {
          throw new Error('Failed to reject')
        }
      } else if (actionDialog === 'suspend' && selectedAccount) {
        const response = await fetch(`/api/accounts/${selectedAccount.id}/suspend`, {
          method: 'POST'
        })

        if (response.ok) {
          toast({
            title: 'Compte suspendu',
            description: `Le compte de ${selectedAccount.name} a été suspendu.`
          })
          await fetchAccounts()
        } else {
          throw new Error('Failed to suspend')
        }
      } else if (actionDialog === 'revoke' && selectedAccount) {
        const response = await fetch(`/api/accounts/${selectedAccount.id}/revoke`, {
          method: 'POST'
        })

        if (response.ok) {
          toast({
            title: 'Compte révoqué',
            description: `Le compte de ${selectedAccount.name} a été révoqué définitivement.`,
            variant: 'destructive'
          })
          await fetchAccounts()
        } else {
          throw new Error('Failed to revoke')
        }
      } else if (actionDialog === 'restore' && selectedAccount) {
        const response = await fetch(`/api/accounts/${selectedAccount.id}/restore`, {
          method: 'POST'
        })

        if (response.ok) {
          toast({
            title: 'Compte restauré',
            description: `Le compte de ${selectedAccount.name} a été restauré avec succès.`
          })
          await fetchAccounts()
        } else {
          throw new Error('Failed to restore')
        }
      }
    } catch (error) {
      console.error('Error performing action:', error)
      toast({
        title: 'Erreur',
        description: 'Une erreur est survenue lors de l\'opération.',
        variant: 'destructive'
      })
    }
    setActionDialog(null)
    setSelectedRequest(null)
    setSelectedAccount(null)
  }

  const getStatusBadge = (status: string) => {
    switch (status) {
      case 'PENDING':
        return <Badge variant="outline" className="bg-yellow-50 text-yellow-700 border-yellow-200"><Clock className="w-3 h-3 mr-1" /> En attente</Badge>
      case 'APPROVED':
      case 'ACTIVE':
        return <Badge variant="outline" className="bg-green-50 text-green-700 border-green-200"><CheckCircle className="w-3 h-3 mr-1" /> Actif</Badge>
      case 'REJECTED':
        return <Badge variant="destructive"><XCircle className="w-3 h-3 mr-1" /> Rejeté</Badge>
      case 'SUSPENDED':
        return <Badge variant="outline" className="bg-orange-50 text-orange-700 border-orange-200"><AlertTriangle className="w-3 h-3 mr-1" /> Suspendu</Badge>
      case 'REVOKED':
        return <Badge variant="destructive"><Ban className="w-3 h-3 mr-1" /> Révoqué</Badge>
      default:
        return <Badge>{status}</Badge>
    }
  }

  const getRoleBadge = (role: string) => {
    switch (role) {
      case 'DELIVERY':
        return <Badge variant="secondary" className="bg-orange-50 text-orange-700 border-orange-300">Livreur</Badge>
      case 'CLIENT':
        return <Badge variant="secondary" className="bg-white text-orange-700 border-orange-300">Client</Badge>
      case 'AGENCY':
        return <Badge variant="secondary" className="bg-purple-50 text-purple-700 border-purple-200">Agence</Badge>
      case 'POINT':
        return <Badge variant="secondary" className="bg-teal-50 text-teal-700 border-teal-200">Point Relais</Badge>
      default:
        return <Badge>{role}</Badge>
    }
  }

  // Fonction pour filtrer les comptes
  const getFilteredAccounts = () => {
    let filtered = accounts

    // Filtre par type de compte
    if (accountTypeFilter !== 'all') {
      filtered = filtered.filter(account => account.role === accountTypeFilter)
    }

    // Filtre par recherche (nom, prénom, email)
    if (searchQuery.trim()) {
      const query = searchQuery.toLowerCase()
      filtered = filtered.filter(account =>
        account.name.toLowerCase().includes(query) ||
        account.email.toLowerCase().includes(query)
      )
    }

    return filtered
  }

  // Composants SVG personnalisés pour les icônes de véhicules - mêmes que dans FormulaireColisExpedition
  const VehicleIcon = ({ type }: { type: string }) => {
    const vehicleType = type.toLowerCase()

    if (vehicleType.includes('moto') || vehicleType.includes('motorcycle')) {
      return <Bike className="w-6 h-6 text-orange-500" />
    } else if (vehicleType.includes('tricycle') || vehicleType.includes('tricy')) {
      return <MdDeliveryDining className="w-6 h-6 text-orange-500" />
    } else if (vehicleType.includes('camion') || vehicleType.includes('truck')) {
      return <TruckIcon className="w-6 h-6 text-orange-500" />
    } else if (vehicleType.includes('bus')) {
      return <TruckIcon className="w-6 h-6 text-orange-500" />
    } else if (vehicleType.includes('voiture') || vehicleType.includes('car') || vehicleType.includes('auto')) {
      return <Car className="w-6 h-6 text-orange-500" />
    } else if (vehicleType.includes('vélo') || vehicleType.includes('velo') || vehicleType.includes('bike') || vehicleType.includes('bicyclette')) {
      return <BsBicycle className="w-6 h-6 text-orange-500" />
    } else {
      // Par défaut, voiture
      return <Car className="w-6 h-6 text-orange-500" />
    }
  }

  const getVehicleIcon = (vehicleType: string) => {
    return (
      <div className="w-10 h-10 flex items-center justify-center rounded-full border-2 border-orange-500">
        <VehicleIcon type={vehicleType} />
      </div>
    )
  }

  const formatDate = (dateString: string) => {
    const date = new Date(dateString)
    return date.toLocaleDateString('fr-FR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    })
  }

  return (
    <div className="min-h-screen flex bg-background">
      {/* Sidebar - Vertical à gauche, toujours visible */}
      <aside className="fixed left-0 top-0 z-40 h-screen w-64 bg-orange-600 dark:bg-orange-700 flex flex-col border-r border-orange-700">
        {/* Logo */}
        <div className="h-20 flex items-center justify-center px-4 border-b border-orange-700">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-white rounded-lg flex items-center justify-center flex-shrink-0">
              <Package className="w-6 h-6 text-orange-600" />
            </div>
            <div className="text-white">
              <h1 className="text-lg font-bold leading-tight">
                TiiB<span className="text-orange-200">n</span>Pick
              </h1>
              <p className="text-xs text-orange-100">Super Admin</p>
            </div>
          </div>
        </div>

        {/* Navigation */}
        <nav className="flex-1 p-4 space-y-2 overflow-y-auto">
          {sidebarItems.map((item) => {
            const Icon = item.icon
            const isActive = activeView === item.id
            return (
              <button
                key={item.id}
                onClick={() => setActiveView(item.id)}
                className={cn(
                  'w-full flex items-center gap-3 px-4 py-3 rounded-lg transition-all',
                  isActive 
                    ? 'bg-white text-orange-600 font-semibold shadow-md' 
                    : 'text-white hover:bg-orange-500/50 hover:text-white'
                )}
              >
                <Icon className="w-5 h-5 flex-shrink-0" />
                <span>{item.label}</span>
              </button>
            )
          })}
        </nav>

        {/* User Section */}
        <div className="p-4 border-t border-orange-700">
          <div className="flex items-center gap-3 text-white">
            <div className="w-10 h-10 bg-orange-500 rounded-full flex items-center justify-center font-semibold flex-shrink-0">
              {superAdminName.split(' ').map(n => n[0]).join('')}
            </div>
            <div className="flex-1 min-w-0">
              <p className="font-medium truncate text-sm">{superAdminName}</p>
              <p className="text-xs text-orange-100 truncate">admin@tiibnpick.com</p>
            </div>
            <Button 
              variant="ghost" 
              size="icon" 
              className="flex-shrink-0 text-white hover:text-white hover:bg-orange-500/50"
              onClick={handleLogout}
            >
              <LogOut className="w-4 h-4" />
            </Button>
          </div>
        </div>
      </aside>

      {/* Main Content */}
      <div className="flex-1 flex flex-col min-h-screen ml-64">
        {/* Top Bar */}
        <header className="h-16 border-b bg-white dark:bg-gray-800 sticky top-0 z-30 flex items-center justify-between px-6">
          <div>
            <h2 className="text-xl font-semibold text-gray-800 dark:text-gray-100">
              {activeView === 'dashboard' && 'Dashboard'}
              {activeView === 'registrations' && 'Demandes d\'inscription'}
              {activeView === 'accounts' && 'Gestion des comptes'}
            </h2>
          </div>
          <Button 
            onClick={() => { fetchRegistrations(); fetchAccounts() }}
            variant="outline"
            size="sm"
          >
            <RefreshCw className="w-4 h-4 mr-2" />
            Actualiser
          </Button>
        </header>

        {/* Content */}
        <main className="flex-1 p-6 overflow-auto">
          {/* Dashboard View */}
          {activeView === 'dashboard' && (
            <div className="space-y-6">
              {/* Welcome Section */}
              <div className="mb-6">
                <h2 className="text-2xl md:text-3xl font-bold">
                  Bienvenue, <span className="text-orange-600">{superAdminName}</span>
                </h2>
              </div>

              {/* Stats Cards */}
              <div className="grid grid-cols-2 lg:grid-cols-5 gap-3 md:gap-4">
                <Card className="hover:shadow-md transition-shadow flex flex-col">
                  <CardHeader className="pb-3 px-4 py-3">
                    <CardDescription className="text-[10px] md:text-xs text-black">En attente</CardDescription>
                    <CardTitle className="text-xl md:text-2xl text-orange-600">{stats.pendingRegistrations}</CardTitle>
                  </CardHeader>
                  <CardContent className="px-4 pb-4 mt-auto">
                    <div className="flex items-center text-xs text-muted-foreground">
                      <Clock className="w-3 h-3 mr-1" />
                      Inscriptions
                    </div>
                  </CardContent>
                </Card>

                <Card className="hover:shadow-md transition-shadow flex flex-col">
                  <CardHeader className="pb-3 px-4 py-3">
                    <CardDescription className="text-[10px] md:text-xs text-black">Livreurs</CardDescription>
                    <CardTitle className="text-xl md:text-2xl text-green-600">{stats.activeDeliveryPersons}</CardTitle>
                  </CardHeader>
                  <CardContent className="px-4 pb-4 mt-auto">
                    <div className="flex items-center text-xs text-muted-foreground">
                      <Users className="w-3 h-3 mr-1" />
                      Actifs
                    </div>
                  </CardContent>
                </Card>

                <Card className="hover:shadow-md transition-shadow flex flex-col">
                  <CardHeader className="pb-3 px-4 py-3">
                    <CardDescription className="text-[10px] md:text-xs text-black">Suspendus</CardDescription>
                    <CardTitle className="text-xl md:text-2xl text-orange-600">{stats.suspendedAccounts}</CardTitle>
                  </CardHeader>
                  <CardContent className="px-4 pb-4 mt-auto">
                    <div className="flex items-center text-xs text-muted-foreground">
                      <AlertTriangle className="w-4 h-4 mr-1" />
                      Comptes
                    </div>
                  </CardContent>
                </Card>

                <Card className="hover:shadow-md transition-shadow lg:block flex flex-col">
                  <CardHeader className="pb-3 px-4 py-3">
                    <CardDescription className="text-xs text-black">Révoqués</CardDescription>
                    <CardTitle className="text-2xl text-red-600">{stats.revokedAccounts}</CardTitle>
                  </CardHeader>
                  <CardContent className="px-4 pb-4 mt-auto">
                    <div className="flex items-center text-xs text-muted-foreground">
                      <Ban className="w-4 h-4 mr-1" />
                      Comptes
                    </div>
                  </CardContent>
                </Card>
              </div>

              {/* Quick Actions */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-6">
                <Card className="cursor-pointer hover:shadow-md transition-shadow" onClick={() => setActiveView('registrations')}>
                  <CardHeader>
                    <CardTitle className="text-base md:text-lg flex items-center gap-2">
                      <UserCheck className="w-4 h-4 md:w-5 md:h-5 text-orange-500" />
                      Voir les inscriptions
                    </CardTitle>
                    <CardDescription className="text-xs md:text-sm">Consultez et validez les nouvelles demandes d'inscription</CardDescription>
                  </CardHeader>
                </Card>

                <Card className="cursor-pointer hover:shadow-md transition-shadow" onClick={() => setActiveView('accounts')}>
                  <CardHeader>
                    <CardTitle className="text-base md:text-lg flex items-center gap-2">
                      <ShieldCheck className="w-4 h-4 md:w-5 md:h-5 text-blue-500" />
                      Gérer les comptes
                    </CardTitle>
                    <CardDescription className="text-xs md:text-sm">Suspendez ou révoquez les comptes utilisateurs</CardDescription>
                  </CardHeader>
                </Card>
              </div>
            </div>
          )}

          {/* Registrations View */}
          {activeView === 'registrations' && (
            <div className="space-y-4">
              {loading ? (
                <div className="flex items-center justify-center py-16">
                  <div className="text-center text-muted-foreground">
                    Chargement...
                  </div>
                </div>
              ) : registrationRequests.length === 0 ? (
                <div className="flex items-center justify-center py-16">
                  <div className="text-center">
                    <div className="w-16 h-16 mx-auto mb-4 bg-muted rounded-full flex items-center justify-center">
                      <CheckCircle className="w-8 h-8 text-muted-foreground" />
                    </div>
                    <h3 className="text-lg font-medium">Aucune demande d'inscription</h3>
                    <p className="text-muted-foreground mt-2">
                      Il n'y a pour l'instant aucune demande d'inscription en attente de validation.
                    </p>
                  </div>
                </div>
              ) : (
                <div className="space-y-2">
                  {registrationRequests.map((request) => (
                    <Card
                      key={request.id}
                      onClick={() => setSelectedRequest(request)}
                      className="hover:shadow-md transition-colors cursor-pointer"
                    >
                      <CardContent className="p-4">
                        <div className="flex items-center justify-between gap-4">
                          {/* Left side - Vehicle icon and info */}
                          <div className="flex items-center gap-3 flex-1 min-w-0">
                            {/* Vehicle Icon */}
                            <div className="flex-shrink-0">
                              {getVehicleIcon(request.vehicleType)}
                            </div>

                            {/* Name and Vehicle Info */}
                            <div className="flex-1 min-w-0">
                              <div className="font-semibold text-sm truncate">{request.name}</div>
                              <div className="text-xs text-gray-600 truncate">
                                {request.vehicleBrand} {request.vehicleModel}
                              </div>
                            </div>
                          </div>

                          {/* Right side - License plate */}
                          <div className="flex-shrink-0">
                            <div className="font-mono text-lg font-bold text-orange-600">
                              {request.vehicleRegNumber}
                            </div>
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              )}
            </div>
          )}

          {/* Accounts View */}
          {activeView === 'accounts' && (
            <div className="space-y-4">
              {/* Filtres */}
              <div className="bg-white dark:bg-gray-800 p-4 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700 space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  {/* Filtre par type de compte */}
                  <div>
                    <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                      Type de compte
                    </label>
                    <div className="flex gap-2">
                      <button
                        onClick={() => setAccountTypeFilter('all')}
                        className={`px-4 py-2 rounded-lg text-sm font-medium transition-colors ${
                          accountTypeFilter === 'all'
                            ? 'bg-orange-600 text-white'
                            : 'bg-gray-100 dark:bg-gray-700 text-gray-800 dark:text-gray-200 hover:bg-gray-200 dark:hover:bg-gray-600'
                        }`}
                      >
                        Tous ({accounts.length})
                      </button>
                      <button
                        onClick={() => setAccountTypeFilter('DELIVERY')}
                        className={`px-4 py-2 rounded-lg text-sm font-medium transition-colors ${
                          accountTypeFilter === 'DELIVERY'
                            ? 'bg-orange-600 text-white'
                            : 'bg-gray-100 dark:bg-gray-700 text-gray-800 dark:text-gray-200 hover:bg-gray-200 dark:hover:bg-gray-600'
                        }`}
                      >
                        Livreurs ({accounts.filter(a => a.role === 'DELIVERY').length})
                      </button>
                      <button
                        onClick={() => setAccountTypeFilter('CLIENT')}
                        className={`px-4 py-2 rounded-lg text-sm font-medium transition-colors ${
                          accountTypeFilter === 'CLIENT'
                            ? 'bg-orange-600 text-white'
                            : 'bg-gray-100 dark:bg-gray-700 text-gray-800 dark:text-gray-200 hover:bg-gray-200 dark:hover:bg-gray-600'
                        }`}
                      >
                        Clients ({accounts.filter(a => a.role === 'CLIENT').length})
                      </button>
                    </div>
                  </div>

                  {/* Barre de recherche */}
                  <div>
                    <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                      Rechercher
                    </label>
                    <input
                      type="text"
                      placeholder="Nom, email..."
                      value={searchQuery}
                      onChange={(e) => setSearchQuery(e.target.value)}
                      className="w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 placeholder-gray-500 dark:placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-orange-500"
                    />
                  </div>
                </div>
              </div>

              {/* Tableau des comptes */}
              <Card>
                <CardHeader>
                  <div className="flex flex-col gap-3">
                    <div>
                      <CardTitle className="text-lg md:text-xl">Gestion des comptes</CardTitle>
                      <CardDescription>Gérez les comptes utilisateurs (suspendre, révoquer)</CardDescription>
                    </div>
                    <div className="flex gap-2">
                      <Badge variant="outline" className="text-xs md:text-sm">
                        {getFilteredAccounts().length} compte{getFilteredAccounts().length !== 1 ? 's' : ''}
                      </Badge>
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="rounded-md border max-h-[600px] overflow-auto">
                    {getFilteredAccounts().length === 0 ? (
                      <div className="p-8 text-center text-muted-foreground">
                        Aucun compte trouvé
                      </div>
                    ) : (
                      <Table>
                        <TableHeader>
                          <TableRow>
                            <TableHead>Utilisateur</TableHead>
                            <TableHead>Contact</TableHead>
                            <TableHead className="hidden md:table-cell">Rôle</TableHead>
                            <TableHead>Statut</TableHead>
                            <TableHead className="text-right">Actions</TableHead>
                          </TableRow>
                        </TableHeader>
                        <TableBody>
                          {loading ? (
                            <TableRow>
                              <TableCell colSpan={5} className="text-center text-muted-foreground py-8">
                                Chargement...
                              </TableCell>
                            </TableRow>
                          ) : (
                            getFilteredAccounts().map((account) => (
                              <TableRow key={account.id}>
                                <TableCell>
                                  <div>
                                    <div className="font-medium text-sm md:text-base">{account.name}</div>
                                    <div className="text-xs text-muted-foreground">
                                      {formatDate(account.createdAt)}
                                    </div>
                                  </div>
                                </TableCell>
                                <TableCell>
                                  <div className="space-y-1">
                                    <div className="flex items-center text-xs">
                                      <Phone className="w-3 h-3 mr-1 text-muted-foreground" />
                                      <span className="truncate block max-w-[120px]">{account.phone}</span>
                                    </div>
                                    <div className="flex items-center text-xs hidden sm:flex">
                                      <Mail className="w-3 h-3 mr-1 text-muted-foreground" />
                                      <span className="truncate block max-w-[120px]">{account.email}</span>
                                    </div>
                                  </div>
                                </TableCell>
                                <TableCell className="hidden md:table-cell">
                                  {getRoleBadge(account.role)}
                                </TableCell>
                                <TableCell>
                                  {getStatusBadge(account.status)}
                                </TableCell>
                                <TableCell className="text-right">
                                  {account.status === 'ACTIVE' && (
                                    <div className="flex justify-end gap-2">
                                      <Button
                                        size="sm"
                                        variant="outline"
                                        onClick={() => handleSuspendAccount(account)}
                                        className="text-orange-600 hover:text-orange-700 hover:bg-orange-50"
                                      >
                                        <AlertTriangle className="w-4 h-4" />
                                      </Button>
                                      <Button
                                        size="sm"
                                        onClick={() => handleRevokeAccount(account)}
                                        className="text-red-600 hover:text-red-700 hover:bg-red-50"
                                      >
                                        <Ban className="w-4 h-4" />
                                      </Button>
                                    </div>
                                  )}
                                  {account.status === 'SUSPENDED' && (
                                    <div className="flex justify-end gap-2">
                                      <Button
                                        size="sm"
                                        variant="outline"
                                        onClick={() => handleRestoreAccount(account)}
                                        className="text-green-600 hover:text-green-700 hover:bg-green-50"
                                      >
                                        <CheckCircle className="w-4 h-4" />
                                      </Button>
                                      <Button
                                        size="sm"
                                        onClick={() => handleRevokeAccount(account)}
                                        className="text-red-600 hover:text-red-700 hover:bg-red-50"
                                      >
                                        <Ban className="w-4 h-4" />
                                      </Button>
                                    </div>
                                  )}
                                  {account.status === 'REVOKED' && (
                                    <Button
                                      size="sm"
                                      variant="outline"
                                      onClick={() => handleRestoreAccount(account)}
                                      className="text-green-600 hover:text-green-700 hover:bg-green-50"
                                    >
                                      <CheckCircle className="w-4 h-4" />
                                    </Button>
                                  )}
                                </TableCell>
                              </TableRow>
                            ))
                          )}
                        </TableBody>
                      </Table>
                    )}
                  </div>
                </CardContent>
              </Card>
            </div>
          )}
        </main>

        {/* Confirmation Dialog */}
        <AlertDialog open={actionDialog !== null}>
          <AlertDialogContent>
            <AlertDialogHeader>
              <AlertDialogTitle>
                {actionDialog === 'approve' && 'Confirmer l\'approbation'}
                {actionDialog === 'reject' && 'Rejeter la demande'}
                {actionDialog === 'suspend' && 'Suspendre le compte'}
                {actionDialog === 'revoke' && 'Révoquer le compte'}
                {actionDialog === 'restore' && 'Restaurer le compte'}
              </AlertDialogTitle>
            </AlertDialogHeader>
            <AlertDialogDescription>
              {actionDialog === 'approve' && selectedRequest && `Êtes-vous sûr de vouloir approuver la demande de ${selectedRequest.name} ?`}
              {actionDialog === 'reject' && selectedRequest && `Êtes-vous sûr de vouloir rejeter la demande de ${selectedRequest.name} ?`}
              {actionDialog === 'suspend' && selectedAccount && `Êtes-vous sûr de vouloir suspendre le compte de ${selectedAccount.name} ?`}
              {actionDialog === 'revoke' && selectedAccount && `Êtes-vous sûr de vouloir révoquer le compte de ${selectedAccount.name} ?`}
              {actionDialog === 'restore' && selectedAccount && `Êtes-vous sûr de vouloir restaurer le compte de ${selectedAccount.name} ?`}
            </AlertDialogDescription>
            <AlertDialogFooter>
              <AlertDialogCancel>Annuler</AlertDialogCancel>
              <AlertDialogAction
                onClick={confirmAction}
                className="bg-orange-600 hover:bg-orange-700"
              >
                Confirmer
              </AlertDialogAction>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialog>
      </div>
    </div>
  )
}
