package com.polytechnique.ticbnpick.services;

import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonUpdateRequest;
import com.polytechnique.ticbnpick.models.Address;
import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.models.PersonAddress;
import com.polytechnique.ticbnpick.models.enums.logistics.LogisticsClass;
import com.polytechnique.ticbnpick.models.enums.logistics.LogisticsType;
import com.polytechnique.ticbnpick.repositories.AddressRepository;
import com.polytechnique.ticbnpick.repositories.PersonAddressRepository;
import com.polytechnique.ticbnpick.services.address.CreationAddressService;
import com.polytechnique.ticbnpick.services.address.ModificationAddressService;
import com.polytechnique.ticbnpick.services.deliveryperson.LectureDeliveryPersonService;
import com.polytechnique.ticbnpick.services.deliveryperson.ModificationDeliveryPersonService;
import com.polytechnique.ticbnpick.services.logistics.LectureLogisticsService;
import com.polytechnique.ticbnpick.services.logistics.ModificationLogisticsService;
import com.polytechnique.ticbnpick.services.person.LecturePersonService;
import com.polytechnique.ticbnpick.services.person.ModificationPersonService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service to handle delivery person profile updates.
 *
 * <p>
 * Orchestrates updates to DeliveryPerson, Person, and Logistics entities
 * based on the fields provided in the update request. All updates are applied
 * directly without admin validation.
 *
 * @author Kengfack Lagrange
 * @date 25/01/2026
 */
@Service
@RequiredArgsConstructor
public class DeliveryPersonProfileService {

    private final LectureDeliveryPersonService lectureDeliveryPersonService;
    private final ModificationDeliveryPersonService modificationDeliveryPersonService;
    private final LectureLogisticsService lectureLogisticsService;
    private final ModificationLogisticsService modificationLogisticsService;
    private final LecturePersonService lecturePersonService;
    private final ModificationPersonService modificationPersonService;
    private final PersonAddressRepository personAddressRepository;
    private final AddressRepository addressRepository;
    private final CreationAddressService creationAddressService;
    private final ModificationAddressService modificationAddressService;

