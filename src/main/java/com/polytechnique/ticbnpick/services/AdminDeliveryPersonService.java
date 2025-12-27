package com.polytechnique.ticbnpick.services;

import com.polytechnique.ticbnpick.dtos.requests.AdminDeliveryPersonValidationRequest;
import com.polytechnique.ticbnpick.dtos.responses.DeliveryPersonDetailsResponse;
import com.polytechnique.ticbnpick.services.deliveryperson.LectureDeliveryPersonService;
import com.polytechnique.ticbnpick.services.deliveryperson.ModificationDeliveryPersonService;
import com.polytechnique.ticbnpick.services.person.LecturePersonService;
import com.polytechnique.ticbnpick.services.support.EmailService;
import com.polytechnique.ticbnpick.services.support.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Orchestrator service for Admin operations on DeliveryPersons.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class AdminDeliveryPersonService {

    private final LectureDeliveryPersonService lectureDeliveryPersonService;
    private final ModificationDeliveryPersonService modificationDeliveryPersonService;
    private final LecturePersonService lecturePersonService;
    private final PendingDeliveryPersonUpdateService pendingUpdateService; // Keep this one as is or refactor if needed
    private final TokenService tokenService;
    private final EmailService emailService;

    public Mono<Void> validateRegistration(AdminDeliveryPersonValidationRequest request) {
        // TODO:
        // Purpose: Admin approves or rejects a registration
        // Inputs: Validation Request (ID, approved boolean, reason)
        // Outputs: Mono<Void>
        // Steps:
        //  1. lectureDeliveryPersonService.findById(request.getDeliveryPersonId())
        //  2. If approved:
        //      a. Update status to APPROVED
        //      b. modificationDeliveryPersonService.updateDeliveryPerson
        //      c. tokenService.generateToken(personId)
        //      d. emailService.send(SetupPasswordLink)
        //  3. If rejected:
        //      a. Update status to REJECTED
        //      b. modificationDeliveryPersonService.updateDeliveryPerson
        //      c. emailService.send(RejectionNotice)
        // Validations: ID exists, Current status is PENDING
        // Errors / Exceptions: DeliveryPersonNotFoundException, ForbiddenOperationException
        // Reactive Flow: flatMap chain
        // Side Effects: DB update, Email sent, Token generated
        // Security Notes: Admin ONLY
        return Mono.empty();
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
     * @throws com.polytechnique.ticbnpick.exceptions.NotFoundException if update not found
     */
    public Mono<Void> reviewUpdate(UUID updateId, boolean approved, String reason) {
        // TODO:
        // Purpose: Admin reviews a profile update request
        // Inputs: updateId, decision
        // Outputs: Mono<Void>
        // Steps:
        //  1. pendingUpdateService.findById(updateId)
        //  2. If approved:
        //      a. Apply JSON changes to DeliveryPerson entity
        //      b. modificationDeliveryPersonService.updateDeliveryPerson
        //      c. pendingUpdateService.deleteById OR update status to APPLIED
        //      d. emailService.send(UpdateApproved)
        //  3. If rejected:
        //      a. pendingUpdateService.update status to REJECTED
        //      b. emailService.send(UpdateRejected)
        // Validations: updateId exists
        // Errors / Exceptions: NotFound
        // Reactive Flow: flatMap chain
        // Side Effects: DB update, Email
        // Security Notes: Admin ONLY
        return Mono.empty();
    }

    public Mono<DeliveryPersonDetailsResponse> getDeliveryPersonDetails(UUID id) {
        // TODO:
        // Purpose: Get full aggregated details for Admin view
        // Inputs: DeliveryPerson UUID
        // Outputs: Detailed Response DTO
        // Steps:
        //  1. lectureDeliveryPersonService.findById(id)
        //  2. lecturePersonService.findById(deliveryPerson.getPersonId())
        //  3. lectureLogisticsService.findAllByCourierId(id) (Inject service first)
        //  4. lectureAddressService... (Inject service first)
        //  5. Aggregate into DTO
        // Validations: ID exists
        // Errors / Exceptions: NotFound
        // Reactive Flow: zip results
        // Side Effects: None
        // Security Notes: Admin ONLY
        return Mono.empty();
    }
}
