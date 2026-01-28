package com.polytechnique.ticbnpick.repositories;

import com.polytechnique.ticbnpick.models.Logistics;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive repository for Logistics entity.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
public interface LogisticsRepository extends ReactiveCrudRepository<Logistics, UUID> {


    /**
     * Finds a logistics entry by delivery person ID.
     *
     * @param deliveryPersonId the delivery person identifier
     * @return Mono containing the Logistics if found, or empty
     */
    Mono<Logistics> findByDeliveryPersonId(UUID deliveryPersonId);

    /**
     * Finds all logistics entries by delivery person ID.
     *
     * @param deliveryPersonId the delivery person identifier
     * @return Flux of matching Logistics entries
     */
    Flux<Logistics> findAllByDeliveryPersonId(UUID deliveryPersonId);
}
