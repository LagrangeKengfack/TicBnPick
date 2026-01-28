package com.polytechnique.ticbnpick.services;

import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonUpdateRequest;
import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.models.enums.logistics.LogisticsClass;
import com.polytechnique.ticbnpick.models.enums.logistics.LogisticsType;
import com.polytechnique.ticbnpick.services.deliveryperson.LectureDeliveryPersonService;
import com.polytechnique.ticbnpick.services.deliveryperson.ModificationDeliveryPersonService;
import com.polytechnique.ticbnpick.services.logistics.LectureLogisticsService;
import com.polytechnique.ticbnpick.services.logistics.ModificationLogisticsService;
import com.polytechnique.ticbnpick.services.person.LecturePersonService;
import com.polytechnique.ticbnpick.services.person.ModificationPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service to handle delivery person profile updates.
 *
 * <p>Orchestrates updates to DeliveryPerson, Person, and Logistics entities
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

    /**
     * Updates a delivery person's profile with the provided data.
     *
     * <p>Applies all provided field updates directly to the corresponding entities:
     * <ul>
     *   <li>Person fields: phone</li>
     *   <li>DeliveryPerson fields: commercialName, commercialRegister</li>
     *   <li>Logistics fields: plateNumber, color, logisticsClass, logisticsType, logisticImage, etc.</li>
     * </ul>
     *
     * @param deliveryPersonId the UUID of the delivery person to update
     * @param request the update request containing the fields to modify
     * @return a Mono&lt;Void&gt; signaling completion
     */
    public Mono<Void> updateProfile(UUID deliveryPersonId, DeliveryPersonUpdateRequest request) {
        return lectureDeliveryPersonService.findById(deliveryPersonId)
                .flatMap(dp -> {
                    Mono<Void> updatePerson = updatePersonFields(dp, request);
                    Mono<Void> updateDeliveryPerson = updateDeliveryPersonFields(dp, request);
                    Mono<Void> updateLogistics = updateLogisticsFields(dp.getId(), request);

                    return Mono.when(updatePerson, updateDeliveryPerson, updateLogistics);
                });
    }

    /**
     * Updates Person entity fields from the request.
     *
     * @param dp the delivery person entity (for personId)
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
     * @param dp the delivery person entity
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
     * @param request the update request
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
                    if (request.getLogisticImage() != null) {
                        logistics.setLogisticImage(request.getLogisticImage());
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
}
