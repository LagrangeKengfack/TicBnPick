package com.polytechnique.ticbnpick.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytechnique.ticbnpick.dtos.requests.AdminDeliveryPersonValidationRequest;
import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonUpdateRequest;
import com.polytechnique.ticbnpick.dtos.responses.DeliveryPersonDetailsResponse;
import com.polytechnique.ticbnpick.events.DeliveryPersonValidatedEvent;
import com.polytechnique.ticbnpick.exceptions.DeliveryPersonNotFoundException;
import com.polytechnique.ticbnpick.exceptions.ForbiddenOperationException;
import com.polytechnique.ticbnpick.exceptions.NotFoundException;
import com.polytechnique.ticbnpick.models.enums.deliveryPerson.DeliveryPersonStatus;
import com.polytechnique.ticbnpick.models.enums.logistics.LogisticsType;
import com.polytechnique.ticbnpick.services.deliveryperson.LectureDeliveryPersonService;
import com.polytechnique.ticbnpick.services.deliveryperson.ModificationDeliveryPersonService;
import com.polytechnique.ticbnpick.services.logistics.LectureLogisticsService;
import com.polytechnique.ticbnpick.services.logistics.ModificationLogisticsService;
import com.polytechnique.ticbnpick.services.person.LecturePersonService;
import com.polytechnique.ticbnpick.services.support.EmailService;
import com.polytechnique.ticbnpick.services.support.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Orchestrator service for Admin operations on DeliveryPersons.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminDeliveryPersonService {

    private final LectureDeliveryPersonService lectureDeliveryPersonService;
    private final ModificationDeliveryPersonService modificationDeliveryPersonService;
    private final LecturePersonService lecturePersonService;
    private final LectureLogisticsService lectureLogisticsService;
    private final ModificationLogisticsService modificationLogisticsService;
    private final PendingDeliveryPersonUpdateService pendingUpdateService;
    private final EmailService emailService;
    private final KafkaEventPublisher kafkaEventPublisher;
    private final ObjectMapper objectMapper;

    /**
     * Validates or rejects a delivery person registration application.
     *
     * Updates the status of the delivery person. If approved, sends an approval email.
     * If rejected, sends a rejection notice.
     *
     * @param request the validation request containing the ID and decision
     * @return a Mono<Void> signaling completion
     * @throws DeliveryPersonNotFoundException if the ID is invalid
     * @throws ForbiddenOperationException if status is not PENDING
     */
    public Mono<Void> validateRegistration(AdminDeliveryPersonValidationRequest request) {
        return lectureDeliveryPersonService.findById(request.getDeliveryPersonId())
                .switchIfEmpty(Mono.error(new DeliveryPersonNotFoundException("Delivery Person not found")))
                .flatMap(dp -> {
                    if (dp.getStatus() != DeliveryPersonStatus.PENDING) {
                        return Mono.error(new ForbiddenOperationException("Can only validate PENDING requests"));
                    }
                    
                    if (request.isApproved()) {
                        dp.setStatus(DeliveryPersonStatus.APPROVED);
                        return modificationDeliveryPersonService.updateDeliveryPerson(dp)
                                .flatMap(updated -> lecturePersonService.findById(updated.getPersonId())
                                        .doOnNext(person -> {
                                            // Send approval email
                                            emailService.sendAccountApproved(person.getEmail());
                                            // Publish Kafka event
                                            kafkaEventPublisher.publishDeliveryPersonValidated(
                                                    new DeliveryPersonValidatedEvent(updated.getId(), true)
                                            );
                                            log.info("Delivery person {} approved", updated.getId());
                                        })
                                        .then()
                                );
                    } else {
                        dp.setStatus(DeliveryPersonStatus.REJECTED);
                        return modificationDeliveryPersonService.updateDeliveryPerson(dp)
                                .flatMap(updated -> lecturePersonService.findById(updated.getPersonId())
                                        .doOnNext(person -> {
                                            // Send rejection email
                                            emailService.sendAccountRejected(person.getEmail(), request.getReason());
                                            // Publish Kafka event
                                            kafkaEventPublisher.publishDeliveryPersonValidated(
                                                    new DeliveryPersonValidatedEvent(updated.getId(), false)
                                            );
                                            log.info("Delivery person {} rejected", updated.getId());
                                        })
                                        .then()
                                );
                    }
                })
                .then();
    }

    /**
     * Reviews and applies a pending profile update.
     *
     * If approved, applies the JSON changes to the entity and notifies the user.
     * If rejected, updates the request status and notifies the user.
     *
     * @param updateId the UUID of the pending update request
     * @param approved boolean indicating approval or rejection
     * @param reason optional reason for the decision
     * @return a Mono<Void> signaling completion
     * @throws NotFoundException if update not found
     */
    public Mono<Void> reviewUpdate(UUID updateId, boolean approved, String reason) {
        return pendingUpdateService.findById(updateId)
                .switchIfEmpty(Mono.error(new NotFoundException("Pending update not found")))
                .flatMap(update -> {
                    if (approved) {
                        try {
                            DeliveryPersonUpdateRequest request = objectMapper.readValue(
                                    update.getNewDataJson(), DeliveryPersonUpdateRequest.class);
                            
                            return lectureDeliveryPersonService.findById(update.getDeliveryPersonId())
                                    .flatMap(dp -> {
                                        // Apply sensitive changes to DeliveryPerson
                                        if (request.getCommercialRegister() != null) {
                                            dp.setCommercialRegister(request.getCommercialRegister());
                                        }
                                        
                                        return modificationDeliveryPersonService.updateDeliveryPerson(dp)
                                                .flatMap(savedDp -> 
                                                    lectureLogisticsService.findByDeliveryPersonId(savedDp.getId())
                                                            .flatMap(logistics -> {
                                                                if (request.getLogisticsType() != null) {
                                                                    logistics.setLogisticsType(
                                                                            LogisticsType.fromValue(request.getLogisticsType()));
                                                                }
                                                                if (request.getLogisticImage() != null) {
                                                                    logistics.setLogisticImage(request.getLogisticImage());
                                                                }
                                                                return modificationLogisticsService.updateLogistics(logistics);
                                                            })
                                                            .then()
                                                )
                                                .then(pendingUpdateService.deleteById(update.getId()));
                                    });
                        } catch (JsonProcessingException e) {
                            return Mono.error(new RuntimeException("Error parsing update data", e));
                        }
                    } else {
                        update.setStatus("REJECTED");
                        return pendingUpdateService.save(update).then();
                    }
                });
    }

    /**
     * Retrieves aggregated details of a delivery person for admin view.
     *
     * Fetches and combines data from Person, DeliveryPerson, Logistics, and Address
     * services into a single detailed response.
     *
     * @param id the UUID of the delivery person
     * @return a Mono containing the detailed response DTO
     * @throws DeliveryPersonNotFoundException if not found
     */
    public Mono<DeliveryPersonDetailsResponse> getDeliveryPersonDetails(UUID id) {
        return lectureDeliveryPersonService.findById(id)
                .switchIfEmpty(Mono.error(new DeliveryPersonNotFoundException("Delivery Person not found")))
                .flatMap(dp -> lecturePersonService.findById(dp.getPersonId())
                        .map(person -> {
                            DeliveryPersonDetailsResponse response = new DeliveryPersonDetailsResponse();
                            response.setId(dp.getId());
                            response.setFirstName(person.getFirstName());
                            response.setLastName(person.getLastName());
                            response.setEmail(person.getEmail());
                            response.setPhone(person.getPhone());
                            response.setStatus(dp.getStatus() != null ? dp.getStatus().getValue() : null);
                            response.setCommercialName(dp.getCommercialName());
                            return response;
                        })
                );
    }
}
