package com.polytechnique.ticbnpick.controllers;

import com.polytechnique.ticbnpick.dtos.subscription.SubscriptionRequestDTO;
import com.polytechnique.ticbnpick.services.AnnouncementService;
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

@WebFluxTest(controllers = SubscriptionController.class)
class SubscriptionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AnnouncementService announcementService;

    @Test
    @WithMockUser
    void subscribe_Accepted() {
        UUID announcementId = UUID.randomUUID();
        UUID deliveryPersonId = UUID.randomUUID();
        SubscriptionRequestDTO request = new SubscriptionRequestDTO();
        request.setDeliveryPersonId(deliveryPersonId);

        when(announcementService.initiateSubscription(eq(announcementId), eq(deliveryPersonId)))
                .thenReturn(Mono.empty());

        webTestClient.mutateWith(csrf())
                .post().uri("/api/announcements/{id}/subscribe", announcementId)
                .bodyValue(request)
                .exchange()
                .expectStatus().isAccepted();
    }
}
