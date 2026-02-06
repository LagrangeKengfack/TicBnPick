package com.polytechnique.ticbnpick.controllers;

import com.polytechnique.ticbnpick.dtos.requests.AdminDeliveryPersonValidationRequest;
import com.polytechnique.ticbnpick.dtos.responses.DeliveryPersonDetailsResponse;
import com.polytechnique.ticbnpick.services.AdminDeliveryPersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest(controllers = AdminDeliveryPersonController.class)
class AdminDeliveryPersonControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AdminDeliveryPersonService adminService;

    @Test
    @WithMockUser
    void validateRegistration_Success() {
        AdminDeliveryPersonValidationRequest request = new AdminDeliveryPersonValidationRequest();
        request.setDeliveryPersonId(UUID.randomUUID());
        request.setApproved(true);

        when(adminService.validateRegistration(any(AdminDeliveryPersonValidationRequest.class)))
                .thenReturn(Mono.empty());

        webTestClient.mutateWith(csrf())
                .put().uri("/api/admin/delivery-persons/validate")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser
    void getDetails_Success() {
        UUID id = UUID.randomUUID();
        DeliveryPersonDetailsResponse response = new DeliveryPersonDetailsResponse();
        response.setId(id);

        when(adminService.getDeliveryPersonDetails(id)).thenReturn(Mono.just(response));

        webTestClient.mutateWith(csrf())
                .get().uri("/api/admin/delivery-persons/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id.toString());
    }

    @Test
    @WithMockUser
    void suspendDeliveryPerson_Success() {
        UUID id = UUID.randomUUID();
        when(adminService.suspendDeliveryPerson(id)).thenReturn(Mono.empty());

        webTestClient.mutateWith(csrf())
                .put().uri("/api/admin/delivery-persons/{id}/suspend", id)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser
    void revokeDeliveryPerson_Success() {
        UUID id = UUID.randomUUID();
        when(adminService.revokeDeliveryPerson(id)).thenReturn(Mono.empty());

        webTestClient.mutateWith(csrf())
                .put().uri("/api/admin/delivery-persons/{id}/revoke", id)
                .exchange()
                .expectStatus().isOk();
    }
}
