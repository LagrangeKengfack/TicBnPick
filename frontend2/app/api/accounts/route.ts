import { NextResponse } from 'next/server'

// Mock data for accounts
const mockAccounts = [
  {
    id: 'acc1',
    name: 'Jean-Pierre Kouassi',
    email: 'jean.kouassi@email.com',
    phone: '+225 07 01 23 45 67',
    role: 'DELIVERY',
    status: 'ACTIVE',
    deliveriesCount: 24,
    lastActivityAt: new Date('2026-02-05T14:30:00'),
    createdAt: new Date('2026-01-15'),
    updatedAt: new Date('2026-02-05'),
  },
  {
    id: 'acc2',
    name: 'Marie Dupont',
    email: 'marie.dupont@email.com',
    phone: '+33 6 12 34 56 78',
    role: 'CLIENT',
    status: 'ACTIVE',
    deliveriesCount: 5,
    lastActivityAt: new Date('2026-02-04T10:15:00'),
    createdAt: new Date('2025-12-01'),
    updatedAt: new Date('2026-02-04'),
  },
  {
    id: 'acc3',
    name: 'Ahmed Hassan',
    email: 'ahmed.hassan@email.com',
    phone: '+216 71 123 456',
    role: 'CLIENT',
    status: 'ACTIVE',
    deliveriesCount: 12,
    lastActivityAt: new Date('2026-02-05T09:45:00'),
    createdAt: new Date('2025-11-20'),
    updatedAt: new Date('2026-02-05'),
  },
  {
    id: 'acc4',
    name: 'Fatoumata Sow',
    email: 'fatou.sow@email.com',
    phone: '+223 60 12 34 56',
    role: 'DELIVERY',
    status: 'SUSPENDED',
    deliveriesCount: 12,
    lastActivityAt: new Date('2026-01-28T16:20:00'),
    createdAt: new Date('2026-01-10'),
    updatedAt: new Date('2026-02-01'),
  },
  {
    id: 'acc5',
    name: 'Koffi Mensah',
    email: 'koffi.mensah@email.com',
    phone: '+233 20 123 4567',
    role: 'DELIVERY',
    status: 'ACTIVE',
    deliveriesCount: 45,
    lastActivityAt: new Date('2026-02-03T11:00:00'),
    createdAt: new Date('2025-12-15'),
    updatedAt: new Date('2026-02-03'),
  },
  {
    id: 'acc6',
    name: 'Aminata Diallo',
    email: 'amina.diallo@email.com',
    phone: '+224 62 12 34 56',
    role: 'DELIVERY',
    status: 'REVOKED',
    deliveriesCount: 8,
    lastActivityAt: new Date('2026-01-20T13:30:00'),
    createdAt: new Date('2026-01-05'),
    updatedAt: new Date('2026-01-25'),
  },
  {
    id: 'acc7',
    name: 'Sophie Martin',
    email: 'sophie.martin@email.com',
    phone: '+33 7 98 76 54 32',
    role: 'CLIENT',
    status: 'ACTIVE',
    deliveriesCount: 3,
    lastActivityAt: new Date('2026-02-02T15:20:00'),
    createdAt: new Date('2026-01-20'),
    updatedAt: new Date('2026-02-02'),
  },
  {
    id: 'acc8',
    name: 'Marco Rossi',
    email: 'marco.rossi@email.com',
    phone: '+39 06 12 34 56 78',
    role: 'DELIVERY',
    status: 'ACTIVE',
    deliveriesCount: 67,
    lastActivityAt: new Date('2026-02-05T08:00:00'),
    createdAt: new Date('2025-10-10'),
    updatedAt: new Date('2026-02-05'),
  },
]

export async function GET() {
  try {
    return NextResponse.json(mockAccounts)
  } catch (error) {
    console.error('Error fetching accounts:', error)
    return NextResponse.json(
      { error: 'Failed to fetch accounts' },
      { status: 500 }
    )
  }
}
