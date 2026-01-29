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
     * <p>Removes the Logistics record from the repository.
     *
     * @param id the UUID of the logistics entry to delete
     * @return a Mono&lt;Void&gt; signaling completion
     */
    public Mono<Void> deleteById(UUID id) {
        return logisticsRepository.deleteById(id);
    }
}
