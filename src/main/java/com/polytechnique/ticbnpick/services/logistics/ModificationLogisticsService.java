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

    /**
     * Updates an existing Logistics entity in the database.
     *
     * <p>Saves the provided Logistics object, updating the existing record based on ID.
     *
     * @param logistics the Logistics object with updated fields
     * @return a Mono containing the updated Logistics entity
     * @throws org.springframework.dao.OptimisticLockingFailureException if version mismatch occurs
     */
    public Mono<Logistics> updateLogistics(Logistics logistics) {
        return logisticsRepository.save(logistics);
    }
}
