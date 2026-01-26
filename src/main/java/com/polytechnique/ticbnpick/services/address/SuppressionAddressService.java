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
     * Removes the Address record from the repository.
     *
     * @param id the UUID of the address to delete
     * @return a Mono<Void> signaling completion
     */
    public Mono<Void> deleteById(UUID id) {
        // TODO:
        // Purpose: Delete Address by ID
        // Inputs: UUID id
        // Outputs: Mono<Void>
        // Steps:
        //  1. Call addressRepository.deleteById(id)
        // Validations: id not null
        // Errors / Exceptions: None
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: Database delete
        // Security Notes: Owner or Admin
        return addressRepository.deleteById(id);
    }
}
