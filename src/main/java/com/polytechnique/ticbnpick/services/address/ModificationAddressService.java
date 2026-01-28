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

    /**
     * Updates an existing Address entity in the database.
     *
     * <p>Saves the provided Address object, updating the existing record based on ID.
     *
     * @param address the Address object with updated fields
     * @return a Mono containing the updated Address entity
     * @throws org.springframework.dao.OptimisticLockingFailureException if version mismatch occurs
     */
    public Mono<Address> updateAddress(Address address) {
        return addressRepository.save(address);
    }
}
