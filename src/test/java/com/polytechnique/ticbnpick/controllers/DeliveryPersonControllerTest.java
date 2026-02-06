package com.polytechnique.ticbnpick.controllers;

import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonUpdateRequest;
import com.polytechnique.ticbnpick.services.DeliveryPersonProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest(controllers = DeliveryPersonController.class)
class DeliveryPersonControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DeliveryPersonProfileService profileService;

    @Test
    @WithMockUser
    void updateProfile_Success() {
        UUID id = UUID.randomUUID();
        DeliveryPersonUpdateRequest request = new DeliveryPersonUpdateRequest();
        request.setCommercialName("Updated Name");
        request.setCity("Paris"); // Test address update field

        when(profileService.updateProfile(eq(id), any(DeliveryPersonUpdateRequest.class)))
                .thenReturn(Mono.empty());

        webTestClient.mutateWith(csrf())
                .put().uri("/api/delivery-persons/{id}", id)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser
    void updateProfile_NotFound() {
        UUID id = UUID.randomUUID();
        DeliveryPersonUpdateRequest request = new DeliveryPersonUpdateRequest();
        request.setCommercialName("Updated Name");

        // Service returns empty Mono (triggered by .switchIfEmpty(Mono.empty()) inside
        // service when not found)
        // Service throws ResponseStatusException when not found
        when(profileService.updateProfile(eq(id), any(DeliveryPersonUpdateRequest.class)))
                .thenReturn(Mono.error(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND)));

        webTestClient.mutateWith(csrf())
                .put().uri("/api/delivery-persons/{id}", id)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound();
    }
}
