package com.polytechnique.ticbnpick.mappers;

import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonRegistrationRequest;
import com.polytechnique.ticbnpick.dtos.responses.DeliveryPersonDetailsResponse;
import com.polytechnique.ticbnpick.dtos.responses.DeliveryPersonRegistrationResponse;
import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.models.Person;
import org.springframework.stereotype.Component;

/**
 * Mapper for DeliveryPerson entities and DTOs.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Component
public class DeliveryPersonMapper {

    public Person toPerson(DeliveryPersonRegistrationRequest request) {
        // TODO: Map fields from request to new Person entity (firstName, lastName, email, phone, etc.)
        return null;
    }

    public DeliveryPerson toDeliveryPerson(DeliveryPersonRegistrationRequest request) {
        // TODO: Map fields from request to new DeliveryPerson entity (siret, commercialName, etc.)
        return null;
    }

    public DeliveryPersonRegistrationResponse toRegistrationResponse(DeliveryPerson deliveryPerson, Person person) {
        // TODO: Create response with ID, email, and status from entities
        return null;
    }

    public DeliveryPersonDetailsResponse toDetailsResponse(DeliveryPerson deliveryPerson, Person person) {
        // TODO: Map all details from both entities to the response DTO
        return null;
    }
}
