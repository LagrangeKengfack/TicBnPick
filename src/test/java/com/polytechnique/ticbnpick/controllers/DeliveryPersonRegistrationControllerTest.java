package com.polytechnique.ticbnpick.controllers;

import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonRegistrationRequest;
import com.polytechnique.ticbnpick.dtos.responses.DeliveryPersonRegistrationResponse;
import com.polytechnique.ticbnpick.models.enums.logistics.LogisticsClass;
import com.polytechnique.ticbnpick.models.enums.logistics.LogisticsType;
import com.polytechnique.ticbnpick.services.DeliveryPersonRegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest(controllers = DeliveryPersonRegistrationController.class)
class DeliveryPersonRegistrationControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DeliveryPersonRegistrationService registrationService;

    @Test
    @WithMockUser
    void register_Success() {
        DeliveryPersonRegistrationRequest request = new DeliveryPersonRegistrationRequest();
        request.setLastName("Doe");
        request.setFirstName("John");
        request.setPhone("123456789");
        request.setEmail("john.doe@example.com");
        request.setPassword("password");
        // Logistics fields - ensuring compatibility with recent changes
        request.setLogisticsType(LogisticsType.BIKE.getValue());
        request.setLogisticsClass(LogisticsClass.STANDARD.getValue());
        request.setPlateNumber("AB-123-CD");

        DeliveryPersonRegistrationResponse response = new DeliveryPersonRegistrationResponse();
        response.setDeliveryPersonId(UUID.randomUUID());
        response.setStatus("PENDING");

        when(registrationService.register(any(DeliveryPersonRegistrationRequest.class)))
                .thenReturn(Mono.just(response));

        webTestClient.mutateWith(csrf())
                .post().uri("/api/delivery-persons/register")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.status").isEqualTo("PENDING");
    }

    @Test
    @WithMockUser
    void register_ValidationFailure() {
        DeliveryPersonRegistrationRequest request = new DeliveryPersonRegistrationRequest();
        // Missing required fields like email, password, etc.

        webTestClient.mutateWith(csrf())
                .post().uri("/api/delivery-persons/register")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
