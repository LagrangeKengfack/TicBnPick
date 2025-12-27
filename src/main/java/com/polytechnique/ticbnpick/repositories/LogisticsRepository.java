package com.polytechnique.ticbnpick.repositories;

import com.polytechnique.ticbnpick.models.Logistics;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * Reactive repository for Logistics entity.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
public interface LogisticsRepository extends ReactiveCrudRepository<Logistics, UUID> {

    Flux<Logistics> findAllByCourierId(UUID courierId);
}
