package com.polytechnique.ticbnpick.controllers;

import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonUpdateRequest;
import com.polytechnique.ticbnpick.services.DeliveryPersonProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Controller for delivery person profile management.
 *
 * @author Kengfack Lagrange
 * @date 25/01/2026
 */
@RestController
@RequestMapping("/api/delivery-persons")
@RequiredArgsConstructor
public class DeliveryPersonController {

    private final DeliveryPersonProfileService profileService;

    /**
     * Updates the delivery person's profile.
     * Sensitive fields trigger a pending update request.
     * Non-sensitive fields are updated immediately.
     *
     * @param id the delivery person ID
     * @param request the update request
     * @return 200 OK
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Void>> updateProfile(
            @PathVariable UUID id,
            @Valid @RequestBody DeliveryPersonUpdateRequest request) {
        return profileService.updateProfile(id, request)
                .map(v -> ResponseEntity.ok().build());
    }
}
