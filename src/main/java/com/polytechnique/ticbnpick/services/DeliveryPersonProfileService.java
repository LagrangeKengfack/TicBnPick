package com.polytechnique.ticbnpick.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonUpdateRequest;
import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.models.Logistics;
import com.polytechnique.ticbnpick.models.PendingDeliveryPersonUpdate;
import com.polytechnique.ticbnpick.models.Person;
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

import java.time.Instant;
import java.util.UUID;

/**
 * Service to handle high-level delivery person profile updates.
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
    private final PendingDeliveryPersonUpdateService pendingUpdateService;
    private final ObjectMapper objectMapper;

    public Mono<Void> updateProfile(UUID deliveryPersonId, DeliveryPersonUpdateRequest request) {
        return lectureDeliveryPersonService.findById(deliveryPersonId)
                .flatMap(dp -> {
                    boolean sensitiveChanged = isSensitiveChanged(request);

                    Mono<Void> updateNonSensitive = updateNonSensitiveFields(dp, request);
                    Mono<Void> handleSensitive = sensitiveChanged ? createPendingUpdate(dp.getId(), request) : Mono.empty();

                    return updateNonSensitive.then(handleSensitive);
                });
    }

    private boolean isSensitiveChanged(DeliveryPersonUpdateRequest request) {
        return request.getCommercialRegister() != null ||
               request.getLogisticsType() != null ||
               request.getLogisticImage() != null;
    }

    private Mono<Void> createPendingUpdate(UUID deliveryPersonId, DeliveryPersonUpdateRequest request) {
        try {
            PendingDeliveryPersonUpdate update = new PendingDeliveryPersonUpdate();
            update.setDeliveryPersonId(deliveryPersonId);
            update.setStatus("PENDING");
            update.setCreatedAt(Instant.now());
            // Create a JSON containing ONLY the sensitive fields to be approved?
            // Or the whole request?
            // User says "Modification -> creation PendingDeliveryPersonUpdate".
            // I'll store the whole request for simplicity or just the sensitive parts.
            // Let's store the request as is.
            update.setNewDataJson(objectMapper.writeValueAsString(request));
            return pendingUpdateService.save(update).then();
        } catch (JsonProcessingException e) {
            return Mono.error(new RuntimeException("Error serializing update request", e));
        }
    }

    private Mono<Void> updateNonSensitiveFields(DeliveryPerson dp, DeliveryPersonUpdateRequest request) {
        // Update Person fields (phone, etc.)
        Mono<Void> updatePerson = lecturePersonService.findById(dp.getPersonId())
                .flatMap(person -> {
                    boolean changed = false;
                    if (request.getPhone() != null) {
                        person.setPhone(request.getPhone());
                        changed = true;
                    }
                    // Add other person fields if needed
                    if (changed) {
                        return modificationPersonService.updatePerson(person).then();
                    }
                    return Mono.empty();
                });

        // Update DeliveryPerson non-sensitive fields
        Mono<Void> updateDP = Mono.defer(() -> {
            boolean changed = false;
            if (request.getCommercialName() != null) {
                dp.setCommercialName(request.getCommercialName());
                changed = true;
            }
            // other fields
            if (changed) {
                return modificationDeliveryPersonService.updateDeliveryPerson(dp).then();
            }
            return Mono.empty();
        });

        // Update Logistics non-sensitive fields
        Mono<Void> updateLogistics = lectureLogisticsService.findByDeliveryPersonId(dp.getId())
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
                    // other fields
                    if (changed) {
                        return modificationLogisticsService.updateLogistics(logistics).then();
                    }
                    return Mono.empty();
                });

        return Mono.when(updatePerson, updateDP, updateLogistics);
    }
}
