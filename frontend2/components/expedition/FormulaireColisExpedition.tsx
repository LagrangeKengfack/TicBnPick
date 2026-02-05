import React, { useState, useEffect, useRef } from 'react';
import { 
  TruckIcon, 
  PhotoIcon, 
  XCircleIcon, 
  ScaleIcon, 
  ExclamationTriangleIcon, 
  ClockIcon,
  ShieldCheckIcon, 
  CheckCircleIcon,
  ShoppingBagIcon,
  HomeIcon,
  MapPinIcon
} from '@heroicons/react/24/outline';
import { ArrowLeft, ArrowRight, Package, Flame, Shield, Zap, Droplets, Truck, Bike, Car } from 'lucide-react';
import { 
  BsBicycle           // Vélo Bootstrap
} from 'react-icons/bs';
import { 
  MdDeliveryDining
} from 'react-icons/md';

import { PackageData, PackageRegistrationProps } from '@/types/package';

const LoadingDots = () => (
  <div className="flex space-x-1">
    {[0, 0.1, 0.2].map((delay, i) => (
      <div key={i} className="w-2 h-2 bg-orange-500 rounded-full animate-bounce" style={{ animationDelay: `${delay}s` }}></div>
    ))}
  </div>
);

const OptionCard = ({ title, subtitle, icon, additionalCost, isSelected, onClick, compact = false }: { 
  title: string;
  subtitle?: string;
  icon: React.ReactNode;
  additionalCost?: number;
  isSelected: boolean;
  onClick: () => void;
  compact?: boolean;
}) => (
  <div
    onClick={onClick}
    className={`relative p-3 border-2 rounded-xl cursor-pointer transition-all hover:scale-105 ${
      isSelected 
        ? 'border-orange-500 bg-orange-50 dark:bg-orange-950/30 dark:border-orange-400 shadow-lg' 
        : 'border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 hover:border-orange-300 dark:hover:border-orange-500 hover:shadow-md'
    } ${compact ? 'text-center' : ''}`}
  >
    <div className={`flex ${compact ? 'flex-col' : 'items-center'} ${compact ? 'space-y-2' : 'space-x-3'}`}>
      <div className={`p-2 rounded-lg ${
        isSelected 
          ? 'bg-orange-500 text-white' 
          : 'bg-gray-100 dark:bg-gray-700 text-gray-500 dark:text-gray-400'
        } ${compact ? 'mx-auto' : ''}`}>
        {icon}
      </div>
      <div className={compact ? 'text-center' : 'flex-1'}>
        <h4 className={`font-semibold text-sm ${
          isSelected 
            ? 'text-orange-800 dark:text-orange-200' 
            : 'text-gray-800 dark:text-gray-200'
        }`}>
          {title}
        </h4>
        {subtitle && (
          <p className={`text-xs ${
            isSelected 
              ? 'text-orange-600 dark:text-orange-300' 
              : 'text-gray-500 dark:text-gray-400'
          }`}>
            {subtitle}
          </p>
        )}
      </div>
    </div>
    
    {additionalCost && additionalCost > 0 && (
      <div className={`mt-2 text-xs font-medium ${
        isSelected 
          ? 'text-orange-700 dark:text-orange-300' 
          : 'text-gray-600 dark:text-gray-400'
        } ${compact ? 'text-center' : ''}`}>
        + {additionalCost.toLocaleString()} FCFA
      </div>
    )}
    
    {isSelected && (
      <div className="absolute -top-2 -right-2 bg-orange-500 text-white rounded-full p-1">
        <CheckCircleIcon className="w-3 h-3" />
      </div>
    )}
  </div>
);

