package com.polytechnique.ticbnpick.services.logistics;

import com.polytechnique.ticbnpick.models.Logistics;
import com.polytechnique.ticbnpick.repositories.LogisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service specialized in Logistics creation.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class CreationLogisticsService {

    private final LogisticsRepository logisticsRepository;

    /**
     * Persists a new Logistics entity to the database.
     *
     * <p>Validates the logistics object and saves it to the repository.
     *
     * @param logistics the transient Logistics object to persist
     * @return a Mono containing the persisted Logistics entity
     * @throws org.springframework.dao.DataIntegrityViolationException if data integrity is violated
     */
    public Mono<Logistics> createLogistics(Logistics logistics) {
        return logisticsRepository.save(logistics);
    }
}
