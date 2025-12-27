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

    public Mono<Void> validate(DeliveryPersonUpdateRequest request) {
        // TODO: Implement validation logic
        return Mono.empty();
    }
}
