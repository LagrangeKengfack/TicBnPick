package com.polytechnique.ticbnpick.repositories;

import com.polytechnique.ticbnpick.models.DeliveryPerson;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * Reactive repository for Courier entity.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
public interface DeliveryPersonRepository extends ReactiveCrudRepository<DeliveryPerson, UUID> {

    /**
     * Finds a courier by associated person id.
     *
     * @param person_id person identifier
     * @return matching courier
     */
    Mono<DeliveryPerson> findByPersonId(UUID person_id);
}