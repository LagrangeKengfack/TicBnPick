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
