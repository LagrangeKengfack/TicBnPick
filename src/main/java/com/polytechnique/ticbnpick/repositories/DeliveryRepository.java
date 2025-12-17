package com.polytechnique.ticbnpick.repositories;

import com.polytechnique.ticbnpick.models.Delivery;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * Reactive repository for Delivery entity.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
public interface DeliveryRepository extends ReactiveCrudRepository<Delivery, UUID> {

    /**
     * Finds all deliveries assigned to a courier.
     *
     * @param courier_id courier identifier
     * @return list of deliveries
     */
//    Flux<Delivery> findAllByCourierId(UUID courier_id);
}