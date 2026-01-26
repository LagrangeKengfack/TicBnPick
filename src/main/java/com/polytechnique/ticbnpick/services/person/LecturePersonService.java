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
     * Queries the repository for a Person with the specified ID.
     *
     * @param id the UUID of the person to retrieve
     * @return a Mono containing the Person if found, or empty if not
     */
    public Mono<Person> findById(UUID id) {
        // TODO:
        // Purpose: Retrieve a Person by ID
        // Inputs: UUID id
        // Outputs: Mono<Person>
        // Steps:
        //  1. Call personRepository.findById(id)
        // Validations: id not null
        // Errors / Exceptions: Mono.empty() if not found
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: None
        // Security Notes: None
        return personRepository.findById(id);
    }

    public Mono<Person> findByEmail(String email) {
        // TODO:
        // Purpose: Retrieve a Person by Email
        // Inputs: String email
        // Outputs: Mono<Person>
        // Steps:
        //  1. Call personRepository.findByEmail(email)
        // Validations: email not null/empty
        // Errors / Exceptions: Mono.empty() if not found
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: None
        // Security Notes: None
        return personRepository.findByEmail(email);
    }

    /**
     * Checks if a Person exists with the given email.
     *
     * Uses the repository to verify the existence of the email.
     *
     * @param email the email address to check
     * @return a Mono emitting true if exists, false otherwise
     */
    public Mono<Boolean> existsByEmail(String email) {
        // TODO:
        // Purpose: Check existence of Person by Email
        // Inputs: String email
        // Outputs: Mono<Boolean>
        // Steps:
        //  1. Call personRepository.existsByEmail(email)
        // Validations: email not null
        // Errors / Exceptions: None
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: None
        // Security Notes: None
        return personRepository.existsByEmail(email);
    }
}
