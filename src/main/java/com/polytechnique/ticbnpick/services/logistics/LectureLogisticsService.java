package com.polytechnique.ticbnpick.services.logistics;

import com.polytechnique.ticbnpick.models.Logistics;
import com.polytechnique.ticbnpick.repositories.LogisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service specialized in Logistics retrieval.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class LectureLogisticsService {

    private final LogisticsRepository logisticsRepository;

    /**
     * Retrieves a Logistics entry by its unique identifier.
     *
     * Queries the repository for a Logistics entry with the specified ID.
     *
     * @param id the UUID of the logistics entry to retrieve
     * @return a Mono containing the Logistics if found, or empty if not
     */
    public Mono<Logistics> findById(UUID id) {
        return logisticsRepository.findById(id);
    }

    /**
     * Retrieves all Logistics entries associated with a specific courier.
     *
     * @param courierId the UUID of the courier
     * @return a Flux containing the Logistics entries found
     * @deprecated Use {@link #findAllByDeliveryPersonId(UUID)} instead
     */
    @Deprecated
    public Flux<Logistics> findAllByCourierId(UUID courierId) {
        return logisticsRepository.findAllByCourierId(courierId);
    }

    /**
     * Retrieves a Logistics entry by delivery person ID.
     *
     * @param deliveryPersonId the UUID of the delivery person
     * @return a Mono containing the Logistics if found, or empty if not
     */
    public Mono<Logistics> findByDeliveryPersonId(UUID deliveryPersonId) {
        return logisticsRepository.findByDeliveryPersonId(deliveryPersonId);
    }

    /**
     * Retrieves all Logistics entries for a delivery person.
     *
     * @param deliveryPersonId the UUID of the delivery person
     * @return a Flux containing all Logistics entries
     */
    public Flux<Logistics> findAllByDeliveryPersonId(UUID deliveryPersonId) {
        return logisticsRepository.findAllByDeliveryPersonId(deliveryPersonId);
    }
}
