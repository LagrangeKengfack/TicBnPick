package com.polytechnique.ticbnpick.mappers;

import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonRegistrationRequest;
import com.polytechnique.ticbnpick.dtos.responses.DeliveryPersonDetailsResponse;
import com.polytechnique.ticbnpick.dtos.responses.DeliveryPersonRegistrationResponse;
import com.polytechnique.ticbnpick.models.Address;
import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.models.Logistics;
import com.polytechnique.ticbnpick.models.Person;
import com.polytechnique.ticbnpick.models.enums.address.AddressType;
import com.polytechnique.ticbnpick.models.enums.logistics.LogisticsClass;
import com.polytechnique.ticbnpick.models.enums.logistics.LogisticsType;
import org.springframework.stereotype.Component;

/**
 * Mapper for DeliveryPerson entities and DTOs.
 *
 * <p>
 * Provides transformation methods between registration request DTOs
 * and entity objects for Person, DeliveryPerson, Logistics, and Address.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Component
public class DeliveryPersonMapper {

    /**
     * Maps a registration request to a Person entity.
     *
     * @param request the registration request
     * @return a new Person entity (without ID or password)
     */
    public Person toPerson(DeliveryPersonRegistrationRequest request) {
        if (request == null) {
            return null;
        }
        Person person = new Person();
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setEmail(request.getEmail());
        person.setPhone(request.getPhone());
        person.setNationalId(request.getNationalId());
        person.setPhotoCard(request.getPhotoCard());
        person.setNui(request.getNui());
        person.setIsActive(true);
        return person;
    }

    /**
     * Maps a registration request to a DeliveryPerson entity.
     *
     * @param request the registration request
     * @return a new DeliveryPerson entity (without ID or personId)
     */
    public DeliveryPerson toDeliveryPerson(DeliveryPersonRegistrationRequest request) {
        if (request == null) {
            return null;
        }
        DeliveryPerson deliveryPerson = new DeliveryPerson();
        deliveryPerson.setCommercialName(request.getCommercialName());
        deliveryPerson.setCommercialRegister(request.getCommercialRegister());
        deliveryPerson.setSiret(request.getSiret());
        deliveryPerson.setCommissionRate(request.getCommissionRate());
        deliveryPerson.setTaxpayerNumber(request.getNui());
        deliveryPerson.setIsActive(true);
        return deliveryPerson;
    }

    /**
     * Maps a registration request to a Logistics entity.
     *
     * @param request the registration request
     * @return a new Logistics entity (without ID or deliveryPersonId)
     */
    public Logistics toLogistics(DeliveryPersonRegistrationRequest request) {
        if (request == null) {
            return null;
        }
        Logistics logistics = new Logistics();
        logistics.setPlateNumber(request.getPlateNumber());
        if (request.getLogisticsType() != null) {
            logistics.setLogisticsType(LogisticsType.fromValue(request.getLogisticsType()));
        }
        if (request.getLogisticsClass() != null) {
            logistics.setLogisticsClass(LogisticsClass.fromValue(request.getLogisticsClass()));
        }
        if (request.getLogisticsClass() != null) {
            logistics.setLogisticsClass(LogisticsClass.fromValue(request.getLogisticsClass()));
        }
        logistics.setBackPhoto(request.getBackPhoto());
        logistics.setFrontPhoto(request.getFrontPhoto());
        logistics.setTankCapacity(request.getTankCapacity());
        logistics.setLuggageMaxCapacity(request.getLuggageMaxCapacity());
        logistics.setTotalSeatNumber(request.getTotalSeatNumber());
        logistics.setColor(request.getColor());
        return logistics;
    }

    /**
     * Maps a registration request to an Address entity.
     *
     * @param request the registration request
     * @return a new Address entity with PRIMARY type
     */
    public Address toAddress(DeliveryPersonRegistrationRequest request) {
        if (request == null) {
            return null;
        }
        Address address = new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setCountry(request.getCountry());
        address.setDescription(request.getDescription());
        address.setType(AddressType.PRIMARY);
        return address;
    }

    /**
     * Maps a DeliveryPerson and Person to a registration response.
     *
     * @param deliveryPerson the delivery person entity
     * @param person         the person entity (unused but available for future
     *                       expansion)
     * @return the registration response with ID and status
     */
    public DeliveryPersonRegistrationResponse toRegistrationResponse(DeliveryPerson deliveryPerson, Person person) {
        if (deliveryPerson == null) {
            return null;
        }
        return new DeliveryPersonRegistrationResponse(
                deliveryPerson.getId(),
                deliveryPerson.getStatus() != null ? deliveryPerson.getStatus().toString() : null);
    }

    /**
     * Maps a DeliveryPerson and Person to a details response.
     *
     * @param deliveryPerson the delivery person entity
     * @param person         the person entity
     * @return the detailed response DTO with all relevant fields
     */
    public DeliveryPersonDetailsResponse toDetailsResponse(DeliveryPerson deliveryPerson, Person person) {
        if (deliveryPerson == null || person == null) {
            return null;
        }
        DeliveryPersonDetailsResponse response = new DeliveryPersonDetailsResponse();
        response.setId(deliveryPerson.getId());
        response.setFirstName(person.getFirstName());
        response.setLastName(person.getLastName());
        response.setEmail(person.getEmail());
        response.setPhone(person.getPhone());
        response.setStatus(deliveryPerson.getStatus() != null ? deliveryPerson.getStatus().getValue() : null);
        response.setCommercialName(deliveryPerson.getCommercialName());
        return response;
    }
}
