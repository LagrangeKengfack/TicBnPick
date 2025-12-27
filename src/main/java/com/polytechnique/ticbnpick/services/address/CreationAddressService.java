package com.polytechnique.ticbnpick.services.address;

import com.polytechnique.ticbnpick.models.Address;
import com.polytechnique.ticbnpick.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service specialized in Address creation.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class CreationAddressService {

    private final AddressRepository addressRepository;

    public Mono<Address> createAddress(Address address) {
        // TODO:
        // Purpose: Create new Address
        // Inputs: Address object
        // Outputs: Mono<Address>
        // Steps:
        //  1. Call addressRepository.save(address)
        // Validations: Mandatory fields
        // Errors / Exceptions: DataIntegrityViolationException
        // Reactive Flow: Simple Mono pipeline
        // Side Effects: Database insert
        // Security Notes: None
        return addressRepository.save(address);
    }
}
