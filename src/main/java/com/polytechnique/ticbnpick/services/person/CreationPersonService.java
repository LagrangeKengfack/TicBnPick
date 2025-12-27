package com.polytechnique.ticbnpick.services.person;

import com.polytechnique.ticbnpick.models.Person;
import com.polytechnique.ticbnpick.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service specialized in Person creation.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class CreationPersonService {

    private final PersonRepository personRepository;

    public Mono<Person> createPerson(Person person) {
        // TODO:
        // Purpose: Persist a new Person entity to the database
        // Inputs: Person object (transient)
        // Outputs: Mono<Person> (persisted with ID)
        // Steps:
        //  1. Validate Person object (basic checks)
        //  2. Check if email already exists (should be handled before or catch DuplicateKeyException)
        //  3. Call personRepository.save(person)
        // Validations: Non-null fields, email format
        // Errors / Exceptions: DataIntegrityViolationException if email duplicate
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: Database insert
        // Security Notes: None
        return personRepository.save(person);
    }
}
