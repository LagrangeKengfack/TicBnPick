package com.polytechnique.ticbnpick.validators;

import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonRegistrationRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Validator for delivery person registration.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Component
public class DeliveryPersonRegistrationValidator {

    /**
     * Validates a delivery person registration request.
     *
     * Checks for data integrity, required fields, and format constraints.
     *
     * @param request the registration request to validate
     * @return a Mono<Void> that completes empty if valid, or signals error
     */
    public Mono<Void> validate(DeliveryPersonRegistrationRequest request) {
        // TODO: Implement validation logic (e.g., check if email is valid, fields are not empty)
        // Return Mono.error(new ValidationException(...)) if invalid, else Mono.empty()
        return Mono.empty();
    }
}
