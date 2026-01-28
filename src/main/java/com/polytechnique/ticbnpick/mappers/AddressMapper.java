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

    /**
     * Maps an AddressCreateRequest to an Address entity.
     *
     * @param request the DTO to map from
     * @return the mapped Address entity
     */
    public Address toEntity(AddressCreateRequest request) {
        if (request == null) {
            return null;
        }
        Address address = new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setType(request.getType());
        return address;
    }
}
