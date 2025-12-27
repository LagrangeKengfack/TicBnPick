package com.polytechnique.ticbnpick.mappers;

import com.polytechnique.ticbnpick.dtos.requests.AddressCreateRequest;
import com.polytechnique.ticbnpick.models.Address;
import org.springframework.stereotype.Component;

/**
 * Mapper for Address entities and DTOs.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Component
public class AddressMapper {

    public Address toEntity(AddressCreateRequest request) {
        // TODO: Map fields from request to new Address entity
        return null;
    }
}
