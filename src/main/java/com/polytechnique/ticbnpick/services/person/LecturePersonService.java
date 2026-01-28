package com.polytechnique.ticbnpick.services.person;

import com.polytechnique.ticbnpick.models.Person;
import com.polytechnique.ticbnpick.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service specialized in Person retrieval.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class LecturePersonService {

    private final PersonRepository personRepository;

    /**
     * Retrieves a Person by their unique identifier.
     *
     * <p>Queries the repository for a Person with the specified ID.
     *
     * @param id the UUID of the person to retrieve
     * @return a Mono containing the Person if found, or empty if not
     */
    public Mono<Person> findById(UUID id) {
        return personRepository.findById(id);
    }

    /**
     * Retrieves a Person by their email address.
     *
     * <p>Queries the repository for a Person with the specified email.
     *
     * @param email the email address to search for
     * @return a Mono containing the Person if found, or empty if not
     */
    public Mono<Person> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    /**
     * Checks if a Person exists with the given email.
     *
     * <p>Uses the repository to verify the existence of the email.
     *
     * @param email the email address to check
     * @return a Mono emitting true if exists, false otherwise
     */
    public Mono<Boolean> existsByEmail(String email) {
        return personRepository.existsByEmail(email);
    }
}
