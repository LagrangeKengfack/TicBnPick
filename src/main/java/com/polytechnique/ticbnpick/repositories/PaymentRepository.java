package com.polytechnique.ticbnpick.repositories;

import com.polytechnique.ticbnpick.models.Payment;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * Reactive repository for Payment entity.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
public interface PaymentRepository extends ReactiveCrudRepository<Payment, UUID> {

    /**
     * Finds a payment by delivery id.
     *
     * @param delivery_id delivery identifier
     * @return matching payment
     */
//    Mono<Payment> findByDeliveryId(UUID delivery_id);
}