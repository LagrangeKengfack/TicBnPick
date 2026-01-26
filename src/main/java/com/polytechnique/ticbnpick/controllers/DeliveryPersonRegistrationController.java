package com.polytechnique.ticbnpick.controllers;

import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonRegistrationRequest;
import com.polytechnique.ticbnpick.dtos.responses.DeliveryPersonRegistrationResponse;
import com.polytechnique.ticbnpick.services.DeliveryPersonRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controller for delivery person registration.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@RestController
@RequestMapping("/api/delivery-persons")
@RequiredArgsConstructor
public class DeliveryPersonRegistrationController {

    private final DeliveryPersonRegistrationService registrationService;

    /**
     * Endpoint for public registration of a new delivery person.
     *
     * Accepts registration data, validates it, and initiates the registration process.
     *
     * @param request the registration request body
     * @return a Mono<ResponseEntity<DeliveryPersonRegistrationResponse>> with status 201 Created
     */
    @PostMapping("/register")
    public Mono<ResponseEntity<DeliveryPersonRegistrationResponse>> register(
            @Valid @RequestBody DeliveryPersonRegistrationRequest request) {
        // TODO: Call service to register delivery person
        return registrationService.register(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }
}
