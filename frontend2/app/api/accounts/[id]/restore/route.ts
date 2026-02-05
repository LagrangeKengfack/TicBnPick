import { NextRequest, NextResponse } from 'next/server'

export async function POST(
  request: NextRequest,
  { params }: { params: { id: string } }
) {
  const { id } = params

  // Mock API - simulate restoring an account
  return NextResponse.json({
    success: true,
    message: `Account ${id} restored successfully`,
    status: 'ACTIVE',
  })
}
