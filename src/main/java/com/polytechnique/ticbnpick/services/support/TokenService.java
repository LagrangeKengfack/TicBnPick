package com.polytechnique.ticbnpick.services.support;

import com.polytechnique.ticbnpick.models.PasswordToken;
import com.polytechnique.ticbnpick.repositories.PasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service for token management.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class TokenService {

    private final PasswordTokenRepository passwordTokenRepository;

    /**
     * Generates a new password reset token for a user.
     *
     * Creates a secure random token, persists it with an expiration time,
     * and associates it with the given person ID.
     *
     * @param personId the UUID of the person requesting the token
     * @return a Mono containing the generated PasswordToken entity
     */
    public Mono<PasswordToken> generateToken(UUID personId) {
        // TODO:
        // Purpose: Generate new password reset token
        // Inputs: Person UUID
        // Outputs: Mono<PasswordToken>
        // Steps:
        //  1. Create random string token (UUID or SecureRandom)
        //  2. Create PasswordToken entity with expiry (48h)
        //  3. Save to repository
        // Validations: personId exists
        // Errors / Exceptions: None
        // Reactive Flow: Mono pipeline
        // Side Effects: Database insert
        // Security Notes: Token entropy high
        return Mono.empty();
    }

    public Mono<Boolean> validateToken(String token) {
        // TODO:
        // Purpose: Validate token validity
        // Inputs: token string
        // Outputs: Mono<Boolean>
        // Steps:
        //  1. Find token in repo
        //  2. Check if found
        //  3. Check expiry > now
        //  4. Check used == false
        // Validations: token not empty
        // Errors / Exceptions: None
        // Reactive Flow: Mono pipeline
        // Side Effects: None
        // Security Notes: None
        return Mono.just(false);
    }

    /**
     * Marks a token as used/expired.
     *
     * Updates the token status in the database to prevent reuse.
     * Should be called immediately after a successful password reset.
     *
     * @param token the token string to expire
     * @return a Mono<Void> signaling completion
     * @throws com.polytechnique.ticbnpick.exceptions.InvalidTokenException if token not found
     */
    public Mono<Void> expireToken(String token) {
        // TODO:
        // Purpose: Mark token as used
        // Inputs: token string
        // Outputs: Mono<Void>
        // Steps:
        //  1. Find token
        //  2. Set used = true
        //  3. Save
        // Validations: token exists
        // Errors / Exceptions: InvalidTokenException
        // Reactive Flow: Mono pipeline
        // Side Effects: Database update
        // Security Notes: None
        return Mono.empty();
    }
}
