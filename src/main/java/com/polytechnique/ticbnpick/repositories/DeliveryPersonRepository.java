package com.polytechnique.ticbnpick.repositories;

import com.polytechnique.ticbnpick.models.DeliveryPerson;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Reactive repository for DeliveryPerson entity.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
public interface DeliveryPersonRepository extends ReactiveCrudRepository<DeliveryPerson, UUID> {

    /**
     * Finds a delivery person by associated person id.
     *
     * @param personId person identifier
     * @return matching delivery person
     */
    Mono<DeliveryPerson> findByPersonId(UUID personId);
}
