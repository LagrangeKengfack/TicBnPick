"use client"

import React from "react"

import { useState } from "react"
import { Eye, EyeOff, Shield } from "lucide-react"
import Link from "next/link"

export default function LoginPage() {
  const [showPassword, setShowPassword] = useState(false)
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    // Handle login logic here
    console.log("Login attempt:", { email, password })
  }

  return (
    <div className="min-h-screen bg-[#f5f0e6] flex items-center justify-center p-4">
      <div className="w-full max-w-md bg-white rounded-2xl shadow-sm p-8">
        {/* Header */}
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-[#1e2a4a] mb-2">Connexion</h1>
          <p className="text-[#5a6b8a]">Accédez à votre espace de gestion</p>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-5">
          {/* Email Field */}
          <div>
            <label
              htmlFor="email"
              className="block text-sm font-medium text-[#1e2a4a] mb-2"
            >
              Adresse email
            </label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full px-4 py-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#e67e22] focus:border-transparent transition-all"
              required
            />
          </div>

          {/* Password Field */}
          <div>
            <label
              htmlFor="password"
              className="block text-sm font-medium text-[#1e2a4a] mb-2"
            >
              Mot de passe
            </label>
            <div className="relative">
              <input
                type={showPassword ? "text" : "password"}
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full px-4 py-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#e67e22] focus:border-transparent transition-all pr-12"
                required
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-4 top-1/2 -translate-y-1/2 text-orange-500 hover:text-orange-600 transition-colors"
                aria-label={showPassword ? "Masquer le mot de passe" : "Afficher le mot de passe"}
              >
                {showPassword ? <Eye size={20} /> : <EyeOff size={20} />}
              </button>
            </div>
          </div>

          {/* Submit Button */}
          <button
            type="submit"
            className="w-full bg-[#e67e22] hover:bg-[#d35400] text-white font-semibold py-3.5 rounded-lg transition-colors mt-2"
          >
            Se connecter
          </button>
        </form>

        {/* Divider */}
        <div className="flex items-center my-6">
          <div className="flex-1 border-t border-gray-200"></div>
          <span className="px-4 text-sm text-gray-500">Pas encore de compte ?</span>
          <div className="flex-1 border-t border-gray-200"></div>
        </div>

        {/* Create Account Button */}
        <Link
          href="/inscription"
          className="w-full border border-gray-300 text-[#1e2a4a] font-medium py-3.5 rounded-lg hover:bg-gray-50 transition-colors block text-center"
        >
          Créer un compte
        </Link>

        {/* Security Notice */}
        <div className="flex items-center justify-center gap-2 mt-6 text-gray-400 text-sm">
          <Shield size={16} />
          <span>Connexion sécurisée</span>
        </div>
      </div>
    </div>
  )
}
