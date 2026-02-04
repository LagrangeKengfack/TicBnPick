'use client'

import { useState, useEffect } from 'react'
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
} from 'lucide-react'

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
  const [loading, setLoading] = useState(true)
  const [selectedRequest, setSelectedRequest] = useState<DeliveryPersonRequest | null>(null)
  const [selectedAccount, setSelectedAccount] = useState<Account | null>(null)
  const [actionDialog, setActionDialog] = useState<'approve' | 'reject' | 'suspend' | 'revoke' | null>(null)
  const [activeView, setActiveView] = useState<ActiveView>('dashboard')
  const [sidebarOpen, setSidebarOpen] = useState(true)
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)

  const [registrationRequests, setRegistrationRequests] = useState<DeliveryPersonRequest[]>([])
  const [accounts, setAccounts] = useState<Account[]>([])

  // Super admin info
  const superAdminName = 'Charles Henry'

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
        return <Badge variant="secondary" className="bg-blue-50 text-blue-700 border-blue-200">Livreur</Badge>
      case 'AGENCY':
        return <Badge variant="secondary" className="bg-purple-50 text-purple-700 border-purple-200">Agence</Badge>
      case 'POINT':
        return <Badge variant="secondary" className="bg-teal-50 text-teal-700 border-teal-200">Point Relais</Badge>
      default:
        return <Badge>{role}</Badge>
    }
  }

  // Composants SVG personnalisés pour les icônes de véhicules - design exactement comme les images
  const VehicleIcon = ({ type }: { type: string }) => {
    const vehicleType = type.toLowerCase()

    if (vehicleType.includes('moto') || vehicleType.includes('motorcycle')) {
      // MOTO - design minimaliste avec guidon, corps arrondi et deux roues
      return (
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" className="w-6 h-6">
          <rect x="8" y="5" width="8" height="7" rx="1" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <circle cx="8" cy="16" r="2.5" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <circle cx="16" cy="16" r="2.5" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <path d="M10 9l2-3h2" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <path d="M12 8v1" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
        </svg>
      )
    } else if (vehicleType.includes('tricycle') || vehicleType.includes('tricy')) {
      // TRICYCLE - corps central arrondi avec trois roues (deux avant, une arrière)
      return (
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" className="w-6 h-6">
          <ellipse cx="12" cy="8" rx="4" ry="3" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <circle cx="8" cy="18" r="2.5" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <circle cx="12" cy="18" r="2.5" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <circle cx="16" cy="18" r="2.5" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <path d="M12 11v-2" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
        </svg>
      )
    } else if (vehicleType.includes('camion') || vehicleType.includes('truck')) {
      // CAMION - corps rectangulaire avec cabine
      return (
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" className="w-6 h-6">
          <path d="M3 10h10v8H3v-8z" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <path d="M13 10h8v8h-8v-8z" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <path d="M13 14v4h8v-4h-8z" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <circle cx="8" cy="20" r="2" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <circle cx="19" cy="20" r="2" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <rect x="5" y="8" width="3" height="2" rx="0.5" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
        </svg>
      )
    } else if (vehicleType.includes('voiture') || vehicleType.includes('car') || vehicleType.includes('auto')) {
      // VOITURE - silhouète arrondie avec deux roues
      return (
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" className="w-6 h-6">
          <rect x="2" y="9" width="14" height="7" rx="3" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <circle cx="6" cy="18" r="2.5" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <circle cx="14" cy="18" r="2.5" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <path d="M5 9l1-2h6l1 2" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <path d="M7 7l1 1h4" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
        </svg>
      )
    } else if (vehicleType.includes('vélo') || vehicleType.includes('velo') || vehicleType.includes('bike') || vehicleType.includes('bicyclette')) {
      // VÉLO - cadre avec deux roues et guidon
      return (
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" className="w-6 h-6">
          <path d="M7 12v4M12 8v8M17 12v4" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <circle cx="7" cy="17" r="2.5" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <circle cx="17" cy="17" r="2.5" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <path d="M7 17l3-7h4" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <path d="M10 10l2-2" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <path d="M14 10h-2" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
        </svg>
      )
    } else {
      // Par défaut, voiture
      return (
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" className="w-6 h-6">
          <rect x="2" y="9" width="14" height="7" rx="3" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <circle cx="6" cy="18" r="2.5" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <circle cx="14" cy="18" r="2.5" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <path d="M5 9l1-2h6l1 2" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          <path d="M7 7l1 1h4" stroke="#f97316" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
        </svg>
      )
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

  const sidebarItems = [
    { id: 'dashboard' as const, icon: LayoutDashboard, label: 'Dashboard' },
    { id: 'registrations' as const, icon: UserCheck, label: 'Inscriptions' },
    { id: 'accounts' as const, icon: ShieldCheck, label: 'Comptes' },
  ]

  return (
    <div className="min-h-screen flex bg-background">
      {/* Sidebar - Desktop only */}
      <aside className="hidden md:flex fixed left-0 top-0 z-40 h-screen bg-card border-r transition-all duration-300 flex-col">
        {/* Logo */}
        <div className="h-16 flex items-center justify-between px-4 border-b">
          <div className="flex items-center gap-2">
            <div className="w-10 h-10 bg-gradient-to-br from-orange-500 to-amber-600 rounded-lg flex items-center justify-center flex-shrink-0">
              <Package className="w-6 h-6 text-white" />
            </div>
            <h1 className="text-lg font-bold">
              TiiB<span className="text-orange-500">n</span>Pick
            </h1>
          </div>
          <Button
            variant="ghost"
            size="icon"
            onClick={() => setSidebarOpen(!sidebarOpen)}
          >
            <X className={cn("w-4 h-4 transition-transform", !sidebarOpen && "rotate-180")} />
          </Button>
        </div>

        {/* Navigation */}
        <nav className="flex-1 p-4 space-y-2">
          {sidebarItems.map((item) => {
            const Icon = item.icon
            const isActive = activeView === item.id
            return (
              <button
                key={item.id}
                onClick={() => setActiveView(item.id)}
                className={cn(
                  'w-full flex items-center gap-3 px-4 py-3 rounded-lg transition-colors',
                  'hover:bg-accent hover:text-accent-foreground',
                  isActive ? 'bg-orange-50 text-orange-700 font-medium' : 'text-muted-foreground'
                )}
              >
                <Icon className="w-5 h-5 flex-shrink-0" />
                {sidebarOpen && <span>{item.label}</span>}
              </button>
            )
          })}
        </nav>

        {/* User Section */}
        <div className="p-4 border-t">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-primary rounded-full flex items-center justify-center text-primary-foreground font-semibold flex-shrink-0">
              {superAdminName.split(' ').map(n => n[0]).join('')}
            </div>
            {sidebarOpen && (
              <div className="flex-1 min-w-0">
                <p className="font-medium truncate">{superAdminName}</p>
                <p className="text-xs text-muted-foreground truncate">admin@tiibnpick.com</p>
              </div>
            )}
            {sidebarOpen && (
              <Button variant="ghost" size="icon" className="flex-shrink-0">
                <LogOut className="w-4 h-4" />
              </Button>
            )}
          </div>
        </div>
      </aside>

      {/* Main Content */}
      <div className="flex-1 flex flex-col min-h-screen md:ml-[256px]">
        {/* Top Bar - Mobile only */}
        <header className="md:hidden h-14 border-b bg-card sticky top-0 z-30 flex items-center justify-between px-4">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 bg-gradient-to-br from-orange-500 to-amber-600 rounded-lg flex items-center justify-center flex-shrink-0">
              <Package className="w-5 h-5 text-white" />
            </div>
            <h1 className="text-base font-bold">
              TiiB<span className="text-orange-500">n</span>Pick
            </h1>
          </div>
          <Button variant="outline" size="sm" onClick={() => { fetchRegistrations(); fetchAccounts() }}>
            <RefreshCw className="w-4 h-4" />
          </Button>
        </header>

        {/* Content */}
        <main className="flex-1 p-4 md:p-6 pb-20 md:pb-6">
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
                <Card className="hover:shadow-md transition-shadow">
                  <CardHeader className="pb-3 px-4 py-3">
                    <CardDescription className="text-[10px] md:text-xs">En attente</CardDescription>
                    <CardTitle className="text-xl md:text-2xl text-orange-600">{stats.pendingRegistrations}</CardTitle>
                  </CardHeader>
                  <CardContent className="px-4 pb-4">
                    <div className="flex items-center text-xs text-muted-foreground">
                      <Clock className="w-3 h-3 mr-1" />
                      Inscriptions
                    </div>
                  </CardContent>
                </Card>

                <Card className="hover:shadow-md transition-shadow">
                  <CardHeader className="pb-3 px-4 py-3">
                    <CardDescription className="text-[10px] md:text-xs">Livreurs</CardDescription>
                    <CardTitle className="text-xl md:text-2xl text-green-600">{stats.activeDeliveryPersons}</CardTitle>
                  </CardHeader>
                  <CardContent className="px-4 pb-4">
                    <div className="flex items-center text-xs text-muted-foreground">
                      <Users className="w-3 h-3 mr-1" />
                      Actifs
                    </div>
                  </CardContent>
                </Card>

                <Card className="hover:shadow-md transition-shadow">
                  <CardHeader className="pb-3 px-4 py-3">
                    <CardDescription className="text-[10px] md:text-xs">Suspendus</CardDescription>
                    <CardTitle className="text-xl md:text-2xl text-orange-600">{stats.suspendedAccounts}</CardTitle>
                  </CardHeader>
                  <CardContent className="px-4 pb-4">
                    <div className="flex items-center text-xs text-muted-foreground">
                      <AlertTriangle className="w-3 h-3 mr-1" />
                      Comptes
                    </div>
                  </CardContent>
                </Card>

                <Card className="hover:shadow-md transition-shadow lg:block">
                  <CardHeader className="pb-3 px-4 py-3">
                    <CardDescription className="text-xs">Révoqués</CardDescription>
                    <CardTitle className="text-2xl text-red-600">{stats.revokedAccounts}</CardTitle>
                  </CardHeader>
                  <CardContent className="px-4 pb-4">
                    <div className="flex items-center text-sm text-muted-foreground">
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
                registrationRequests.map((request) => (
                  <Card key={request.id} className="hover:shadow-md transition-shadow">
                    <CardContent className="p-4">
                      <div className="flex items-center gap-4">
                        {/* Vehicle Icon */}
                        <div className="flex-shrink-0">
                          {getVehicleIcon(request.vehicleType)}
                        </div>

                        {/* Driver Info */}
                        <div className="flex-1 min-w-0">
                          <div className="font-medium text-base md:text-lg">{request.name}</div>
                          <div className="text-xs text-muted-foreground">{request.vehicleBrand} {request.vehicleModel}</div>
                        </div>

                        {/* License Plate */}
                        <div className="flex-shrink-0">
                          <div className="font-bold text-xs text-orange-600">{request.vehicleRegNumber}</div>
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                )))}
              </div>
          )}

          {/* Accounts View */}
          {activeView === 'accounts' && (
            <Card>
              <CardHeader>
                <div className="flex flex-col gap-3">
                  <div>
                    <CardTitle className="text-lg md:text-xl">Gestion des comptes</CardTitle>
                    <CardDescription>Gérez tous les comptes utilisateurs (suspendre, révoquer)</CardDescription>
                  </div>
                  <div className="flex gap-2">
                    <Badge variant="outline" className="text-xs md:text-sm">
                      {accounts.length} comptes
                    </Badge>
                  </div>
                </div>
              </CardHeader>
              <CardContent>
                <div className="rounded-md border max-h-[600px] overflow-auto">
                  <Table>
                    <TableHeader>
                      <TableRow>
                        <TableHead>Utilisateur</TableHead>
                        <TableHead>Contact</TableHead>
                        <TableHead className="hidden md:table-cell">Rôle</TableHead>
                        <TableHead>Dernière activité</TableHead>
                        <TableHead>Statut</TableHead>
                        <TableHead className="text-right">Actions</TableHead>
                      </TableRow>
                    </TableHeader>
                    <TableBody>
                      {loading ? (
                        <TableRow>
                          <TableCell colSpan={6} className="text-center text-muted-foreground py-8">
                            Chargement...
                          </TableCell>
                        </TableRow>
                      ) : accounts.length === 0 ? (
                        <TableRow>
                          <TableCell colSpan={6} className="text-center text-muted-foreground py-8">
                            Aucun compte trouvé
                          </TableCell>
                        </TableRow>
                      ) : (
                        accounts.map((account) => (
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
                            <TableCell className="hidden md:table-cell">
                              {account.lastActivityAt ? (
                                <span className="text-xs text-muted-foreground">{formatDate(account.lastActivityAt)}</span>
                              ) : (
                                <span className="text-xs text-muted-foreground">Jamais</span>
                              )}
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
                                <Button
                                  size="sm"
                                  onClick={() => handleRevokeAccount(account)}
                                  className="text-red-600 hover:text-red-700 hover:bg-red-50"
                                >
                                  <Ban className="w-4 h-4" />
                                </Button>
                              )}
                            </TableCell>
                          </TableRow>
                        )))}
                      </TableBody>
                    </Table>
                </div>
              </CardContent>
            </Card>
          )}
        </main>

        {/* Bottom Navigation - Mobile only */}
        <nav className="md:hidden fixed bottom-0 left-0 right-0 z-50 bg-card border-t border-gray-200 safe-area-bottom">
          <div className="flex justify-between px-6 py-3">
            <button
              onClick={() => setActiveView('dashboard')}
              className={cn(
                'flex flex-col items-center gap-1 transition-colors flex-1',
                activeView === 'dashboard' ? 'text-orange-600' : 'text-muted-foreground'
              )}
            >
              <LayoutDashboard className="w-5 h-5" />
              <span className="text-xs">Dashboard</span>
            </button>
            <button
              onClick={() => setActiveView('registrations')}
              className={cn(
                'flex flex-col items-center gap-1 transition-colors flex-1',
                activeView === 'registrations' ? 'text-orange-600' : 'text-muted-foreground'
              )}
            >
              <UserCheck className="w-5 h-5" />
              <span className="text-xs">Inscriptions</span>
            </button>
            <button
              onClick={() => setActiveView('accounts')}
              className={cn(
                'flex flex-col items-center gap-1 transition-colors flex-1',
                activeView === 'accounts' ? 'text-orange-600' : 'text-muted-foreground'
              )}
            >
              <ShieldCheck className="w-5 h-5" />
              <span className="text-xs">Comptes</span>
            </button>
          </div>
        </nav>

        {/* Confirmation Dialog */}
        <AlertDialog open={actionDialog !== null}>
          <AlertDialogContent>
            <AlertDialogHeader>
              <AlertDialogTitle>
                {actionDialog === 'approve' && 'Confirmer l\'approbation'}
                {actionDialog === 'reject' && 'Rejeter la demande'}
                {actionDialog === 'suspend' && 'Suspendre le compte'}
                {actionDialog === 'revoke' && 'Révoquer le compte'}
              </AlertDialogTitle>
            </AlertDialogHeader>
            <AlertDialogDescription>
              {actionDialog === 'approve' && selectedRequest && `Êtes-vous sûr de vouloir approuver la demande de ${selectedRequest.name} ?`}
              {actionDialog === 'reject' && selectedRequest && `Êtes-vous sûr de vouloir rejeter la demande de ${selectedRequest.name} ?`}
              {actionDialog === 'suspend' && selectedAccount && `Êtes-vous sûr de vouloir suspendre le compte de ${selectedAccount.name} ?`}
              {actionDialog === 'revoke' && selectedAccount && `Êtes-vous sûr de vouloir révoquer le compte de ${selectedAccount.name} ?`}
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
