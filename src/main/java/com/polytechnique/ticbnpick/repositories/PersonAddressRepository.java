package com.polytechnique.ticbnpick.repositories;

import com.polytechnique.ticbnpick.models.PersonAddress;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Repository for PersonAddress entity operations.
 *
 * @author Kengfack Lagrange
 * @date 21/01/2026
 */
@Repository
public interface PersonAddressRepository extends ReactiveCrudRepository<PersonAddress, UUID> {
    Flux<PersonAddress> findByPersonId(UUID personId);
}
