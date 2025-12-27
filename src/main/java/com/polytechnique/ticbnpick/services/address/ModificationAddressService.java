package com.polytechnique.ticbnpick.services.address;

import com.polytechnique.ticbnpick.models.Address;
import com.polytechnique.ticbnpick.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service specialized in Address updates.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class ModificationAddressService {

    private final AddressRepository addressRepository;

    public Mono<Address> updateAddress(Address address) {
        // TODO:
        // Purpose: Update Address
        // Inputs: Address object
        // Outputs: Mono<Address>
        // Steps:
        //  1. Call addressRepository.save(address)
        // Validations: ID present
        // Errors / Exceptions: OptimisticLockingFailureException
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: Database update
        // Security Notes: Owner or Admin
        return addressRepository.save(address);
    }
}
