package com.polytechnique.ticbnpick.services.logistics;

import com.polytechnique.ticbnpick.models.Logistics;
import com.polytechnique.ticbnpick.repositories.LogisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service specialized in Logistics retrieval.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class LectureLogisticsService {

    private final LogisticsRepository logisticsRepository;

    /**
     * Retrieves a Logistics entry by its unique identifier.
     *
     * Queries the repository for a Logistics entry with the specified ID.
     *
     * @param id the UUID of the logistics entry to retrieve
     * @return a Mono containing the Logistics if found, or empty if not
     */
    public Mono<Logistics> findById(UUID id) {
        // TODO:
        // Purpose: Retrieve Logistics by ID
        // Inputs: UUID id
        // Outputs: Mono<Logistics>
        // Steps:
        //  1. Call logisticsRepository.findById(id)
        // Validations: id not null
        // Errors / Exceptions: Mono.empty() if not found
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: None
        // Security Notes: None
        return logisticsRepository.findById(id);
    }

    /**
     * Retrieves all Logistics entries associated with a specific courier.
     *
     * Queries the repository for all logistics entries linked to the courier's ID.
     *
     * @param courierId the UUID of the courier
     * @return a Flux containing the Logistics entries found
     */
    public Flux<Logistics> findAllByCourierId(UUID courierId) {
        // TODO:
        // Purpose: Retrieve all Logistics for a courier
        // Inputs: UUID courierId
        // Outputs: Flux<Logistics>
        // Steps:
        //  1. Call logisticsRepository.findAllByCourierId(courierId)
        // Validations: courierId not null
        // Errors / Exceptions: Empty Flux if none
        // Reactive Flow: Simple Flux pipeline
        // Side Effects: None
        // Security Notes: None
        return logisticsRepository.findAllByCourierId(courierId);
    }
}
