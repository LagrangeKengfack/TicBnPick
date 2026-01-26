package com.polytechnique.ticbnpick.validators;

import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonUpdateRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Validator for delivery person update.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Component
public class DeliveryPersonUpdateValidator {

    /**
     * Validates a delivery person update request.
     *
     * Ensures that the requested updates comply with business rules.
     *
     * @param request the update request to validate
     * @return a Mono<Void> that completes empty if valid, or signals error
     */
    public Mono<Void> validate(DeliveryPersonUpdateRequest request) {
        // TODO: Implement validation logic
        return Mono.empty();
    }
}
