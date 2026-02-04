import type { Metadata } from 'next'
import { Geist, Geist_Mono } from 'next/font/google'
import { Analytics } from '@vercel/analytics/next'
import './globals.css'
import { ThemeProvider } from '@/components/theme-provider'
import { AuthProvider } from '@/context/AuthContext'
import { NotificationProvider } from '@/context/NotificationContext'
import { cookies } from 'next/headers'
import ThemeCookieSync from '@/components/theme-cookie-sync'

const _geist = Geist({ subsets: ["latin"] });
const _geistMono = Geist_Mono({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: 'v0 App',
  description: 'Created with v0',
  generator: 'v0.app',
  icons: {
    icon: [
      {
        url: '/icon-light-32x32.png',
        media: '(prefers-color-scheme: light)',
      },
      {
        url: '/icon-dark-32x32.png',
        media: '(prefers-color-scheme: dark)',
      },
      {
        url: '/icon.svg',
        type: 'image/svg+xml',
      },
    ],
    apple: '/apple-icon.png',
  },
}

export default async function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  // Read theme cookie on the server to keep SSR class consistent
  // Support both API shapes: cookies may be a function (cookies()) or an object (cookies)
  const cookieStore: any = typeof cookies === 'function' ? await cookies() : cookies;
  let themeCookie = 'light';
  try {
    const getter = cookieStore?.get; // could be function
    const raw = typeof getter === 'function' ? getter.call(cookieStore, 'theme') : cookieStore?.get?.('theme');
    if (raw) {
      // raw can be string or { value }
      themeCookie = typeof raw === 'string' ? raw : (raw?.value ?? 'light');
    }
  } catch (e) {
    // fallback
    themeCookie = 'light';
  }

  return (
    <html lang="en" className={themeCookie === 'dark' ? 'dark' : 'light'} style={{ colorScheme: themeCookie }}>
      <body className={`font-sans antialiased`}>
        <ThemeProvider attribute="class">
          <AuthProvider>
            <NotificationProvider>
              {children}
              {/* Client component that keeps the cookie in sync when theme changes */}
              <ThemeCookieSync />
            </NotificationProvider>
          </AuthProvider>
        </ThemeProvider>
        <Analytics />
      </body>
    </html>
  )
}
