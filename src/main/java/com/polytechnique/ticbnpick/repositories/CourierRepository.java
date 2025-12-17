package com.polytechnique.ticbnpick.repositories;

import com.polytechnique.ticbnpick.models.Courier;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * Reactive repository for Courier entity.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
public interface CourierRepository extends ReactiveCrudRepository<Courier, UUID> {

    /**
     * Finds a courier by associated person id.
     *
     * @param person_id person identifier
     * @return matching courier
     */
    Mono<Courier> findByPersonId(UUID person_id);
}