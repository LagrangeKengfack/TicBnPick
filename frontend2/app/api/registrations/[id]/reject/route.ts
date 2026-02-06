import { NextResponse } from 'next/server'

export async function POST() {
  try {
    // Mock rejection - in a real app, this would update the database
    return NextResponse.json({ success: true, message: 'Registration rejected' })
  } catch (error) {
    console.error('Error rejecting registration:', error)
    return NextResponse.json(
      { error: 'Failed to reject registration' },
      { status: 500 }
    )
  }
}
