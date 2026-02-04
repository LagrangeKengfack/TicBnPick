"use client";

import { useEffect } from 'react';
import { useTheme } from 'next-themes';

export default function ThemeCookieSync() {
  const { theme } = useTheme();

  useEffect(() => {
    if (!theme) return;
    // set cookie for 1 year
    const expires = new Date(Date.now() + 365 * 24 * 60 * 60 * 1000).toUTCString();
    document.cookie = `theme=${theme}; path=/; expires=${expires}; SameSite=Lax`;
  }, [theme]);

  return null;
}
