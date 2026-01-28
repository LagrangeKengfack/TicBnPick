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
 * <p>Provides public endpoint for new delivery person registration.
 * After successful registration, the account is in PENDING status
 * awaiting admin validation.
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
     * Registers a new delivery person.
     *
     * <p>Accepts registration data, validates it, and creates the delivery person
     * with all related entities (Person, DeliveryPerson, Logistics, Address).
     * Sends a confirmation email and publishes a Kafka event.
     *
     * @param request the registration request body containing all required data
     * @return a Mono&lt;ResponseEntity&lt;DeliveryPersonRegistrationResponse&gt;&gt; with status 201 Created
     */
    @PostMapping("/register")
    public Mono<ResponseEntity<DeliveryPersonRegistrationResponse>> register(
            @Valid @RequestBody DeliveryPersonRegistrationRequest request) {
        return registrationService.register(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }
}
