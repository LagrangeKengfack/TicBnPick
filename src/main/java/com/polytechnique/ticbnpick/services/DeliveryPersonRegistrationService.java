package com.polytechnique.ticbnpick.services;

import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonRegistrationRequest;
import com.polytechnique.ticbnpick.dtos.responses.DeliveryPersonRegistrationResponse;
import com.polytechnique.ticbnpick.mappers.DeliveryPersonMapper;
import com.polytechnique.ticbnpick.services.address.CreationAddressService;
import com.polytechnique.ticbnpick.services.deliveryperson.CreationDeliveryPersonService;
import com.polytechnique.ticbnpick.services.logistics.CreationLogisticsService;
import com.polytechnique.ticbnpick.services.person.CreationPersonService;
import com.polytechnique.ticbnpick.services.person.LecturePersonService;
import com.polytechnique.ticbnpick.services.support.EmailService;
import com.polytechnique.ticbnpick.services.support.TokenService;
import com.polytechnique.ticbnpick.validators.DeliveryPersonRegistrationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Orchestrator service for DeliveryPerson registration.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Service
@RequiredArgsConstructor
public class DeliveryPersonRegistrationService {

    private final CreationPersonService creationPersonService;
    private final LecturePersonService lecturePersonService;
    private final CreationDeliveryPersonService creationDeliveryPersonService;
    private final CreationLogisticsService creationLogisticsService;
    private final CreationAddressService creationAddressService;

    private final DeliveryPersonRegistrationValidator validator;
    private final DeliveryPersonMapper mapper;
    private final TokenService tokenService;
    private final EmailService emailService;

    public Mono<DeliveryPersonRegistrationResponse> register(DeliveryPersonRegistrationRequest request) {
        // TODO:
        // Purpose: Orchestrate full delivery person registration flow
        // Inputs: Registration Request DTO
        // Outputs: Registration Response DTO
        // Steps:
        //  1. validator.validate(request)
        //  2. lecturePersonService.existsByEmail(email) -> Error if true
        //  3. Map request to Person (password=null), DeliveryPerson, Logistics, Address
        //  4. creationPersonService.createPerson(person) -> returns savedPerson
        //  5. Set personId on DeliveryPerson, Logistics, Address
        //  6. Parallel save:
        //      - creationDeliveryPersonService.createDeliveryPerson
        //      - creationLogisticsService.createLogistics
        //      - creationAddressService.createAddress
        //  7. Construct Response DTO with ID and Status
        //  8. Send "Pending Validation" Email (async side effect)
        // Validations: Full DTO validation, Unique Email
        // Errors / Exceptions: EmailAlreadyUsedException, ValidationException
        // Reactive Flow: chain with flatMap, zip for parallel creations
        // Side Effects: 4 DB inserts, 1 Email sent
        // Security Notes: Public endpoint, no auth required
        return Mono.empty();
    }
}
