package com.polytechnique.ticbnpick.controllers;

import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonLocationUpdateRequest;
import com.polytechnique.ticbnpick.services.DeliveryPersonLocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest(controllers = DeliveryPersonLocationController.class)
class DeliveryPersonLocationControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DeliveryPersonLocationService locationService;

    @Test
    @WithMockUser
    void updateLocation_Success() {
        UUID id = UUID.randomUUID();
        DeliveryPersonLocationUpdateRequest request = new DeliveryPersonLocationUpdateRequest();
        request.setLatitude(48.8566);
        request.setLongitude(2.3522);

        when(locationService.updateLocation(eq(id), eq(48.8566), eq(2.3522)))
                .thenReturn(Mono.empty());

        webTestClient.mutateWith(csrf())
                .patch().uri("/api/delivery-persons/{id}/location", id)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();
    }
}
