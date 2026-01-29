package com.polytechnique.ticbnpick.services.deliveryperson;

import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.repositories.DeliveryPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service specialized in DeliveryPerson retrieval.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class LectureDeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;

    /**
     * Retrieves a DeliveryPerson by their unique identifier.
     *
     * <p>Queries the repository for a DeliveryPerson with the specified ID.
     *
     * @param id the UUID of the delivery person to retrieve
     * @return a Mono containing the DeliveryPerson if found, or empty if not
     */
    public Mono<DeliveryPerson> findById(UUID id) {
        return deliveryPersonRepository.findById(id);
    }

    /**
     * Retrieves a DeliveryPerson by their associated Person ID.
     *
     * <p>Queries the repository for a DeliveryPerson linked to the specified Person ID.
     *
     * @param personId the UUID of the associated Person
     * @return a Mono containing the DeliveryPerson if found, or empty if not
     */
    public Mono<DeliveryPerson> findByPersonId(UUID personId) {
        return deliveryPersonRepository.findByPersonId(personId);
    }
}
