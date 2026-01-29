package com.polytechnique.ticbnpick.services.deliveryperson;

import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.repositories.DeliveryPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service specialized in DeliveryPerson creation.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class CreationDeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;

    /**
     * Persists a new DeliveryPerson entity to the database.
     *
     * <p>Validates the delivery person object and saves it to the repository.
     *
     * @param deliveryPerson the transient DeliveryPerson object to persist
     * @return a Mono containing the persisted DeliveryPerson entity
     * @throws org.springframework.dao.DataIntegrityViolationException if data integrity is violated
     */
    public Mono<DeliveryPerson> createDeliveryPerson(DeliveryPerson deliveryPerson) {
        return deliveryPersonRepository.save(deliveryPerson);
    }
}
