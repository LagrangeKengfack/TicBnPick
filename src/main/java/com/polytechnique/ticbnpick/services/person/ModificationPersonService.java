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

    /**
     * Updates an existing Person entity in the database.
     *
     * <p>Saves the provided Person object, updating the existing record based on ID.
     *
     * @param person the Person object with updated fields
     * @return a Mono containing the updated Person entity
     * @throws org.springframework.dao.OptimisticLockingFailureException if version mismatch occurs
     */
    public Mono<Person> updatePerson(Person person) {
        return personRepository.save(person);
    }
}
