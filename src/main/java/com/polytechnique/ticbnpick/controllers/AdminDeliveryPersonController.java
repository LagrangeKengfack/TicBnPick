package com.polytechnique.ticbnpick.controllers;

import com.polytechnique.ticbnpick.dtos.requests.AdminDeliveryPersonValidationRequest;
import com.polytechnique.ticbnpick.dtos.responses.DeliveryPersonDetailsResponse;
import com.polytechnique.ticbnpick.services.AdminDeliveryPersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Controller for admin operations on delivery persons.
 *
 * <p>Provides endpoints for administrative management of delivery person accounts:
 * <ul>
 *   <li>Validation of new registrations</li>
 *   <li>Suspension of active accounts</li>
 *   <li>Revocation of accounts</li>
 *   <li>Retrieval of detailed account information</li>
 * </ul>
 *
 * <p>All endpoints require admin authentication.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@RestController
@RequestMapping("/api/admin/delivery-persons")
@RequiredArgsConstructor
public class AdminDeliveryPersonController {

    private final AdminDeliveryPersonService adminService;

    /**
     * Validates or rejects a delivery person's registration.
     *
     * <p>Delegates to the service layer to process the admin's decision.
     * Updates the delivery person status and sends appropriate notification email.
     *
     * @param request the validation request body containing decision and optional reason
     * @return a Mono&lt;ResponseEntity&lt;Void&gt;&gt; with status 200 OK on success
     */
    @PutMapping("/validate")
    public Mono<ResponseEntity<Void>> validateRegistration(
            @Valid @RequestBody AdminDeliveryPersonValidationRequest request) {
        return adminService.validateRegistration(request)
                .map(v -> ResponseEntity.ok().build());
    }

    /**
     * Retrieves detailed information about a delivery person.
     *
     * <p>Returns an aggregated view of the delivery person's profile,
     * including personal info and commercial details.
     *
     * @param id the UUID of the delivery person
     * @return a Mono&lt;ResponseEntity&lt;DeliveryPersonDetailsResponse&gt;&gt; with the details
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DeliveryPersonDetailsResponse>> getDetails(@PathVariable UUID id) {
        return adminService.getDeliveryPersonDetails(id)
                .map(ResponseEntity::ok);
    }

    /**
     * Suspends a delivery person's account.
     *
     * <p>Changes the account status to SUSPENDED and sends a notification email.
     * Only APPROVED accounts can be suspended.
     *
     * @param id the UUID of the delivery person to suspend
     * @return a Mono&lt;ResponseEntity&lt;Void&gt;&gt; with status 200 OK on success
     */
    @PutMapping("/{id}/suspend")
    public Mono<ResponseEntity<Void>> suspendDeliveryPerson(@PathVariable UUID id) {
        return adminService.suspendDeliveryPerson(id)
                .map(v -> ResponseEntity.ok().build());
    }

    /**
     * Revokes (permanently deactivates) a delivery person's account.
     *
     * <p>Changes the account status to REJECTED and sends a notification email.
     * APPROVED or SUSPENDED accounts can be revoked.
     *
     * @param id the UUID of the delivery person to revoke
     * @return a Mono&lt;ResponseEntity&lt;Void&gt;&gt; with status 200 OK on success
     */
    @PutMapping("/{id}/revoke")
    public Mono<ResponseEntity<Void>> revokeDeliveryPerson(@PathVariable UUID id) {
        return adminService.revokeDeliveryPerson(id)
                .map(v -> ResponseEntity.ok().build());
    }
}
