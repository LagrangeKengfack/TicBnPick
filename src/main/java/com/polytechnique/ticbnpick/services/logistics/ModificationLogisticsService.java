package com.polytechnique.ticbnpick.services.logistics;

import com.polytechnique.ticbnpick.models.Logistics;
import com.polytechnique.ticbnpick.repositories.LogisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service specialized in Logistics updates.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class ModificationLogisticsService {

    private final LogisticsRepository logisticsRepository;

    public Mono<Logistics> updateLogistics(Logistics logistics) {
        // TODO:
        // Purpose: Update Logistics entry
        // Inputs: Logistics object
        // Outputs: Mono<Logistics>
        // Steps:
        //  1. Call logisticsRepository.save(logistics)
        // Validations: ID present
        // Errors / Exceptions: OptimisticLockingFailureException
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: Database update
        // Security Notes: Owner or Admin
        return logisticsRepository.save(logistics);
    }
}
