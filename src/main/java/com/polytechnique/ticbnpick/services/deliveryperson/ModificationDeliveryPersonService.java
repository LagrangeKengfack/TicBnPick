package com.polytechnique.ticbnpick.services.deliveryperson;

import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.repositories.DeliveryPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service specialized in DeliveryPerson updates.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class ModificationDeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;

    /**
     * Updates an existing DeliveryPerson entity in the database.
     *
     * <p>Saves the provided DeliveryPerson object, updating the existing record based on ID.
     *
     * @param deliveryPerson the DeliveryPerson object with updated fields
     * @return a Mono containing the updated DeliveryPerson entity
     * @throws org.springframework.dao.OptimisticLockingFailureException if version mismatch occurs
     */
    public Mono<DeliveryPerson> updateDeliveryPerson(DeliveryPerson deliveryPerson) {
        return deliveryPersonRepository.save(deliveryPerson);
    }
}
