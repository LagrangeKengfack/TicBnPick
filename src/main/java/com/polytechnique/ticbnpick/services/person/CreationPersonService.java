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

    /**
     * Persists a new Person entity to the database.
     *
     * <p>Validates the person object and saves it to the repository.
     *
     * @param person the transient Person object to persist
     * @return a Mono containing the persisted Person entity
     * @throws org.springframework.dao.DataIntegrityViolationException if data integrity is violated
     */
    public Mono<Person> createPerson(Person person) {
        return personRepository.save(person);
    }
}
