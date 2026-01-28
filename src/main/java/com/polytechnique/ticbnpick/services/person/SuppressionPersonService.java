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

    /**
     * Deletes a Person by their unique identifier.
     *
     * <p>Removes the Person record from the repository.
     *
     * @param id the UUID of the person to delete
     * @return a Mono&lt;Void&gt; signaling completion
     */
    public Mono<Void> deleteById(UUID id) {
        return personRepository.deleteById(id);
    }
}
