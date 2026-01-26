package com.polytechnique.ticbnpick.services.deliveryperson;

import com.polytechnique.ticbnpick.repositories.DeliveryPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service specialized in DeliveryPerson deletion.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class SuppressionDeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;

    /**
     * Deletes a DeliveryPerson by their unique identifier.
     *
     * Removes the DeliveryPerson record from the repository.
     *
     * @param id the UUID of the delivery person to delete
     * @return a Mono<Void> signaling completion
     */
    public Mono<Void> deleteById(UUID id) {
        // TODO:
        // Purpose: Delete DeliveryPerson by ID
        // Inputs: UUID id
        // Outputs: Mono<Void>
        // Steps:
        //  1. Call deliveryPersonRepository.deleteById(id)
        // Validations: id not null
        // Errors / Exceptions: None
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: Database delete
        // Security Notes: Admin only
        return deliveryPersonRepository.deleteById(id);
    }
}
