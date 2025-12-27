package com.polytechnique.ticbnpick.repositories;

import com.polytechnique.ticbnpick.models.PendingDeliveryPersonUpdate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * Reactive repository for PendingDeliveryPersonUpdate entity.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
public interface PendingDeliveryPersonUpdateRepository extends ReactiveCrudRepository<PendingDeliveryPersonUpdate, UUID> {

    /**
     * Finds all pending updates by status.
     *
     * @param status the status string
     * @return flux of pending updates
     */
    Flux<PendingDeliveryPersonUpdate> findByStatus(String status);
}
