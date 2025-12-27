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

    public Mono<Void> validate(DeliveryPersonRegistrationRequest request) {
        // TODO: Implement validation logic (e.g., check if email is valid, fields are not empty)
        // Return Mono.error(new ValidationException(...)) if invalid, else Mono.empty()
        return Mono.empty();
    }
}
