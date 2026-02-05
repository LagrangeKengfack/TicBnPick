/**
 * @file types/package.ts
 * @description Centralized TypeScript definitions.
 */

import { ElementType } from 'react';

// --- CORE DATA MODELS ---
export interface SenderData {
  senderName: string;
  senderPhone: string;
  senderEmail: string;
  senderCountry: string;
  senderRegion: string;
  senderCity: string;
  senderAddress: string;
  senderLieuDit: string;
}

export interface RecipientData {
  recipientName: string;
  recipientPhone: string;
  recipientEmail: string;
  recipientCountry: string;
  recipientRegion: string;
  recipientCity: string;
  recipientAddress: string;
  recipientLieuDit: string;
}

export interface PackageData {
  photo: string | null; 
  designation: string;
  description: string;
  weight: string;
  length: string;
  width: string;
  height: string;
  isFragile: boolean;
  isPerishable: boolean;
  isLiquid: boolean;
  isInsured: boolean;
  declaredValue: string;
  transportMethod: 'truck' | 'tricycle' | 'moto' | 'bike' | 'car' | '';
  logistics: 'standard' | 'express_48h' | 'express_24h';
  pickup: boolean;
  delivery: boolean;
  // FOR THE IMPROVEMENTS
  hasPackageNow: boolean; // Did they click "Yes"?
  exactPickupAddress?: string; // The "Road name/Position"
  coordinates?: {
    lat: number;
    lng: number;
  } | null;
}


export interface RouteData {
  departurePointId: string | null;
  arrivalPointId: string | null;
  departurePointName: string;
  arrivalPointName: string;
  distanceKm: number;
}

export interface SignatureData {
  signatureUrl: string | null;
}

// --- APP STATE ---
export interface ExpeditionFormData {
  currentStep: number;
  senderData: SenderData;
  recipientData: RecipientData;
  packageData: PackageData;
  routeData: RouteData;
  signatureData: SignatureData;
  pricing: {
    basePrice: number;
    travelPrice: number;
    operatorFee: number;
    totalPrice: number;
  };
}

// --- ALL EXPEDITION DATA ---
export type AllExpeditionData = SenderData &
 RecipientData &
 PackageData &
 RouteData &
 SignatureData &
 {
  basePrice: number; 
    travelPrice: number; 
 };

// --- COMPONENT PROPS ---
export interface SenderInfoStepProps {
  initialData: SenderData;
  onContinue: (data: SenderData) => void;
  currentUser?: any;
}

export interface RecipientInfoStepProps {
  initialData: RecipientData;
  onContinue: (data: RecipientData) => void;
  onBack: () => void;
}

export interface PackageRegistrationProps {
  initialData?: Partial<PackageData>;
  onContinue: (data: PackageData, totalPrice: number) => void;
  onBack?: () => void;
}

export interface RouteSelectionStepProps {
  onContinue: (data: RouteData, travelPrice: number) => void;
  onBack: () => void;
  initialDepartureAddress?: string;
  initialArrivalAddress?: string;
}


export interface PaymentStepProps {
  allData: AllExpeditionData;
  onBack: () => void;
  onPaymentFinalized: (pricing: {
    basePrice: number;
    travelPrice: number;
    operatorFee: number;
    totalPrice: number;
    trackingNumber?: string;
  }) => void;
  currentUser: LoggedInUser | null; // if there is a problem, look here
}

export interface PaymentOptionProps {
  id: 'cash' | 'mobile' | 'recipient';
  label: string;
  description: string;
  icon: ElementType;
  fee: number;
  selected: 'cash' | 'mobile' | 'recipient';
  setSelected: (id: 'cash' | 'mobile' | 'recipient') => void;
  badge?: string;
}

export interface SignatureStepProps {
  onBack: () => void;
  onSubmit: (signatureData: string) => void;
}


//for the login, when we will integrate
export interface LoggedInUser {
  id: string;
  full_name: string | null;
  phone: string | null;
  email?: string;
  address?: string | null;
  lieu_dit?: string | null;
}