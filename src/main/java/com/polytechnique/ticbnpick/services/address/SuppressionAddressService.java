package com.polytechnique.ticbnpick.services.address;

import com.polytechnique.ticbnpick.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service specialized in Address deletion.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class SuppressionAddressService {

    private final AddressRepository addressRepository;

    /**
     * Deletes an Address by its unique identifier.
     *
     * <p>Removes the Address record from the repository.
     *
     * @param id the UUID of the address to delete
     * @return a Mono&lt;Void&gt; signaling completion
     */
    public Mono<Void> deleteById(UUID id) {
        return addressRepository.deleteById(id);
    }
}
