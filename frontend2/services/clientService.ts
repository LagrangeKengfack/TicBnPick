import axios from 'axios';

// Use relative path to leverage Next.js rewrites (see next.config.mjs)
// This allows the frontend to be accessed from other devices (like a phone)
// while correctly proxying requests to the backend (e.g. localhost:8081).
const API_URL = '/api/clients';

export interface ClientDTO {
    lastName: string;
    firstName: string;
    phone: string;
    email: string;
    password: string;
    nationalId: string;
    photoCard: string;
    criminalRecord: string;
    loyaltyStatus: string;
}

export const checkEmail = async (email: string): Promise<boolean> => {
    try {
        const response = await axios.get(`${API_URL}/check-email`, {
            params: { email }
        });
        return response.data;
    } catch (error) {
        console.error('Error checking email:', error);
        throw error;
    }
};

export const checkNationalId = async (nationalId: string): Promise<boolean> => {
    try {
        const response = await axios.get(`${API_URL}/check-national-id`, {
            params: { nationalId }
        });
        return response.data;
    } catch (error) {
        console.error('Error checking nationalId:', error);
        throw error;
    }
};

export const createClient = async (clientData: any) => {
    try {
        // Mapping to ClientDTO.java
        // Note: ClientDTO requires nationalId, photoCard, loyaltyStatus which are not in the Client form.
        // We will send placeholders for now or map if available.
        // We'll use defaults to pass validation if they are missing.

        const payload: ClientDTO = {
            lastName: clientData.nom,
            firstName: clientData.prenom,
            phone: clientData.telephone,
            email: clientData.email,
            password: clientData.motDePasse,
            // Required by Backend DTO but not in Client Form
            nationalId: clientData.numeroCNI,
            photoCard: "NOT_PROVIDED", // Placeholder
            criminalRecord: "CLEAN", // Placeholder
            loyaltyStatus: "REGULAR" // Default
        };

        const response = await axios.post(API_URL, payload);
        return response.data;
    } catch (error) {
        console.error('Error creating client:', error);
        throw error;
    }
};
