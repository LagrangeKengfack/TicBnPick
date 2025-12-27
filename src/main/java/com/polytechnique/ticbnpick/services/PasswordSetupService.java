package com.polytechnique.ticbnpick.services;

import com.polytechnique.ticbnpick.dtos.requests.SetPasswordRequest;
import com.polytechnique.ticbnpick.services.person.LecturePersonService;
import com.polytechnique.ticbnpick.services.person.ModificationPersonService;
import com.polytechnique.ticbnpick.services.support.PasswordHasherService;
import com.polytechnique.ticbnpick.services.support.TokenService;
import com.polytechnique.ticbnpick.validators.SetPasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Orchestrator service for Password Setup flow.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class PasswordSetupService {

    private final TokenService tokenService;
    private final LecturePersonService lecturePersonService;
    private final ModificationPersonService modificationPersonService;
    private final PasswordHasherService passwordHasherService;
    private final SetPasswordValidator validator;

    /**
     * Sets the password for a user using a valid token.
     *
     * Validates the token, hashes the new password, updates the Person entity,
     * and invalidates the token.
     *
     * @param request the request containing the token and new password
     * @return a Mono<Void> signaling completion
     * @throws com.polytechnique.ticbnpick.exceptions.InvalidTokenException if the token is invalid or expired
     */
    public Mono<Void> setPassword(SetPasswordRequest request) {
        // TODO:
        // Purpose: Set password using a valid token
        // Inputs: SetPasswordRequest (token, newPassword)
        // Outputs: Mono<Void>
        // Steps:
        //  1. validator.validate(request)
        //  2. tokenService.validateToken(request.getToken()) -> Error if invalid
        //  3. Retrieve Token entity to get personId
        //  4. lecturePersonService.findById(personId)
        //  5. Hash new password: passwordHasherService.encode(request.getNewPassword())
        //  6. Update Person: modificationPersonService.updatePerson
        //  7. tokenService.expireToken(request.getToken())
        // Validations: Token valid/active, Password strength
        // Errors / Exceptions: InvalidTokenException, ExpiredTokenException
        // Reactive Flow: flatMap chain
        // Side Effects: DB update (Person password), DB update (Token used)
        // Security Notes: Token usage is one-time only
        return Mono.empty();
    }
}
