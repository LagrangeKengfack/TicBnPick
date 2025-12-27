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

    @PutMapping("/validate")
    public Mono<ResponseEntity<Void>> validateRegistration(
            @Valid @RequestBody AdminDeliveryPersonValidationRequest request) {
        // TODO: Call service to validate registration
        return adminService.validateRegistration(request)
                .map(v -> ResponseEntity.ok().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DeliveryPersonDetailsResponse>> getDetails(@PathVariable UUID id) {
        // TODO: Call service to get details
        return adminService.getDeliveryPersonDetails(id)
                .map(ResponseEntity::ok);
    }

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
