package com.polytechnique.ticbnpick.repositories;

import com.polytechnique.ticbnpick.models.Address;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Reactive repository for Address entity.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
public interface AddressRepository extends ReactiveCrudRepository<Address, UUID> {
}