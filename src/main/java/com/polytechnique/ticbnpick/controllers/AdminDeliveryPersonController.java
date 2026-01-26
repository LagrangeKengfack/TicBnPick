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
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@RestController
@RequestMapping("/api/admin/delivery-persons")
@RequiredArgsConstructor
public class AdminDeliveryPersonController {

    private final AdminDeliveryPersonService adminService;

    /**
     * Endpoint to validate or reject a delivery person's registration.
     *
     * Delegates to the service layer to process the admin's decision.
     *
     * @param request the validation request body
     * @return a Mono<ResponseEntity<Void>> with status 200 OK
     */
    @PutMapping("/validate")
    public Mono<ResponseEntity<Void>> validateRegistration(
            @Valid @RequestBody AdminDeliveryPersonValidationRequest request) {
        // TODO: Call service to validate registration
        return adminService.validateRegistration(request)
                .map(v -> ResponseEntity.ok().build());
    }

    /**
     * Endpoint to retrieve detailed information about a delivery person.
     *
     * Returns an aggregated view of the delivery person's profile, including
     * personal info, logistics, and address.
     *
     * @param id the UUID of the delivery person
     * @return a Mono<ResponseEntity<DeliveryPersonDetailsResponse>> with the details
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DeliveryPersonDetailsResponse>> getDetails(@PathVariable UUID id) {
        // TODO: Call service to get details
        return adminService.getDeliveryPersonDetails(id)
                .map(ResponseEntity::ok);
    }

    /**
     * Endpoint to review (approve/reject) a pending profile update.
     *
     * @param updateId the UUID of the update request
     * @param approved query parameter indicating the decision
     * @param reason optional query parameter for the decision reason
     * @return a Mono<ResponseEntity<Void>> with status 200 OK
     */
    @PutMapping("/updates/{updateId}")
    public Mono<ResponseEntity<Void>> reviewUpdate(
            @PathVariable UUID updateId,
            @RequestParam boolean approved,
            @RequestParam(required = false) String reason) {
        // TODO: Call service to review update
        return adminService.reviewUpdate(updateId, approved, reason)
                .map(v -> ResponseEntity.ok().build());
    }
}
