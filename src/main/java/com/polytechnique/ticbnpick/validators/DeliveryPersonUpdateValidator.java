package com.polytechnique.ticbnpick.validators;

import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonUpdateRequest;
import com.polytechnique.ticbnpick.exceptions.ValidationException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

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
     * @return a Mono containing the valid request, or error if invalid
     * @throws ValidationException if validation fails
     */
    public Mono<DeliveryPersonUpdateRequest> validate(DeliveryPersonUpdateRequest request) {
        List<String> errors = new ArrayList<>();

        if (request == null) {
            return Mono.error(new ValidationException("Update request cannot be null"));
        }

        // At least one field should be provided for update
        boolean hasAnyField = request.getPhone() != null ||
                request.getCommercialName() != null ||
                request.getCommercialRegister() != null ||
                request.getLogisticsType() != null ||
                request.getBackPhoto() != null ||
                request.getFrontPhoto() != null ||
                request.getPlateNumber() != null ||
                request.getLogisticsClass() != null ||
                request.getColor() != null ||
                request.getTankCapacity() != null ||
                request.getLuggageMaxCapacity() != null ||
                request.getTotalSeatNumber() != null ||
                request.getStreet() != null ||
                request.getCity() != null ||
                request.getDistrict() != null ||
                request.getCountry() != null ||
                request.getDescription() != null;

        if (!hasAnyField) {
            errors.add("At least one field must be provided for update");
        }

        // Return validation result
        if (!errors.isEmpty()) {
            return Mono.error(new ValidationException(String.join(", ", errors)));
        }

        return Mono.just(request);
    }
}
