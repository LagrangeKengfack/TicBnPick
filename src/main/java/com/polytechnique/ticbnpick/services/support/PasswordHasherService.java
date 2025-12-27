package com.polytechnique.ticbnpick.services.support;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for password hashing.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
public class PasswordHasherService {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encode(String rawPassword) {
        // TODO:
        // Purpose: Hash password with BCrypt
        // Inputs: raw password string
        // Outputs: hashed string
        // Steps:
        //  1. passwordEncoder.encode(rawPassword)
        // Validations: rawPassword not null
        // Errors / Exceptions: None
        // Reactive Flow: Synchronous
        // Side Effects: None
        // Security Notes: BCrypt strength defaults
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Verifies if a raw password matches the encoded password.
     *
     * Uses BCrypt's secure comparison to check validity.
     *
     * @param rawPassword the plain text password to check
     * @param encodedPassword the stored hashed password
     * @return true if passwords match, false otherwise
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        // TODO:
        // Purpose: Verify password match
        // Inputs: raw password, hashed password
        // Outputs: boolean match result
        // Steps:
        //  1. passwordEncoder.matches(rawPassword, encodedPassword)
        // Validations: inputs not null
        // Errors / Exceptions: None
        // Reactive Flow: Synchronous
        // Side Effects: None
        // Security Notes: Timing attack safe comparison
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
