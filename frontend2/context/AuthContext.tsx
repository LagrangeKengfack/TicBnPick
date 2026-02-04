"use client";

import React, { createContext, useContext, useState, ReactNode } from "react";

type User = {
  id: string;
  email?: string;
  name?: string;
} | null;

type AuthContextValue = {
  user: User;
  isAuthenticated: boolean;
  login: (u: User) => void;
  logout: () => void;
};

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User>(null);

  const login = (u: User) => setUser(u);
  const logout = () => setUser(null);

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: !!user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    // Retourne un fallback pour éviter les erreurs côté client si le provider n'est pas utilisé
    return { user: null, isAuthenticated: false, login: () => {}, logout: () => {} } as AuthContextValue;
  }
  return ctx;
};

export default AuthContext;
