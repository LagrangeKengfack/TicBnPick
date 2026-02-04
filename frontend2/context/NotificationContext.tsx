"use client";

import React, { createContext, useContext, ReactNode } from "react";

type NotificationOptions = {
  title?: string;
  message?: string;
  type?: "info" | "success" | "error";
};

type NotificationContextValue = {
  showNotification: (opts: NotificationOptions) => void;
};

const NotificationContext = createContext<NotificationContextValue | undefined>(undefined);

export const NotificationProvider = ({ children }: { children: ReactNode }) => {
  const showNotification = (opts: NotificationOptions) => {
    // minimal stub: use console for now
    console.log("Notification:", opts);
  };

  return (
    <NotificationContext.Provider value={{ showNotification }}>
      {children}
    </NotificationContext.Provider>
  );
};

export const useNotification = () => {
  const ctx = useContext(NotificationContext);
  if (!ctx) return { showNotification: (opts: NotificationOptions) => console.log("Notification:", opts) };
  return ctx;
};

export default NotificationContext;
