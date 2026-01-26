package com.polytechnique.ticbnpick.services.logistics;

import com.polytechnique.ticbnpick.repositories.LogisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service specialized in Logistics deletion.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class SuppressionLogisticsService {

    private final LogisticsRepository logisticsRepository;

    /**
     * Deletes a Logistics entry by its unique identifier.
     *
     * Removes the Logistics record from the repository.
     *
     * @param id the UUID of the logistics entry to delete
     * @return a Mono<Void> signaling completion
     */
    public Mono<Void> deleteById(UUID id) {
        // TODO:
        // Purpose: Delete Logistics by ID
        // Inputs: UUID id
        // Outputs: Mono<Void>
        // Steps:
        //  1. Call logisticsRepository.deleteById(id)
        // Validations: id not null
        // Errors / Exceptions: None
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: Database delete
        // Security Notes: Owner or Admin
        return logisticsRepository.deleteById(id);
    }
}
