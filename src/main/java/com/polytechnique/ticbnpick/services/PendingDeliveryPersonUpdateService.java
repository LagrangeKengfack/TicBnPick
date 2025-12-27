package com.polytechnique.ticbnpick.services;

import com.polytechnique.ticbnpick.models.PendingDeliveryPersonUpdate;
import com.polytechnique.ticbnpick.repositories.PendingDeliveryPersonUpdateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * CRUD Service for PendingDeliveryPersonUpdate.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class PendingDeliveryPersonUpdateService {

    private final PendingDeliveryPersonUpdateRepository repository;

    public Mono<PendingDeliveryPersonUpdate> save(PendingDeliveryPersonUpdate update) {
        // TODO:
        // Purpose: Save a pending update request
        // Inputs: PendingDeliveryPersonUpdate entity
        // Outputs: Mono<PendingDeliveryPersonUpdate> (saved entity)
        // Steps:
        //  1. Validate update object
        //  2. repository.save(update)
        // Validations: Non-null fields
        // Errors / Exceptions: Database exceptions
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: Database insert/update
        // Security Notes: None
        return repository.save(update);
    }

    public Mono<PendingDeliveryPersonUpdate> findById(UUID id) {
        // TODO:
        // Purpose: Retrieve a pending update by ID
        // Inputs: UUID id
        // Outputs: Mono<PendingDeliveryPersonUpdate>
        // Steps:
        //  1. repository.findById(id)
        // Validations: None
        // Errors / Exceptions: Mono.empty() if not found
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: None
        // Security Notes: None
        return repository.findById(id);
    }

    /**
     * Retrieves all pending updates with a specific status.
     *
     * @param status the status string to filter by (e.g., "PENDING")
     * @return a Flux of matching PendingDeliveryPersonUpdate entities
     */
    public Flux<PendingDeliveryPersonUpdate> findByStatus(String status) {
        // TODO:
        // Purpose: Retrieve pending updates by status
        // Inputs: String status
        // Outputs: Flux<PendingDeliveryPersonUpdate>
        // Steps:
        //  1. repository.findByStatus(status)
        // Validations: None
        // Errors / Exceptions: None
        // Reactive Flow: Simple Flux pipeline
        // Side Effects: None
        // Security Notes: None
        return repository.findByStatus(status);
    }

    /**
     * Deletes a pending update by its ID.
     *
     * Used when an update is approved/applied or rejected/discarded.
     *
     * @param id the UUID of the update to delete
     * @return a Mono<Void> signaling completion
     */
    public Mono<Void> deleteById(UUID id) {
        // TODO:
        // Purpose: Delete a pending update by ID
        // Inputs: UUID id
        // Outputs: Mono<Void>
        // Steps:
        //  1. repository.deleteById(id)
        // Validations: None
        // Errors / Exceptions: None
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: Database deletion
        // Security Notes: None
        return repository.deleteById(id);
    }
}
