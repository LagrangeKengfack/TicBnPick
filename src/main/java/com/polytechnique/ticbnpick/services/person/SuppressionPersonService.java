package com.polytechnique.ticbnpick.services.person;

import com.polytechnique.ticbnpick.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service specialized in Person deletion.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class SuppressionPersonService {

    private final PersonRepository personRepository;

    public Mono<Void> deleteById(UUID id) {
        // TODO:
        // Purpose: Delete a Person by ID
        // Inputs: UUID id
        // Outputs: Mono<Void>
        // Steps:
        //  1. Call personRepository.deleteById(id)
        // Validations: id not null
        // Errors / Exceptions: None
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: Database delete
        // Security Notes: Ensure caller has admin rights or self-deletion
        return personRepository.deleteById(id);
    }
}
