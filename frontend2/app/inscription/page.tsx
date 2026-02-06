'use client'

import React, { useEffect, useState } from "react"
import { useSearchParams } from 'next/navigation'
import { Card, CardContent } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Button } from '@/components/ui/button'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { User, UserCircle, Phone, Mail, Lock, MapPin, Upload, Camera, ChevronDown, ImageIcon, Car, Check, Eye, EyeOff } from 'lucide-react'
import Link from 'next/link'
import { useRouter } from 'next/navigation'
import { checkEmail, createClient, checkNationalId } from '../../services/clientService'

type Step = 1 | 2 | 3 | 4
type Role = 'client' | 'livreur' | null

export default function LoginPage() {

  const router = useRouter()
  const [step, setStep] = useState<Step>(1)
  const [role, setRole] = useState<Role>(null)
  const [completedSteps, setCompletedSteps] = useState<number[]>([])
  const [photoIdentite, setPhotoIdentite] = useState<File | null>(null)
  const [photoCniRecto, setPhotoCniRecto] = useState<File | null>(null)
  const [photoCniVerso, setPhotoCniVerso] = useState<File | null>(null)
  const [photoVehiculeAvant, setPhotoVehiculeAvant] = useState<File | null>(null)
  const [photoVehiculeArriere, setPhotoVehiculeArriere] = useState<File | null>(null)
  const [emailError, setEmailError] = useState<string | null>(null);
  const [cniError, setCniError] = useState<string | null>(null);
  const [fieldErrors, setFieldErrors] = useState<{ [key: string]: string }>({});
  const [isTermsAccepted, setIsTermsAccepted] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const [formData, setFormData] = useState({
    nom: '',
    prenom: '',
    telephone: '',
    email: '',
    motDePasse: '',
    confirmerMotDePasse: '',
    pays: '',
    adressePersonnelle: '',
    lieuDitAdresse: '',
    numeroCNI: '',
    numeroNINE: '',
    typeVehicule: '',
    marqueVehicule: '',
    numeroImmatriculation: '',
    couleurVoiture: '',
    dimensionsMalle: ''
  })

  const searchParams = useSearchParams()

  useEffect(() => {
    try {
      const r = searchParams.get('role')
      const s = searchParams.get('step')
      if (r === 'livreur' || r === 'client') {
        setRole(r as Role)
        setCompletedSteps([1])
        if (s) {
          const n = parseInt(s, 10)
          if (!isNaN(n) && n >= 1 && n <= 4) setStep(n as Step)
          else setStep(2)
        } else {
          setStep(2)
        }
      }
    } catch (e) {
      // ignore
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [searchParams])

  const selectClient = () => {
    setRole('client')
    setCompletedSteps([1])
    setStep(2)
  }

  const selectLivreur = () => {
    setRole('livreur')
    setCompletedSteps([1])
    setStep(2)
  }

  const goBack = () => {
    if (step === 2) {
      setStep(1)
      setRole(null)
    } else {
      setStep((step - 1) as Step)
    }
  }

  const validateStep2 = () => {
    const errors: { [key: string]: string } = {};
    let isValid = true;

    if (!formData.nom.trim()) {
      errors.nom = "Le nom est requis";
      isValid = false;
    }
    if (!formData.prenom.trim()) {
      errors.prenom = "Le prénom est requis";
      isValid = false;
    }
    if (!formData.telephone.trim()) {
      errors.telephone = "Le téléphone est requis";
      isValid = false;
    } else if (!/^6\d{8}$/.test(formData.telephone)) {
      errors.telephone = "Le téléphone doit commencer par 6 et contenir 9 chiffres";
      isValid = false;
    }
    if (!formData.email.trim()) {
      errors.email = "L'email est requis";
      isValid = false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      errors.email = "Format d'email invalide";
      isValid = false;
    }
    if (!formData.motDePasse) {
      errors.motDePasse = "Le mot de passe est requis";
      isValid = false;
    }
    if (!formData.confirmerMotDePasse) {
      errors.confirmerMotDePasse = "La confirmation est requise";
      isValid = false;
    } else if (formData.motDePasse !== formData.confirmerMotDePasse) {
      errors.confirmerMotDePasse = "Les mots de passe ne correspondent pas";
      isValid = false;
    }

    if (!formData.numeroCNI.trim()) {
      errors.numeroCNI = "Le numéro de CNI est requis";
      isValid = false;
    }


    setFieldErrors(errors);
    return isValid;
  }

  const goNext = async () => {
    if (step === 2 && role === 'client') {
      if (!validateStep2()) {
        return;
      }

      // Validate email before proceeding
      if (formData.email) {
        try {
          const exists = await checkEmail(formData.email);
          if (exists) {
            setEmailError("Cet email est déjà utilisé.");
            return; // Stop here
          }
          setEmailError(null); // Clear error if valid
        } catch (err) {
          console.error("Error checking email", err);
          setEmailError("Erreur lors de la vérification de l'email.");
          return;
        }
      }

      // Validate CNI before proceeding
      if (formData.numeroCNI) {
        try {
          const exists = await checkNationalId(formData.numeroCNI);
          if (exists) {
            setCniError("Ce numéro de CNI est déjà utilisé.");
            return; // Stop here
          }
          setCniError(null);
        } catch (err) {
          console.error("Error checking CNI", err);
          setCniError("Erreur lors de la vérification du CNI.");
          return;
        }
      }
    }

    if (role === 'livreur' && step < 4) {
      setCompletedSteps([...completedSteps, step])
      setStep((step + 1) as Step)
    } else if (role === 'client' && step < 3) {
      setCompletedSteps([...completedSteps, step])
      setStep((step + 1) as Step)
    } else {
      console.log('Submit:', formData, { photoIdentite, photoCniRecto, photoCniVerso, photoVehiculeAvant, photoVehiculeArriere })
    }
  }

  const updateField = (field: string, value: string) => {
    if (field === 'telephone') {
      let digitsOnly = value.replace(/\D/g, '').slice(0, 9)
      // Force start with 6 if user types anything else at start
      if (digitsOnly.length > 0 && !digitsOnly.startsWith('6')) {
        // If user typed '6' it's fine. If they typed '1', replace with '6' or block?
        // User said "forcé cela sur l'interface". Let's handle it by auto-prefixing or validation.
        // Simpler to just enforce strictly in validation and maybe prevent non-6 start if possible, 
        // but simple replace is often jarring. 
        // Let's just allow digits but validation handles it.
        // Actually, let's try to be smart.
      }
      setFormData({ ...formData, [field]: digitsOnly })
    } else if (field === 'numeroCNI') {
      // No length limit, just string
      setFormData({ ...formData, [field]: value })
    } else if (field === 'numeroNINE') {
      const valueUpper = value.toUpperCase().replace(/[^A-Z0-9]/g, '').slice(0, 12)
      setFormData({ ...formData, [field]: valueUpper })
    } else if (field === 'numeroImmatriculation') {
      const valueUpper = value.toUpperCase().replace(/[^A-Z0-9]/g, '').slice(0, 9)
      setFormData({ ...formData, [field]: valueUpper })
    } else {
      setFormData({ ...formData, [field]: value })
    }
  }

  const handlePhotoIdentiteChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file) setPhotoIdentite(file)
  }

  const handlePhotoCniRectoChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file) setPhotoCniRecto(file)
  }

  const handlePhotoCniVersoChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file) setPhotoCniVerso(file)
  }

  const handlePhotoVehiculeAvantChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file) setPhotoVehiculeAvant(file)
  }

  const handlePhotoVehiculeArriereChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file) setPhotoVehiculeArriere(file)
  }

  // Step 1: Selection
  if (step === 1) {
    return (
      <div className="min-h-screen flex flex-col bg-[#f5f0e6]">
        <main className="flex-1 flex flex-col items-center justify-center px-4 py-6 md:py-8">
          <div className="w-full max-w-md space-y-4 md:space-y-6">

            <div className="flex items-center justify-center">
              <div className="w-10 h-10 rounded-full bg-orange-500 flex items-center justify-center">
                <span className="text-white font-bold text-lg">1</span>
              </div>
            </div>

            <h2 className="text-base md:text-lg font-medium text-gray-800 text-center leading-relaxed">
              Quel type de compte souhaitez-vous créer?
            </h2>

            <div className="space-y-3 md:space-y-4 pt-2">
              <Card
                className="cursor-pointer border-2 border-gray-200 hover:border-orange-400 transition-all active:scale-95"
                onClick={selectClient}
              >
                <CardContent className="p-4 md:p-6 flex flex-col items-center text-center gap-3 md:gap-4">
                  <div className="w-14 h-14 md:w-16 md:h-16 rounded-full bg-white border-2 border-orange-500 flex items-center justify-center">
                    <svg className="w-7 h-7 md:w-8 md:h-8 text-orange-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                    </svg>
                  </div>
                  <div className="flex flex-col items-center gap-1 md:gap-2">
                    <h3 className="font-semibold text-gray-900 text-lg md:text-xl">Client</h3>
                    <p className="text-xs md:text-sm text-gray-500">Je souhaite envoyer des colis</p>
                  </div>
                </CardContent>
              </Card>

              <Card
                className="cursor-pointer border-2 border-gray-200 hover:border-orange-400 transition-all active:scale-95"
                onClick={selectLivreur}
              >
                <CardContent className="p-4 md:p-6 flex flex-col items-center text-center gap-3 md:gap-4">
                  <div className="w-14 h-14 md:w-16 md:h-16 rounded-full bg-white border-2 border-orange-500 flex items-center justify-center">
                    <svg className="w-7 h-7 md:w-8 md:h-8 text-orange-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z" />
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16V6a1 1 0 00-1-1H4a1 1 0 00-1 1v10a1 1 0 001 1h1m8-1a1 1 0 01-1 1H9m4-1V8a1 1 0 011-1h2.586a1 1 0 01.707.293l3.414 3.414a1 1 0 01.293.707V16a1 1 0 01-1 1h-1m-6-1a1 1 0 001 1h1M5 17a2 2 0 104 0m-4 0a2 2 0 114 0m6 0a2 2 0 104 0m-4 0a2 2 0 114 0" />
                    </svg>
                  </div>
                  <div className="flex flex-col items-center gap-1 md:gap-2">
                    <h3 className="font-semibold text-gray-900 text-lg md:text-xl">Livreur</h3>
                    <p className="text-xs md:text-sm text-gray-500">Je souhaite livrer des colis</p>
                  </div>
                </CardContent>
              </Card>
            </div>
          </div>
        </main>

        <footer className="p-3 md:p-6 border-t border-gray-200">
          <div className="max-w-md mx-auto text-center">
            <p className="text-xs md:text-sm text-gray-600">
              Vous avez déjà un compte?{' '}
              <Link href="/" className="text-orange-500 hover:text-orange-600 font-semibold">
                Se connecter
              </Link>
            </p>
          </div>
        </footer>
      </div>
    )
  }

  // Step 2+: Form
  const totalSteps = role === 'client' ? 3 : 4

  return (
    <div className="min-h-screen flex flex-col bg-[#f5f0e6]">
      <main className="flex-1 flex flex-col items-start justify-start px-3 md:px-4 py-4 md:py-6 overflow-y-auto">
        <div className="w-full max-w-md mx-auto space-y-3 md:space-y-6">

          {/* Step Indicators */}
          <div className="flex flex-col items-center gap-2 md:gap-4 sticky top-0 bg-[#f5f0e6] py-2 z-10">
            <div className="flex items-center justify-center gap-0">
              {Array.from({ length: totalSteps }).map((_, i) => (
                <div key={i + 1} className="flex items-center">
                  <div className={`w-8 h-8 md:w-10 md:h-10 rounded-full flex items-center justify-center border-2 border-orange-500 ${completedSteps.includes(i + 1) ? 'bg-orange-500' : step === i + 1 ? 'bg-orange-500' : 'bg-white'
                    }`}>
                    {completedSteps.includes(i + 1) ? (
                      <Check className="w-4 h-4 md:w-5 md:h-5 text-white" />
                    ) : step === i + 1 ? (
                      <span className="font-bold text-sm md:text-lg text-white">{i + 1}</span>
                    ) : (
                      <span className="font-bold text-sm md:text-lg text-orange-500">{i + 1}</span>
                    )}
                  </div>
                  {i < totalSteps - 1 && <div className={`w-6 md:w-8 h-1 ${completedSteps.includes(i + 1) ? 'bg-orange-500' : 'bg-white'}`}></div>}
                </div>
              ))}
            </div>

            {step === 2 && (
              <h2 className="text-base md:text-lg font-medium text-gray-800 text-center leading-relaxed">
                Veuillez remplir vos informations
              </h2>
            )}

            {step === 3 && role === 'client' && (
              <h2 className="text-base md:text-lg font-medium text-gray-800 text-center leading-relaxed">
                Finalisation
              </h2>
            )}

            {step === 3 && role === 'livreur' && (
              <h2 className="text-base md:text-lg font-medium text-gray-800 text-center leading-relaxed">
                Coordonnées
              </h2>
            )}

            {step === 4 && role === 'livreur' && (
              <h2 className="text-base md:text-lg font-medium text-gray-800 text-center leading-relaxed">
                Informations sur le véhicule
              </h2>
            )}
          </div>

          {/* Step 2: Informations personnelles */}
          {step === 2 && (
            <Card className="border-2 border-gray-200">
              <CardContent className="p-4 md:p-6 space-y-3 md:space-y-4">
                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Nom</Label>
                  <div className="relative">
                    <User className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 md:h-5 md:w-5 text-orange-500" />
                    <Input
                      type="text"
                      placeholder="Votre nom"
                      value={formData.nom}
                      onChange={(e) => updateField('nom', e.target.value)}
                      className={`pl-9 md:pl-10 border-gray-300 focus:border-orange-500 text-sm md:text-base ${role === 'client' && fieldErrors.nom ? 'border-red-500' : ''}`}
                    />
                  </div>
                  {role === 'client' && fieldErrors.nom && <p className="text-red-500 text-xs mt-1">{fieldErrors.nom}</p>}
                </div>

                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Prénom</Label>
                  <div className="relative">
                    <UserCircle className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 md:h-5 md:w-5 text-orange-500" />
                    <Input
                      type="text"
                      placeholder="Votre prénom"
                      value={formData.prenom}
                      onChange={(e) => updateField('prenom', e.target.value)}
                      className={`pl-9 md:pl-10 border-gray-300 focus:border-orange-500 text-sm md:text-base ${role === 'client' && fieldErrors.prenom ? 'border-red-500' : ''}`}
                    />
                  </div>
                  {role === 'client' && fieldErrors.prenom && <p className="text-red-500 text-xs mt-1">{fieldErrors.prenom}</p>}
                </div>

                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Numéro de téléphone</Label>
                  <div className="relative">
                    <Phone className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 md:h-5 md:w-5 text-orange-500" />
                    <Input
                      type="tel"
                      placeholder="XXXXXXXXX"
                      value={formData.telephone}
                      onChange={(e) => updateField('telephone', e.target.value)}
                      maxLength={9}
                      className={`pl-9 md:pl-10 border-gray-300 focus:border-orange-500 text-sm md:text-base ${role === 'client' && fieldErrors.telephone ? 'border-red-500' : ''}`}
                    />
                  </div>
                  {role === 'client' && fieldErrors.telephone && <p className="text-red-500 text-xs mt-1">{fieldErrors.telephone}</p>}
                  {!(role === 'client' && fieldErrors.telephone) && <p className="text-xs text-gray-500">9 chiffres requis</p>}
                </div>

                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Adresse email</Label>
                  <div className="relative">
                    <Mail className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 md:h-5 md:w-5 text-orange-500" />
                    <Input
                      type="email"
                      placeholder="votre@email.com"
                      value={formData.email}
                      onChange={(e) => updateField('email', e.target.value)}
                      className={`pl-9 md:pl-10 border-gray-300 focus:border-orange-500 text-sm md:text-base ${role === 'client' && (fieldErrors.email || emailError) ? 'border-red-500' : ''}`}
                    />
                  </div>
                  {role === 'client' && fieldErrors.email && <p className="text-red-500 text-xs mt-1">{fieldErrors.email}</p>}
                  {role === 'client' && emailError && <p className="text-red-500 text-xs mt-1">{emailError}</p>}
                </div>

                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Numéro CNI</Label>
                  <div className="relative">
                    <Camera className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 md:h-5 md:w-5 text-orange-500" />
                    <Input
                      type="text"
                      placeholder="Identifiant National"
                      value={formData.numeroCNI}
                      onChange={(e) => updateField('numeroCNI', e.target.value)}
                      className={`pl-9 md:pl-10 border-gray-300 focus:border-orange-500 text-sm md:text-base ${role === 'client' && (fieldErrors.numeroCNI || cniError) ? 'border-red-500' : ''}`}
                    />
                  </div>
                  {role === 'client' && fieldErrors.numeroCNI && <p className="text-red-500 text-xs mt-1">{fieldErrors.numeroCNI}</p>}
                  {role === 'client' && cniError && <p className="text-red-500 text-xs mt-1">{cniError}</p>}
                </div>

                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Mot de passe</Label>
                  <div className="relative">
                    <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 md:h-5 md:w-5 text-orange-500" />
                    <Input
                      type={showPassword ? "text" : "password"}
                      placeholder="Votre mot de passe"
                      value={formData.motDePasse}
                      onChange={(e) => updateField('motDePasse', e.target.value)}
                      className={`pl-9 md:pl-10 pr-10 border-gray-300 focus:border-orange-500 text-sm md:text-base ${role === 'client' && fieldErrors.motDePasse ? 'border-red-500' : ''}`}
                    />
                    <button
                      type="button"
                      onClick={() => setShowPassword(!showPassword)}
                      className="absolute right-3 top-1/2 -translate-y-1/2 text-orange-500 hover:text-orange-600 transition-colors"
                      aria-label={showPassword ? "Masquer le mot de passe" : "Afficher le mot de passe"}
                    >
                      {showPassword ? <Eye size={20} /> : <EyeOff size={20} />}
                    </button>
                  </div>
                  {role === 'client' && fieldErrors.motDePasse && <p className="text-red-500 text-xs mt-1">{fieldErrors.motDePasse}</p>}
                </div>

                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Confirmer mot de passe</Label>
                  <div className="relative">
                    <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 md:h-5 md:w-5 text-orange-500" />
                    <Input
                      type={showConfirmPassword ? "text" : "password"}
                      placeholder="Confirmez votre mot de passe"
                      value={formData.confirmerMotDePasse}
                      onChange={(e) => updateField('confirmerMotDePasse', e.target.value)}
                      className={`pl-9 md:pl-10 pr-10 border-gray-300 focus:border-orange-500 text-sm md:text-base ${role === 'client' && fieldErrors.confirmerMotDePasse ? 'border-red-500' : ''}`}
                    />
                    <button
                      type="button"
                      onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                      className="absolute right-3 top-1/2 -translate-y-1/2 text-orange-500 hover:text-orange-600 transition-colors"
                      aria-label={showConfirmPassword ? "Masquer le mot de passe" : "Afficher le mot de passe"}
                    >
                      {showConfirmPassword ? <Eye size={20} /> : <EyeOff size={20} />}
                    </button>
                  </div>
                  {role === 'client' && fieldErrors.confirmerMotDePasse && <p className="text-red-500 text-xs mt-1">{fieldErrors.confirmerMotDePasse}</p>}
                </div>

                <div className="flex gap-2 md:gap-3 pt-3 md:pt-4">
                  <Button
                    variant="outline"
                    className="flex-1 border-orange-500 text-orange-500 hover:bg-orange-50 h-12 md:h-auto text-sm md:text-base bg-transparent"
                    onClick={goBack}
                  >
                    Précédent
                  </Button>
                  <Button
                    className="flex-1 bg-orange-500 hover:bg-orange-600 h-12 md:h-auto text-sm md:text-base"
                    onClick={goNext}
                  >
                    Suivant
                  </Button>
                </div>
              </CardContent>
            </Card>
          )}

          {/* Step 3: Coordonnées - LIVREUR ONLY */}
          {step === 3 && role === 'livreur' && (
            <Card className="border-2 border-gray-200">
              <CardContent className="p-4 md:p-6 space-y-3 md:space-y-4">
                <h3 className="text-center text-base md:text-lg font-semibold text-orange-500 mb-3 md:mb-4">Informations de localisation</h3>

                {/* 1. Pays */}
                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Pays</Label>
                  <Select value={formData.pays} onValueChange={(value) => updateField('pays', value)}>
                    <SelectTrigger className="border-gray-300 focus:border-orange-500 text-sm md:text-base">
                      <SelectValue placeholder="Sélectionner votre pays" />
                      <ChevronDown className="ml-2 h-4 w-4 text-orange-500" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="cameroun">Cameroun</SelectItem>
                      <SelectItem value="france">France</SelectItem>
                      <SelectItem value="belgique">Belgique</SelectItem>
                      <SelectItem value="suisse">Suisse</SelectItem>
                      <SelectItem value="canada">Canada</SelectItem>
                      <SelectItem value="usa">États-Unis</SelectItem>
                      <SelectItem value="autre">Autre</SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                {/* 2. Adresse personnelle */}
                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Adresse personnelle</Label>
                  <div className="relative">
                    <MapPin className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 md:h-5 md:w-5 text-orange-500" />
                    <Input
                      type="text"
                      placeholder="Quartier Bastos, Yaoundé"
                      value={formData.adressePersonnelle}
                      onChange={(e) => updateField('adressePersonnelle', e.target.value)}
                      className="pl-9 md:pl-10 border-gray-300 focus:border-orange-500 text-sm md:text-base"
                    />
                  </div>
                </div>

                {/* 3. Lieu dit de l'adresse */}
                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Lieu dit de l'adresse</Label>
                  <div className="relative">
                    <MapPin className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 md:h-5 md:w-5 text-orange-500" />
                    <Input
                      type="text"
                      placeholder="Face Pharmacie du Rond Point"
                      value={formData.lieuDitAdresse}
                      onChange={(e) => updateField('lieuDitAdresse', e.target.value)}
                      className="pl-9 md:pl-10 border-gray-300 focus:border-orange-500 text-sm md:text-base"
                    />
                  </div>
                </div>

                {/* 4. Photo d'identité */}
                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Photo d'identité</Label>
                  <div className="relative">
                    <label htmlFor="photo-identite" className="block">
                      <div className="border-2 border-dashed border-orange-300 rounded-lg p-4 md:p-6 cursor-pointer hover:border-orange-400 transition-colors text-center bg-orange-100 hover:bg-orange-100">
                        <div className="flex flex-col items-center gap-2 md:gap-3">
                          <ImageIcon className="h-10 w-10 md:h-12 md:w-12 text-orange-500" />
                          {photoIdentite ? (
                            <div className="text-center">
                              <p className="text-xs md:text-sm font-medium text-gray-700">{photoIdentite.name}</p>
                              <p className="text-xs text-gray-500">Cliquez pour changer</p>
                            </div>
                          ) : (
                            <div className="text-center">
                              <p className="text-xs md:text-sm font-medium text-gray-700">Cliquez pour téléverser</p>
                              <p className="text-xs text-gray-500">Photo d'identité (CNI, Passeport)</p>
                            </div>
                          )}
                        </div>
                      </div>
                      <input
                        id="photo-identite"
                        type="file"
                        accept="image/*"
                        onChange={handlePhotoIdentiteChange}
                        className="hidden"
                      />
                    </label>
                  </div>
                </div>

                {/* 5. Numéro CNI */}
                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Numéro CNI</Label>
                  <div className="relative">
                    <Camera className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 md:h-5 md:w-5 text-orange-500" />
                    <Input
                      type="text"
                      placeholder="123456789"
                      value={formData.numeroCNI}
                      onChange={(e) => updateField('numeroCNI', e.target.value)}
                      maxLength={9}
                      className="pl-9 md:pl-10 border-gray-300 focus:border-orange-500 text-sm md:text-base"
                    />
                  </div>
                  <p className="text-xs text-gray-500">9 chiffres requis</p>
                </div>

                {/* 6. Photos CNI Recto et Verso */}
                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Photos CNI</Label>
                  <div className="grid grid-cols-2 gap-3 md:gap-4">
                    {/* Recto */}
                    <label htmlFor="photo-cni-recto" className="block">
                      <div className="border-2 border-dashed border-orange-400 rounded-lg p-3 md:p-4 cursor-pointer hover:border-orange-500 transition-colors text-center bg-orange-100 hover:bg-orange-100">
                        <div className="flex flex-col items-center gap-1 md:gap-2">
                          <ImageIcon className="h-8 w-8 md:h-10 md:w-10 text-orange-500" />
                          {photoCniRecto ? (
                            <div className="text-center">
                              <p className="text-xs font-medium text-gray-700">Recto</p>
                              <p className="text-xs text-gray-600 truncate">{photoCniRecto.name}</p>
                            </div>
                          ) : (
                            <div className="text-center">
                              <p className="text-xs font-medium text-gray-700">Recto</p>
                              <p className="text-xs text-gray-500">Téléverser</p>
                            </div>
                          )}
                        </div>
                      </div>
                      <input
                        id="photo-cni-recto"
                        type="file"
                        accept="image/*"
                        onChange={handlePhotoCniRectoChange}
                        className="hidden"
                      />
                    </label>

                    {/* Verso */}
                    <label htmlFor="photo-cni-verso" className="block">
                      <div className="border-2 border-dashed border-orange-400 rounded-lg p-3 md:p-4 cursor-pointer hover:border-orange-500 transition-colors text-center bg-orange-100 hover:bg-orange-100">
                        <div className="flex flex-col items-center gap-1 md:gap-2">
                          <ImageIcon className="h-8 w-8 md:h-10 md:w-10 text-orange-500" />
                          {photoCniVerso ? (
                            <div className="text-center">
                              <p className="text-xs font-medium text-gray-700">Verso</p>
                              <p className="text-xs text-gray-600 truncate">{photoCniVerso.name}</p>
                            </div>
                          ) : (
                            <div className="text-center">
                              <p className="text-xs font-medium text-gray-700">Verso</p>
                              <p className="text-xs text-gray-500">Téléverser</p>
                            </div>
                          )}
                        </div>
                      </div>
                      <input
                        id="photo-cni-verso"
                        type="file"
                        accept="image/*"
                        onChange={handlePhotoCniVersoChange}
                        className="hidden"
                      />
                    </label>
                  </div>
                </div>

                {/* 7. Numéro NINE */}
                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Numéro NINE</Label>
                  <div className="relative">
                    <Camera className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 md:h-5 md:w-5 text-orange-500" />
                    <Input
                      type="text"
                      placeholder="M123456789A"
                      value={formData.numeroNINE}
                      onChange={(e) => updateField('numeroNINE', e.target.value)}
                      maxLength={12}
                      className="pl-9 md:pl-10 border-gray-300 focus:border-orange-500 text-sm md:text-base uppercase"
                    />
                  </div>
                  <p className="text-xs text-gray-500">Format: M123456789A (12 caractères)</p>
                </div>

                <div className="flex gap-2 md:gap-3 pt-3 md:pt-4">
                  <Button
                    variant="outline"
                    className="flex-1 border-orange-500 text-orange-500 hover:bg-orange-50 h-12 md:h-auto text-sm md:text-base bg-transparent"
                    onClick={() => setStep(2)}
                  >
                    Précédent
                  </Button>
                  <Button
                    className="flex-1 bg-orange-500 hover:bg-orange-600 h-12 md:h-auto text-sm md:text-base"
                    onClick={goNext}
                  >
                    Suivant
                  </Button>
                </div>
              </CardContent>
            </Card>
          )}

          {/* Step 3: Finalisation - CLIENT ONLY */}
          {step === 3 && role === 'client' && (
            <Card className="border-2 border-gray-200">
              <CardContent className="p-4 md:p-6 space-y-3 md:space-y-4">
                <h3 className="text-center text-base md:text-lg font-semibold text-orange-500 mb-3 md:mb-4">Vérification de Soumission</h3>

                {/* Résumé des informations */}
                <div className="bg-orange-50 border-2 border-orange-200 rounded-lg p-4 space-y-2 md:space-y-3">
                  <div className="flex flex-col gap-1">
                    <span className="text-xs md:text-sm font-medium text-orange-700">Type de compte:</span>
                    <span className="text-sm md:text-base text-gray-800">Client</span>
                  </div>
                  <div className="flex flex-col gap-1">
                    <span className="text-xs md:text-sm font-medium text-orange-700">Nom:</span>
                    <span className="text-sm md:text-base text-gray-800">{formData.nom} {formData.prenom}</span>
                  </div>
                  <div className="flex flex-col gap-1">
                    <span className="text-xs md:text-sm font-medium text-orange-700">Email:</span>
                    <span className="text-sm md:text-base text-gray-800">{formData.email}</span>
                  </div>
                </div>

                {/* Checkbox conditions */}
                <div className="flex items-start gap-3 pt-2">
                  <input
                    type="checkbox"
                    id="conditions"
                    checked={isTermsAccepted}
                    onChange={(e) => setIsTermsAccepted(e.target.checked)}
                    className="mt-1 w-4 h-4 md:w-5 md:h-5 text-orange-500 border-gray-300 rounded focus:ring-orange-500"
                  />
                  <label htmlFor="conditions" className="text-xs md:text-sm text-gray-700 cursor-pointer">
                    J'ai lu et j'accepte les conditions d'utilisation
                  </label>
                </div>

                {/* Boutons */}
                <div className="flex gap-2 md:gap-3 pt-3 md:pt-4">
                  <Button
                    variant="outline"
                    className="flex-1 border-orange-500 text-orange-500 hover:bg-orange-50 h-12 md:h-auto text-sm md:text-base bg-transparent"
                    onClick={goBack}
                  >
                    Précédent
                  </Button>
                  <Button
                    className={`flex-1 h-12 md:h-auto text-sm md:text-base ${isTermsAccepted
                      ? 'bg-orange-500 hover:bg-orange-600'
                      : 'bg-gray-300 cursor-not-allowed'
                      }`}
                    disabled={!isTermsAccepted}
                    onClick={async () => {
                      try {
                        console.log('Submitting Client:', formData);
                        const result = await createClient(formData);
                        console.log('Client created:', result);
                        alert('Compte créé avec succès !');
                        router.push('/'); // Redirect to home page (http://10.2.8.89:3000)
                      } catch (error) {
                        console.error('Registration failed', error);
                        alert('Erreur lors de la création du compte');
                      }
                    }}
                  >
                    Créer mon compte
                  </Button>
                </div>
              </CardContent>
            </Card>
          )}

          {/* Step 4: Véhicule - LIVREUR ONLY */}
          {step === 4 && role === 'livreur' && (
            <Card className="border-2 border-gray-200">
              <CardContent className="p-4 md:p-6 space-y-3 md:space-y-4">
                <div className="flex items-center justify-center mb-3 md:mb-4">
                  <Car className="h-6 w-6 md:h-8 md:w-8 text-orange-500 mr-2" />
                  <h3 className="text-base md:text-lg font-semibold text-orange-500 text-center">Informations sur le véhicule</h3>
                </div>

                {/* 1. Type de véhicule */}
                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Type de véhicule</Label>
                  <Select value={formData.typeVehicule} onValueChange={(value) => updateField('typeVehicule', value)}>
                    <SelectTrigger className="border-gray-300 focus:border-orange-500 text-sm md:text-base">
                      <SelectValue placeholder="Sélectionner le type de véhicule" />
                      <ChevronDown className="ml-2 h-4 w-4 text-orange-500" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="voiture">Voiture</SelectItem>
                      <SelectItem value="moto">Moto</SelectItem>
                      <SelectItem value="scooter">Scooter</SelectItem>
                      <SelectItem value="camionnette">Camionnette</SelectItem>
                      <SelectItem value="velo">Vélo</SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                {/* 2. Marque de véhicule */}
                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Marque de véhicule</Label>
                  <div className="relative">
                    <Car className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 md:h-5 md:w-5 text-orange-500" />
                    <Input
                      type="text"
                      placeholder="Toyota, Mercedes, etc."
                      value={formData.marqueVehicule}
                      onChange={(e) => updateField('marqueVehicule', e.target.value)}
                      className="pl-9 md:pl-10 border-gray-300 focus:border-orange-500 text-sm md:text-base"
                    />
                  </div>
                </div>

                {/* 3. Photos du véhicule */}
                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Photos du véhicule</Label>
                  <div className="grid grid-cols-2 gap-3 md:gap-4">
                    {/* Avant */}
                    <label htmlFor="photo-vehicule-avant" className="block">
                      <div className="border-2 border-dashed border-orange-400 rounded-lg p-3 md:p-4 cursor-pointer hover:border-orange-500 transition-colors text-center bg-orange-100 hover:bg-orange-100">
                        <div className="flex flex-col items-center gap-1 md:gap-2">
                          <ImageIcon className="h-8 w-8 md:h-10 md:w-10 text-orange-500" />
                          {photoVehiculeAvant ? (
                            <div className="text-center">
                              <p className="text-xs font-medium text-gray-700">Avant</p>
                              <p className="text-xs text-gray-600 truncate">{photoVehiculeAvant.name}</p>
                            </div>
                          ) : (
                            <div className="text-center">
                              <p className="text-xs font-medium text-gray-700">Avant</p>
                              <p className="text-xs text-gray-500">Téléverser</p>
                            </div>
                          )}
                        </div>
                      </div>
                      <input
                        id="photo-vehicule-avant"
                        type="file"
                        accept="image/*"
                        onChange={handlePhotoVehiculeAvantChange}
                        className="hidden"
                      />
                    </label>

                    {/* Arrière */}
                    <label htmlFor="photo-vehicule-arriere" className="block">
                      <div className="border-2 border-dashed border-orange-400 rounded-lg p-3 md:p-4 cursor-pointer hover:border-orange-500 transition-colors text-center bg-orange-100 hover:bg-orange-100">
                        <div className="flex flex-col items-center gap-1 md:gap-2">
                          <ImageIcon className="h-8 w-8 md:h-10 md:w-10 text-orange-500" />
                          {photoVehiculeArriere ? (
                            <div className="text-center">
                              <p className="text-xs font-medium text-gray-700">Arrière</p>
                              <p className="text-xs text-gray-600 truncate">{photoVehiculeArriere.name}</p>
                            </div>
                          ) : (
                            <div className="text-center">
                              <p className="text-xs font-medium text-gray-700">Arrière</p>
                              <p className="text-xs text-gray-500">Téléverser</p>
                            </div>
                          )}
                        </div>
                      </div>
                      <input
                        id="photo-vehicule-arriere"
                        type="file"
                        accept="image/*"
                        onChange={handlePhotoVehiculeArriereChange}
                        className="hidden"
                      />
                    </label>
                  </div>
                </div>

                {/* 4. Numéro d'immatriculation */}
                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Numéro d'immatriculation</Label>
                  <div className="relative">
                    <Car className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 md:h-5 md:w-5 text-orange-500" />
                    <Input
                      type="text"
                      placeholder="LT-806-AA"
                      value={formData.numeroImmatriculation}
                      onChange={(e) => updateField('numeroImmatriculation', e.target.value)}
                      className="pl-9 md:pl-10 border-gray-300 focus:border-orange-500 text-sm md:text-base uppercase"
                    />
                  </div>
                </div>

                {/* 5. Couleur de la voiture */}
                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Couleur de la voiture</Label>
                  <Select value={formData.couleurVoiture} onValueChange={(value) => updateField('couleurVoiture', value)}>
                    <SelectTrigger className="border-gray-300 focus:border-orange-500 text-sm md:text-base">
                      <SelectValue placeholder="Sélectionner la couleur" />
                      <ChevronDown className="ml-2 h-4 w-4 text-orange-500" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="blanc">Blanc</SelectItem>
                      <SelectItem value="noir">Noir</SelectItem>
                      <SelectItem value="gris">Gris</SelectItem>
                      <SelectItem value="rouge">Rouge</SelectItem>
                      <SelectItem value="bleu">Bleu</SelectItem>
                      <SelectItem value="vert">Vert</SelectItem>
                      <SelectItem value="jaune">Jaune</SelectItem>
                      <SelectItem value="or">Or</SelectItem>
                      <SelectItem value="argent">Argent</SelectItem>
                      <SelectItem value="autre">Autre</SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                {/* 6. Dimensions de la malle arrière (optionnel) */}
                <div className="space-y-1 md:space-y-2">
                  <Label className="text-gray-700 text-xs md:text-sm font-medium">Dimensions de la malle arrière (optionnel)</Label>
                  <div className="relative">
                    <Input
                      type="text"
                      placeholder="50x30x40cm"
                      value={formData.dimensionsMalle}
                      onChange={(e) => updateField('dimensionsMalle', e.target.value)}
                      className="border-gray-300 focus:border-orange-500 text-sm md:text-base"
                    />
                  </div>
                  <p className="text-xs text-gray-500">Exemple: 50x30x40cm</p>
                </div>

                {/* Boutons */}
                <div className="flex gap-2 md:gap-3 pt-3 md:pt-4">
                  <Button
                    variant="outline"
                    className="flex-1 border-orange-500 text-orange-500 hover:bg-orange-50 h-12 md:h-auto text-sm md:text-base bg-transparent"
                    onClick={goBack}
                  >
                    Précédent
                  </Button>
                  <Button
                    className="flex-1 bg-orange-500 hover:bg-orange-600 h-12 md:h-auto text-sm md:text-base"
                    onClick={() => console.log('Submit:', formData)}
                  >
                    Créer mon compte
                  </Button>
                </div>
              </CardContent>
            </Card>
          )}
        </div>
      </main>

      <footer className="p-3 md:p-6 border-t border-gray-200 mt-auto">
        <div className="max-w-md mx-auto text-center">
          <p className="text-xs md:text-sm text-gray-600">
            Vous avez déjà un compte?{' '}
            <Link href="/" className="text-orange-500 hover:text-orange-600 font-semibold">
              Se connecter
            </Link>
          </p>
        </div>
      </footer>
    </div>
  )
}
