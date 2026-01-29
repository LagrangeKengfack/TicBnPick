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

    /**
     * Persists a new Address entity to the database.
     *
     * <p>Validates the address object and saves it to the repository.
     *
     * @param address the transient Address object to persist
     * @return a Mono containing the persisted Address entity
     * @throws org.springframework.dao.DataIntegrityViolationException if data integrity is violated
     */
    public Mono<Address> createAddress(Address address) {
        return addressRepository.save(address);
    }
}