    /**
     * Updates a delivery person's profile with the provided data.
     *
     * <p>
     * Applies all provided field updates directly to the corresponding entities:
     * <ul>
     * <li>Person fields: phone</li>
     * <li>DeliveryPerson fields: commercialName, commercialRegister</li>
     * <li>Logistics fields: plateNumber, color, logisticsClass, logisticsType,
     * logisticImage, etc.</li>
     * </ul>
     *
     * @param deliveryPersonId the UUID of the delivery person to update
     * @param request          the update request containing the fields to modify
     * @return a Mono&lt;Void&gt; signaling completion
     */
    public Mono<Void> updateProfile(UUID deliveryPersonId, DeliveryPersonUpdateRequest request) {
        return lectureDeliveryPersonService.findById(deliveryPersonId)
                .flatMap(dp -> {
                    Mono<Void> updatePerson = updatePersonFields(dp, request);
                    Mono<Void> updateDeliveryPerson = updateDeliveryPersonFields(dp, request);
                    Mono<Void> updateLogistics = updateLogisticsFields(dp.getId(), request);
                    Mono<Void> updateAddress = updateAddressFields(dp.getPersonId(), request);

                    return Mono.when(updatePerson, updateDeliveryPerson, updateLogistics, updateAddress);
                })
                .switchIfEmpty(Mono.error(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Delivery Person not found")));
    }

    /**
     * Updates Person entity fields from the request.
     *
     * @param dp      the delivery person entity (for personId)
     * @param request the update request
     * @return a Mono&lt;Void&gt; signaling completion
     */
    private Mono<Void> updatePersonFields(DeliveryPerson dp, DeliveryPersonUpdateRequest request) {
        return lecturePersonService.findById(dp.getPersonId())
                .flatMap(person -> {
                    boolean changed = false;
                    if (request.getPhone() != null) {
                        person.setPhone(request.getPhone());
                        changed = true;
                    }

                    if (changed) {
                        return modificationPersonService.updatePerson(person).then();
                    }
                    return Mono.empty();
                });
    }

    /**
     * Updates DeliveryPerson entity fields from the request.
     *
     * @param dp      the delivery person entity
     * @param request the update request
     * @return a Mono&lt;Void&gt; signaling completion
     */
    private Mono<Void> updateDeliveryPersonFields(DeliveryPerson dp, DeliveryPersonUpdateRequest request) {
        boolean changed = false;

        if (request.getCommercialName() != null) {
            dp.setCommercialName(request.getCommercialName());
            changed = true;
        }
        if (request.getCommercialRegister() != null) {
            dp.setCommercialRegister(request.getCommercialRegister());
            changed = true;
        }

        if (changed) {
            return modificationDeliveryPersonService.updateDeliveryPerson(dp).then();
        }
        return Mono.empty();
    }

    /**
     * Updates Logistics entity fields from the request.
     *
     * @param deliveryPersonId the UUID of the delivery person
     * @param request          the update request
     * @return a Mono&lt;Void&gt; signaling completion
     */
    private Mono<Void> updateLogisticsFields(UUID deliveryPersonId, DeliveryPersonUpdateRequest request) {
        return lectureLogisticsService.findByDeliveryPersonId(deliveryPersonId)
                .flatMap(logistics -> {
                    boolean changed = false;

                    if (request.getPlateNumber() != null) {
                        logistics.setPlateNumber(request.getPlateNumber());
                        changed = true;
                    }
                    if (request.getColor() != null) {
                        logistics.setColor(request.getColor());
                        changed = true;
                    }
                    if (request.getLogisticsClass() != null) {
                        logistics.setLogisticsClass(LogisticsClass.fromValue(request.getLogisticsClass()));
                        changed = true;
                    }
                    if (request.getLogisticsType() != null) {
                        logistics.setLogisticsType(LogisticsType.fromValue(request.getLogisticsType()));
                        changed = true;
                    }
                    if (request.getBackPhoto() != null) {
                        logistics.setBackPhoto(request.getBackPhoto());
                        changed = true;
                    }
                    if (request.getFrontPhoto() != null) {
                        logistics.setFrontPhoto(request.getFrontPhoto());
                        changed = true;
                    }
                    if (request.getTankCapacity() != null) {
                        logistics.setTankCapacity(request.getTankCapacity());
                        changed = true;
                    }
                    if (request.getLuggageMaxCapacity() != null) {
                        logistics.setLuggageMaxCapacity(request.getLuggageMaxCapacity());
                        changed = true;
                    }
                    if (request.getTotalSeatNumber() != null) {
                        logistics.setTotalSeatNumber(request.getTotalSeatNumber());
                        changed = true;
                    }

                    if (changed) {
                        return modificationLogisticsService.updateLogistics(logistics).then();
                    }
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.empty());
    }

    private Mono<Void> updateAddressFields(UUID personId, DeliveryPersonUpdateRequest request) {
        // If no address fields are present, skip
        if (request.getStreet() == null && request.getCity() == null &&
                request.getDistrict() == null && request.getCountry() == null &&
                request.getDescription() == null) {
            return Mono.empty();
        }

        return personAddressRepository.findByPersonId(personId)
                .next() // Get the first address if multiple exist
                .flatMap(personAddress -> addressRepository.findById(personAddress.getAddressId()))
                .flatMap(address -> {
                    boolean changed = false;
                    if (request.getStreet() != null) {
                        address.setStreet(request.getStreet());
                        changed = true;
                    }
                    if (request.getCity() != null) {
                        address.setCity(request.getCity());
                        changed = true;
                    }
                    if (request.getDistrict() != null) {
                        address.setDistrict(request.getDistrict());
                        changed = true;
                    }
                    if (request.getCountry() != null) {
                        address.setCountry(request.getCountry());
                        changed = true;
                    }
                    if (request.getDescription() != null) {
                        address.setDescription(request.getDescription());
                        changed = true;
                    }

                    if (changed) {
                        return modificationAddressService.updateAddress(address).then();
                    }
                    return Mono.empty();
                })
                .switchIfEmpty(
                        Mono.defer(() -> {
                            // Create new address if none exists
                            Address newAddress = new Address();
                            newAddress.setStreet(request.getStreet());
                            newAddress.setCity(request.getCity());
                            newAddress.setDistrict(request.getDistrict());
                            newAddress.setCountry(request.getCountry());
                            newAddress.setDescription(request.getDescription());
                            newAddress.setType(com.polytechnique.ticbnpick.models.enums.address.AddressType.PRIMARY);
                            // Set defaults or required fields if necessary.
                            // Assuming Address generated ID on save.

                            return creationAddressService.createAddress(newAddress)
                                    .flatMap(savedAddress -> {
                                        PersonAddress pa = new PersonAddress();
                                        pa.setPersonId(personId);
                                        pa.setAddressId(savedAddress.getId());
                                        return personAddressRepository.save(pa).then();
                                    });
                        }));
    }
}
