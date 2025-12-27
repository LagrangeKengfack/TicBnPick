package com.polytechnique.ticbnpick.services.person;

import com.polytechnique.ticbnpick.models.Person;
import com.polytechnique.ticbnpick.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service specialized in Person updates.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class ModificationPersonService {

    private final PersonRepository personRepository;

    public Mono<Person> updatePerson(Person person) {
        // TODO:
        // Purpose: Update an existing Person entity
        // Inputs: Person object (managed, with ID)
        // Outputs: Mono<Person> (updated)
        // Steps:
        //  1. Validate ID exists in Person object
        //  2. Call personRepository.save(person) (r2dbc save acts as update if ID present)
        // Validations: ID not null
        // Errors / Exceptions: OptimisticLockingFailureException if version mismatch (if configured)
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: Database update
        // Security Notes: Ensure caller is authorized
        return personRepository.save(person);
    }
}
