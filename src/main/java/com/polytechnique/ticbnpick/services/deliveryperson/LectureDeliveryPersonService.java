package com.polytechnique.ticbnpick.services.deliveryperson;

import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.repositories.DeliveryPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service specialized in DeliveryPerson retrieval.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class LectureDeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;

    public Mono<DeliveryPerson> findById(UUID id) {
        // TODO:
        // Purpose: Retrieve DeliveryPerson by ID
        // Inputs: UUID id
        // Outputs: Mono<DeliveryPerson>
        // Steps:
        //  1. Call deliveryPersonRepository.findById(id)
        // Validations: id not null
        // Errors / Exceptions: Mono.empty() if not found
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: None
        // Security Notes: None
        return deliveryPersonRepository.findById(id);
    }

    public Mono<DeliveryPerson> findByPersonId(UUID personId) {
        // TODO:
        // Purpose: Retrieve DeliveryPerson by Person ID
        // Inputs: UUID personId
        // Outputs: Mono<DeliveryPerson>
        // Steps:
        //  1. Call deliveryPersonRepository.findByPersonId(personId)
        // Validations: personId not null
        // Errors / Exceptions: Mono.empty() if not found
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: None
        // Security Notes: None
        return deliveryPersonRepository.findByPersonId(personId);
    }
}
