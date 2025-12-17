package com.polytechnique.ticbnpick.repositories;

import com.polytechnique.ticbnpick.models.Person;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Reactive repository for Person entity.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
public interface PersonRepository extends ReactiveCrudRepository<Person, UUID> {

    /**
     * Finds a person by email.
     *
     * @param email person email
     * @return matching person
     */
    Mono<Person> findByEmail(String email);
}