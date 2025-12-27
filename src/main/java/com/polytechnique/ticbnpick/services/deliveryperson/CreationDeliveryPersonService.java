package com.polytechnique.ticbnpick.services.deliveryperson;

import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.repositories.DeliveryPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service specialized in DeliveryPerson creation.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class CreationDeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;

    public Mono<DeliveryPerson> createDeliveryPerson(DeliveryPerson deliveryPerson) {
        // TODO:
        // Purpose: Create a new DeliveryPerson profile
        // Inputs: DeliveryPerson object (transient)
        // Outputs: Mono<DeliveryPerson> (persisted)
        // Steps:
        //  1. Validate mandatory fields (personId, etc.)
        //  2. Call deliveryPersonRepository.save(deliveryPerson)
        // Validations: personId must reference existing Person
        // Errors / Exceptions: DataIntegrityViolationException
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: Database insert
        // Security Notes: None
        return deliveryPersonRepository.save(deliveryPerson);
    }
}
