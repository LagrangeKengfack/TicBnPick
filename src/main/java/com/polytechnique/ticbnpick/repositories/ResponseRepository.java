package com.polytechnique.ticbnpick.repositories;

import com.polytechnique.ticbnpick.models.Response;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * Reactive repository for Response entity.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
public interface ResponseRepository extends ReactiveCrudRepository<Response, UUID> {

    Flux<Response> findAllByAnnouncementId(UUID announcement_id);

    Flux<Response> findAllByCourierId(UUID courier_id);
}