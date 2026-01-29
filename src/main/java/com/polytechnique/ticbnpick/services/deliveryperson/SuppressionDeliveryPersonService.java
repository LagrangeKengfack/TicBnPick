package com.polytechnique.ticbnpick.services.deliveryperson;

import com.polytechnique.ticbnpick.repositories.DeliveryPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service specialized in DeliveryPerson deletion.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class SuppressionDeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;

    /**
     * Deletes a DeliveryPerson by their unique identifier.
     *
     * <p>Removes the DeliveryPerson record from the repository.
     *
     * @param id the UUID of the delivery person to delete
     * @return a Mono&lt;Void&gt; signaling completion
     */
    public Mono<Void> deleteById(UUID id) {
        return deliveryPersonRepository.deleteById(id);
    }
}