export default function PackageRegistration({
  initialData = {},
  onContinue,
  onBack,
}: PackageRegistrationProps) {
  // << CORRIGÉ: Initialisation de l'état avec la nouvelle structure
  const [packageData, setPackageData] = useState<PackageData>({
    photo: null,
    designation: "",
    description: "",
    weight: "",
    length: "",
    width: "",
    height: "",
    isFragile: false,
    isPerishable: false,
    isLiquid: false,
    isInsured: false,
    declaredValue: "",
    transportMethod: "",
    logistics: "standard",
    pickup: false,
    delivery: false,
    hasPackageNow: false,
    exactPickupAddress: "",
    coordinates: null,
    ...initialData,
  });

  const [expressOption, setExpressOption] = useState<"none" | "24h" | "48h">(
    "none",
  );
  const [priceLoading, setPriceLoading] = useState(false);
  const [price, setPrice] = useState<number | null>(null);
  const [volume, setVolume] = useState(0);
  const [isFormValid, setIsFormValid] = useState(false);
  const [validationError, setValidationError] = useState<string>("");
  // MODIFICATION: Initialisation sécurisée de la prévisualisation
  const [photoPreview, setPhotoPreview] = useState<string | null>(() => {
    if (!initialData?.photo) return null;
    if (typeof initialData.photo === "string") return initialData.photo;
    // On ne peut pas créer d'ObjectUrl lors du SSR ou render initial pur sans side effect propre
    // On laisse l'effet s'en charger si c'est un File
    return null;
  });

  const fileInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    const isValid = Boolean(
      packageData.photo &&
      packageData.designation.trim().length >= 3 &&
      packageData.weight.trim() &&
      !isNaN(parseFloat(packageData.weight)) &&
      parseFloat(packageData.weight) > 0,
    );

    setIsFormValid(isValid);

    if (!isValid) {
      if (!packageData.photo) {
        setValidationError("Veuillez ajouter une photo du colis");
      } else if (packageData.designation.trim().length < 3) {
        setValidationError(
          "La désignation doit contenir au moins 3 caractères",
        );
      } else if (
        !packageData.weight.trim() ||
        isNaN(parseFloat(packageData.weight)) ||
        parseFloat(packageData.weight) <= 0
      ) {
        setValidationError("Veuillez saisir un poids valide");
      }
    } else {
      setValidationError("");
    }
  }, [packageData.photo, packageData.designation, packageData.weight]);

  useEffect(() => {
    if (!isFormValid) {
      setPrice(null);
      return;
    }

    setPriceLoading(true);

    const calculatePrice = () => {
      const weight = parseFloat(packageData.weight) || 0;
      const length = parseFloat(packageData.length) || 10;
      const width = parseFloat(packageData.width) || 10;
      const height = parseFloat(packageData.height) || 10;

      const calculatedVolume = (length * width * height) / 1000000;
      const volumetricWeight = calculatedVolume * 200;
      const billableWeight = Math.max(weight, volumetricWeight);

      setVolume(calculatedVolume);

      let basePrice = 1500 + billableWeight * 300;

      if (packageData.isFragile) basePrice += 1200;
      if (packageData.isPerishable) basePrice += 800;
      if (packageData.isLiquid) basePrice += 500;
      if (packageData.isInsured && parseFloat(packageData.declaredValue) > 0) {
        basePrice += parseFloat(packageData.declaredValue) * 0.02;
      }

      if (packageData.pickup) basePrice += 1000;
      if (packageData.delivery) basePrice += 1000;

      let expressMultiplier = 1;
      if (expressOption === "24h") expressMultiplier = 2.0;
      else if (expressOption === "48h") expressMultiplier = 1.5;

      const finalPrice = Math.round(basePrice * expressMultiplier);

      setTimeout(() => {
        setPrice(finalPrice);
        setPriceLoading(false);
      }, 600);
    };

    calculatePrice();
  }, [packageData, expressOption, isFormValid]);

  const handleInputChange = (field: keyof PackageData, value: any) => {
    setPackageData((prev) => {
      const newData = { ...prev, [field]: value };

      // Pré-remplir la description avec la désignation si la description est vide
      if (field === "designation" && value && !prev.description.trim()) {
        newData.description = value;
      }

      return newData;
    });
  };

  const handleImageUpload = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
      if (file.size > 5 * 1024 * 1024) {
        alert(
          "Le fichier est trop volumineux. Veuillez choisir une image de moins de 5MB.",
        );
        return;
      }

      handleInputChange("photo", file); // On stocke l'objet Fichier
      setPhotoPreview(URL.createObjectURL(file)); // On crée une URL locale pour l'aperçu

      const reader = new FileReader();
      reader.onload = (e) => {
        handleInputChange("photo", e.target?.result as string);
      };
      reader.readAsDataURL(file);
    }
  };

  const validateNumberInput = (e: React.KeyboardEvent<HTMLInputElement>) => {
    const allowedKeys = [
      "Backspace",
      "Delete",
      "Tab",
      "Escape",
      "Enter",
      "ArrowLeft",
      "ArrowRight",
    ];
    if (
      allowedKeys.includes(e.key) ||
      (e.key >= "0" && e.key <= "9") ||
      (e.key === "." && !e.currentTarget.value.includes("."))
    ) {
      return;
    }
    e.preventDefault();
  };

  const handleContinue = () => {
    if (isFormValid && price !== null) {
      onContinue(packageData, price);
    }
  };

  const logisticsOptions = [
    { key: "truck", label: "Camion", icon: <TruckIcon className="w-4 h-4" /> },
    {
      key: "tricycle",
      label: "Tricycle",
      icon: <MdDeliveryDining className="w-4 h-4" />,
    },
    { key: "moto", label: "Moto", icon: <Bike className="w-4 h-4" /> },
    { key: "bike", label: "Vélo", icon: <BsBicycle className="w-4 h-4" /> },
    { key: "car", label: "Voiture", icon: <Car className="w-4 h-4" /> },
  ];

  // --- NEW: SMART PICKUP LOGIC ---
  const [loadingLoc, setLoadingLoc] = useState(false);

  const handleSmartPickup = async () => {
    if (!navigator.geolocation) {
      alert("La géolocalisation n'est pas supportée par votre navigateur.");
      return;
    }

    setLoadingLoc(true);

    navigator.geolocation.getCurrentPosition(
      async (position) => {
        const { latitude, longitude } = position.coords;

        try {
          // Reverse Geocoding using OpenStreetMap (Free)
          const response = await fetch(
            `https://nominatim.openstreetmap.org/reverse?format=json&lat=${latitude}&lon=${longitude}&zoom=18`,
            { headers: { "Accept-Language": "fr" } },
          );
          const data = await response.json();
          const address = data.display_name || "Adresse détectée";

          // Update state with coordinates and address
          setPackageData((prev) => ({
            ...prev,
            hasPackageNow: true,
            exactPickupAddress: address,
            coordinates: { lat: latitude, lng: longitude },
          }));
        } catch (error) {
          console.error("Geocoding error", error);
          alert("Erreur lors de la récupération de l'adresse.");
        } finally {
          setLoadingLoc(false);
        }
      },
      (error) => {
        setLoadingLoc(false);
        alert(
          "Veuillez autoriser l'accès à la position dans votre navigateur.",
        );
      },
    );
  };

  return (
    <div className="max-w-7xl mx-auto p-4 min-h-screen bg-gray-50 dark:bg-transparent transition-colors">
      <div className="text-center mb-6">
        <h1 className="text-2xl font-bold text-gray-800 dark:text-gray-100 mb-1">
          Enregistrement du colis
        </h1>
        <p className="text-gray-600 dark:text-gray-400 text-sm">
          Décrivez votre colis pour obtenir une estimation précise
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-4">
          {/* Photo du colis */}
          <div className="bg-white dark:bg-gray-800 p-4 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700">
            <div className="flex items-center mb-3">
              <PhotoIcon className="w-4 h-4 text-orange-500 mr-2" />
              <h3 className="font-semibold text-gray-800 dark:text-gray-200">
                Photo du colis <span className="text-red-500">*</span>
              </h3>
            </div>

            {!packageData.photo ? (
              <div
                onClick={() => fileInputRef.current?.click()}
                className="border-2 border-dashed border-gray-300 dark:border-gray-600 rounded-lg p-6 text-center cursor-pointer hover:border-orange-400 dark:hover:border-orange-500 hover:bg-orange-50 dark:hover:bg-orange-950/20 transition-colors"
              >
                <PhotoIcon className="w-8 h-8 text-gray-400 dark:text-gray-500 mx-auto mb-2" />
                <p className="text-gray-600 dark:text-gray-300 font-medium text-sm">
                  Cliquez pour ajouter une photo
                </p>
                <p className="text-xs text-gray-500 dark:text-gray-400 mt-1">
                  PNG, JPG jusqu'à 5MB
                </p>
              </div>
            ) : (
              <div className="relative inline-block">
                <img
                  src={packageData.photo}
                  alt="Colis"
                  className="w-32 h-32 object-cover rounded-lg border border-gray-200 dark:border-gray-600"
                />
                <button
                  onClick={() => handleInputChange("photo", null)}
                  className="absolute -top-2 -right-2 bg-red-500 text-white rounded-full p-1 hover:bg-red-600 transition-colors"
                >
                  <XCircleIcon className="w-4 h-4" />
                </button>
              </div>
            )}

            <input
              ref={fileInputRef}
              type="file"
              accept="image/*"
              onChange={handleImageUpload}
              className="hidden"
            />
          </div>

          {/* Informations de base */}
          <div className="bg-white dark:bg-gray-800 p-4 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700">
            <div className="flex items-center mb-3">
              <ShoppingBagIcon className="w-4 h-4 text-orange-500 mr-2" />
              <h3 className="font-semibold text-gray-800 dark:text-gray-200">
                Informations de base
              </h3>
            </div>

            <div className="space-y-3">
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                  Désignation du colis <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  value={packageData.designation}
                  onChange={(e) =>
                    handleInputChange("designation", e.target.value)
                  }
                  placeholder="Ex: Vêtements, Livres, Électronique..."
                  className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-sm bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 placeholder-gray-500 dark:placeholder-gray-400"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                  Description détaillée
                </label>
                <textarea
                  value={packageData.description}
                  onChange={(e) =>
                    handleInputChange("description", e.target.value)
                  }
                  placeholder="Décrivez le contenu de votre colis..."
                  rows={2}
                  className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-sm bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 placeholder-gray-500 dark:placeholder-gray-400"
                />
              </div>
            </div>
          </div>

          {/* Poids et dimensions */}
          <div className="bg-white dark:bg-gray-800 p-4 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700">
            <div className="flex items-center mb-3">
              <ScaleIcon className="w-4 h-4 text-orange-500 mr-2" />
              <h3 className="font-semibold text-gray-800 dark:text-gray-200">
                Poids et dimensions
              </h3>
            </div>

            <div className="space-y-3">
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                  Poids (kg) <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  value={packageData.weight}
                  onChange={(e) => handleInputChange("weight", e.target.value)}
                  onKeyDown={validateNumberInput}
                  placeholder="1.5"
                  className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-sm bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 placeholder-gray-500 dark:placeholder-gray-400"
                />
              </div>

              <div className="grid grid-cols-3 gap-3">
                <div>
                  <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                    Longueur (cm)
                  </label>
                  <input
                    type="text"
                    value={packageData.length}
                    onChange={(e) =>
                      handleInputChange("length", e.target.value)
                    }
                    onKeyDown={validateNumberInput}
                    placeholder="20"
                    className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-sm bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 placeholder-gray-500 dark:placeholder-gray-400"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                    Largeur (cm)
                  </label>
                  <input
                    type="text"
                    value={packageData.width}
                    onChange={(e) => handleInputChange("width", e.target.value)}
                    onKeyDown={validateNumberInput}
                    placeholder="15"
                    className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-sm bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 placeholder-gray-500 dark:placeholder-gray-400"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                    Hauteur (cm)
                  </label>
                  <input
                    type="text"
                    value={packageData.height}
                    onChange={(e) =>
                      handleInputChange("height", e.target.value)
                    }
                    onKeyDown={validateNumberInput}
                    placeholder="10"
                    className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-sm bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 placeholder-gray-500 dark:placeholder-gray-400"
                  />
                </div>
              </div>
            </div>
          </div>

          {/* Options spéciales */}
          <div className="bg-white dark:bg-gray-800 p-4 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700">
            <h3 className="font-semibold text-gray-800 dark:text-gray-200 mb-3">
              Options spéciales
            </h3>

            <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
              <OptionCard
                title="Fragile"
                icon={<ExclamationTriangleIcon className="w-4 h-4" />}
                additionalCost={1200}
                isSelected={packageData.isFragile}
                onClick={() =>
                  handleInputChange("isFragile", !packageData.isFragile)
                }
                compact={true}
              />

              <OptionCard
                title="Périssable"
                icon={<Flame className="w-4 h-4" />}
                additionalCost={800}
                isSelected={packageData.isPerishable}
                onClick={() =>
                  handleInputChange("isPerishable", !packageData.isPerishable)
                }
                compact={true}
              />

              <OptionCard
                title="Liquide"
                icon={<Droplets className="w-4 h-4" />}
                additionalCost={500}
                isSelected={packageData.isLiquid}
                onClick={() =>
                  handleInputChange("isLiquid", !packageData.isLiquid)
                }
                compact={true}
              />

              <OptionCard
                title="Assuré"
                icon={<Shield className="w-4 h-4" />}
                isSelected={packageData.isInsured}
                onClick={() =>
                  handleInputChange("isInsured", !packageData.isInsured)
                }
                compact={true}
              />
            </div>

            {packageData.isInsured && (
              <div className="mt-3">
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                  Valeur déclarée (FCFA)
                </label>
                <input
                  type="text"
                  value={packageData.declaredValue}
                  onChange={(e) =>
                    handleInputChange("declaredValue", e.target.value)
                  }
                  onKeyDown={validateNumberInput}
                  placeholder="50000"
                  className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:ring-2 focus:ring-orange-500 focus:border-orange-500 text-sm bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 placeholder-gray-500 dark:placeholder-gray-400"
                />
              </div>
            )}
          </div>

          {/* Choix de la logistique */}
          <div className="bg-white dark:bg-gray-800 p-4 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700">
            <h3 className="font-semibold text-gray-800 dark:text-gray-200 mb-3">
              Moyen de transport (optionnel)
            </h3>
            <div className="grid grid-cols-3 md:grid-cols-5 gap-2">
              {logisticsOptions.map((option) => (
                <OptionCard
                  key={option.key}
                  title={option.label}
                  icon={option.icon}
                  isSelected={packageData.logistics === option.key}
                  onClick={() =>
                    handleInputChange(
                      "logistics",
                      packageData.logistics === option.key ? "" : option.key,
                    )
                  }
                  compact={true}
                />
              ))}
            </div>
          </div>

          {/* Services additionnels & Smart Pickup */}
          <div className="bg-white dark:bg-gray-800 p-4 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700">
            <h3 className="font-semibold text-gray-800 dark:text-gray-200 mb-3">
              Options de ramassage
            </h3>

            <div className="space-y-4">
              {/* Smart Pickup Button */}
              <div className="p-4 bg-orange-50 dark:bg-orange-900/20 border border-orange-200 dark:border-orange-800 rounded-xl">
                <div className="flex items-center justify-between mb-3">
                  <div className="flex items-center gap-2">
                    <MapPinIcon className="w-5 h-5 text-orange-600" />
                    <div>
                      <p className="text-sm font-bold text-orange-900 dark:text-orange-100">
                        Avez-vous le colis avec vous ?
                      </p>
                      <p className="text-xs text-orange-700 dark:text-orange-300">
                        Nous pouvons vous localiser pour le coursier
                      </p>
                    </div>
                  </div>
                </div>

                <div className="flex gap-2">
                  <button
                    type="button"
                    onClick={handleSmartPickup}
                    className={`flex-1 flex items-center justify-center gap-2 py-2 px-4 rounded-lg font-bold text-xs transition-all ${
                      packageData.hasPackageNow
                        ? "bg-green-600 text-white"
                        : "bg-orange-500 text-white hover:bg-orange-600"
                    }`}
                  >
                    {loadingLoc ? (
                      <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
                    ) : (
                      <CheckCircleIcon className="w-4 h-4" />
                    )}
                    {packageData.hasPackageNow
                      ? "Position Validée"
                      : "Oui, localisez-moi"}
                  </button>

                  <button
                    type="button"
                    onClick={() =>
                      setPackageData((prev) => ({
                        ...prev,
                        hasPackageNow: false,
                        exactPickupAddress: "",
                        coordinates: null,
                      }))
                    }
                    className="py-2 px-4 border border-gray-300 dark:border-gray-600 rounded-lg text-xs font-medium text-gray-600 dark:text-gray-400"
                  >
                    Non
                  </button>
                </div>

                {/* Automated Address Field */}
                {packageData.exactPickupAddress && (
                  <div className="mt-3 animate-in fade-in slide-in-from-top-1">
                    <label className="text-[10px] font-bold text-orange-600 uppercase mb-1 block">
                      Adresse de ramassage détectée
                    </label>
                    <div className="flex items-start gap-2 bg-white dark:bg-gray-700 p-2 rounded border border-orange-200 dark:border-orange-900">
                      <MapPinIcon className="w-4 h-4 text-orange-500 mt-0.5 flex-shrink-0" />
                      <p className="text-xs text-gray-700 dark:text-gray-200 italic leading-tight">
                        {packageData.exactPickupAddress}
                      </p>
                    </div>
                  </div>
                )}
              </div>

              {/* Standard Home Pickup Card */}
              <OptionCard
                title="Récupération à domicile programmée"
                subtitle="Un livreur vient chercher le colis plus tard"
                icon={<HomeIcon className="w-4 h-4" />}
                additionalCost={1000}
                isSelected={packageData.pickup}
                onClick={() => handleInputChange("pickup", !packageData.pickup)}
              />
            </div>
          </div>

          {/* Livraison express */}
          <div className="bg-white dark:bg-gray-800 p-4 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700">
            <h3 className="font-semibold text-gray-800 dark:text-gray-200 mb-3">
              Livraison express
            </h3>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
              <OptionCard
                title="Standard"
                subtitle="3-5 jours ouvrables"
                icon={<TruckIcon className="w-4 h-4" />}
                isSelected={expressOption === "none"}
                onClick={() => setExpressOption("none")}
                compact={true}
              />

              <OptionCard
                title="Express 48h"
                subtitle="Livraison en 2 jours"
                icon={<ClockIcon className="w-4 h-4" />}
                isSelected={expressOption === "48h"}
                onClick={() => setExpressOption("48h")}
                compact={true}
              />

              <OptionCard
                title="Express 24h"
                subtitle="Livraison en 1 jour"
                icon={<Zap className="w-4 h-4" />}
                isSelected={expressOption === "24h"}
                onClick={() => setExpressOption("24h")}
                compact={true}
              />
            </div>
          </div>
        </div>

        {/* Résumé du colis - Sticky */}
        <div className="lg:col-span-1">
          <div className="sticky top-44 bg-gradient-to-br from-orange-50 to-amber-50 dark:from-orange-950/30 dark:to-amber-950/30 p-4 rounded-lg border-2 border-orange-200 dark:border-orange-800 shadow-sm">
            <h3 className="text-lg font-bold text-orange-800 dark:text-orange-200 mb-3 flex items-center">
              <Package className="w-4 h-4 mr-2" />
              Résumé du colis
            </h3>

            {/* Photo dans le résumé */}
            {packageData.photo && (
              <div className="mb-3 text-center">
                <img
                  src={packageData.photo}
                  alt="Colis"
                  className="w-20 h-20 object-cover rounded-lg mx-auto border border-orange-200 dark:border-orange-700"
                />
              </div>
            )}

            <div className="space-y-2 mb-4 text-sm">
              <div>
                <span className="font-medium text-gray-600 dark:text-gray-400">
                  Désignation:
                </span>
                <p className="text-gray-800 dark:text-gray-200 font-semibold">
                  {packageData.designation || "..."}
                </p>
              </div>

              <div>
                <span className="font-medium text-gray-600 dark:text-gray-400">
                  Poids:
                </span>
                <p className="text-gray-800 dark:text-gray-200 font-semibold">
                  {packageData.weight ? `${packageData.weight} kg` : "..."}
                </p>
              </div>

              {volume > 0 && (
                <div>
                  <span className="font-medium text-gray-600 dark:text-gray-400">
                    Volume:
                  </span>
                  <p className="text-gray-800 dark:text-gray-200 font-semibold">
                    {volume.toFixed(3)} m³
                  </p>
                </div>
              )}

              {/* Options actives */}
              {(packageData.isFragile ||
                packageData.isPerishable ||
                packageData.isLiquid ||
                packageData.isInsured ||
                packageData.pickup ||
                packageData.delivery) && (
                <div>
                  <span className="font-medium text-gray-600 dark:text-gray-400">
                    Options:
                  </span>
                  <div className="flex flex-wrap gap-1 mt-1">
                    {packageData.isFragile && (
                      <span className="px-2 py-1 bg-orange-100 dark:bg-orange-900/50 text-orange-700 dark:text-orange-300 text-xs rounded-full">
                        Fragile
                      </span>
                    )}
                    {packageData.isPerishable && (
                      <span className="px-2 py-1 bg-orange-100 dark:bg-orange-900/50 text-orange-700 dark:text-orange-300 text-xs rounded-full">
                        Périssable
                      </span>
                    )}
                    {packageData.isLiquid && (
                      <span className="px-2 py-1 bg-orange-100 dark:bg-orange-900/50 text-orange-700 dark:text-orange-300 text-xs rounded-full">
                        Liquide
                      </span>
                    )}
                    {packageData.isInsured && (
                      <span className="px-2 py-1 bg-orange-100 dark:bg-orange-900/50 text-orange-700 dark:text-orange-300 text-xs rounded-full">
                        Assuré
                      </span>
                    )}
                    {packageData.pickup && (
                      <span className="px-2 py-1 bg-orange-100 dark:bg-orange-900/50 text-orange-700 dark:text-orange-300 text-xs rounded-full">
                        Récupération
                      </span>
                    )}
                    {packageData.delivery && (
                      <span className="px-2 py-1 bg-orange-100 dark:bg-orange-900/50 text-orange-700 dark:text-orange-300 text-xs rounded-full">
                        Livraison
                      </span>
                    )}
                  </div>
                </div>
              )}

              {expressOption !== "none" && (
                <div>
                  <span className="font-medium text-gray-600 dark:text-gray-400">
                    Livraison:
                  </span>
                  <p className="text-gray-800 dark:text-gray-200 font-semibold">
                    Express {expressOption === "24h" ? "24h" : "48h"}
                  </p>
                </div>
              )}
            </div>

            {/* Prix de manutention */}
            <div className="border-t-2 border-dashed border-orange-200 dark:border-orange-700 pt-3 mb-4">
              <div className="text-center">
                <p className="text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                  Prix de manutention
                </p>
                {priceLoading ? (
                  <div className="flex items-center justify-center space-x-2">
                    <LoadingDots />
                    <span className="text-xs text-gray-500 dark:text-gray-400">
                      Calcul...
                    </span>
                  </div>
                ) : price !== null ? (
                  <div>
                    <p className="text-2xl font-bold text-orange-600 dark:text-orange-400">
                      {price.toLocaleString()}
                      <span className="text-sm font-normal text-gray-500 dark:text-gray-400 ml-1">
                        FCFA
                      </span>
                    </p>
                    <p className="text-xs text-gray-500 dark:text-gray-400">
                      Manutention uniquement
                    </p>
                  </div>
                ) : (
                  <p className="text-gray-500 dark:text-gray-400 text-xs">
                    Complétez le formulaire
                  </p>
                )}
              </div>
            </div>

            {validationError && (
              <div className="mb-3 p-2 bg-red-50 dark:bg-red-950/30 border border-red-200 dark:border-red-800 rounded-md">
                <p className="text-red-700 dark:text-red-400 text-xs font-medium">
                  {validationError}
                </p>
              </div>
            )}

            <div className="space-y-2">
              <button
                onClick={handleContinue}
                disabled={!isFormValid || price === null}
                className={`w-full flex items-center justify-center py-2 px-3 rounded-md font-semibold text-sm transition-all ${
                  isFormValid && price !== null
                    ? "bg-gradient-to-r from-orange-500 to-orange-600 text-white hover:from-orange-600 hover:to-orange-700 shadow-md hover:shadow-lg"
                    : "bg-gray-300 dark:bg-gray-700 text-gray-500 dark:text-gray-400 cursor-not-allowed"
                }`}
              >
                Continuer vers les adresses
                <ArrowRight className="w-4 h-4 ml-2" />
              </button>

              {onBack && (
                <button
                  onClick={onBack}
                  className="w-full flex items-center justify-center py-2 px-3 border border-gray-300 dark:border-gray-600 rounded-md font-semibold text-sm text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
                >
                  <ArrowLeft className="w-4 h-4 mr-2" />
                  Retour
                </button>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
