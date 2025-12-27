package com.polytechnique.ticbnpick.repositories;

import com.polytechnique.ticbnpick.models.Person;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Reactive repository for Person entity.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
public interface PersonRepository extends ReactiveCrudRepository<Person, UUID> {

    /**
     * Finds a person by email.
     *
     * @param email person email
     * @return matching person
     */
    /**
     * Finds a person by email.
     *
     * @param email person email
     * @return matching person
     */
    Mono<Person> findByEmail(String email);

    /**
     * Checks if a person exists by email.
     *
     * @param email person email
     * @return true if exists, false otherwise
     */
    Mono<Boolean> existsByEmail(String email);
}
