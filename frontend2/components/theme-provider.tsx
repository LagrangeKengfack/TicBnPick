'use client'

import * as React from 'react'
import {
  ThemeProvider as NextThemesProvider,
  type ThemeProviderProps,
} from 'next-themes'
import { useEffect } from 'react'
import { useTheme } from 'next-themes'

export function ThemeProvider({ children, ...props }: ThemeProviderProps) {
  const defaults: Partial<ThemeProviderProps> = {
    attribute: 'class',
    enableSystem: false,
    defaultTheme: 'light',
    disableTransitionOnChange: true,
  };

  // Sync resolved theme to a cookie so server can read it on next render
  function CookieSync() {
    const { resolvedTheme } = useTheme();

    useEffect(() => {
      try {
        if (typeof document !== 'undefined' && resolvedTheme) {
          // expire in 1 year
          document.cookie = `theme=${resolvedTheme}; Path=/; Max-Age=${60 * 60 * 24 * 365};`;
        }
      } catch (e) {
        // ignore
      }
    }, [resolvedTheme]);

    return null;
  }

  return (
    <NextThemesProvider {...defaults} {...props}>
      <CookieSync />
      {children}
    </NextThemesProvider>
  )
}
