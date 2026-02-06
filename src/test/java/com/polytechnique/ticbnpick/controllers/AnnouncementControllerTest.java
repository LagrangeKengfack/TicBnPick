package com.polytechnique.ticbnpick.controllers;

import com.polytechnique.ticbnpick.dtos.announcement.AnnouncementRequestDTO;
import com.polytechnique.ticbnpick.dtos.announcement.AnnouncementResponseDTO;
import com.polytechnique.ticbnpick.models.enums.announcement.AnnouncementStatus;
import com.polytechnique.ticbnpick.services.AnnouncementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest(controllers = AnnouncementController.class)
class AnnouncementControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AnnouncementService announcementService;

    @Test
    @WithMockUser
    void createAnnouncement_Success() {
        AnnouncementRequestDTO request = new AnnouncementRequestDTO();
        request.setTitle("Test Announcement");

        AnnouncementResponseDTO response = new AnnouncementResponseDTO();
        response.setId(UUID.randomUUID());
        response.setTitle("Test Announcement");
        response.setStatus(AnnouncementStatus.PUBLISHED);

        when(announcementService.createAnnouncement(any(AnnouncementRequestDTO.class))).thenReturn(Mono.just(response));

        webTestClient.mutateWith(csrf())
                .post().uri("/api/announcements")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.title").isEqualTo("Test Announcement");
    }

    @Test
    @WithMockUser
    void getAllAnnouncements_Success() {
        AnnouncementResponseDTO response = new AnnouncementResponseDTO();
        response.setId(UUID.randomUUID());
        response.setTitle("Announcement 1");

        when(announcementService.getAllAnnouncements()).thenReturn(Flux.just(response));

        webTestClient.mutateWith(csrf())
                .get().uri("/api/announcements")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AnnouncementResponseDTO.class)
                .hasSize(1);
    }

    @Test
    @WithMockUser
    void getAnnouncement_Success() {
        UUID id = UUID.randomUUID();
        AnnouncementResponseDTO response = new AnnouncementResponseDTO();
        response.setId(id);

        when(announcementService.getAnnouncement(id)).thenReturn(Mono.just(response));

        webTestClient.mutateWith(csrf())
                .get().uri("/api/announcements/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id.toString());
    }

    @Test
    @WithMockUser
    void updateAnnouncement_Success() {
        UUID id = UUID.randomUUID();
        AnnouncementRequestDTO request = new AnnouncementRequestDTO();
        request.setTitle("Updated Title");

        AnnouncementResponseDTO response = new AnnouncementResponseDTO();
        response.setId(id);
        response.setTitle("Updated Title");

        when(announcementService.updateAnnouncement(eq(id), any(AnnouncementRequestDTO.class)))
                .thenReturn(Mono.just(response));

        webTestClient.mutateWith(csrf())
                .put().uri("/api/announcements/{id}", id)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Updated Title");
    }

    @Test
    @WithMockUser
    void deleteAnnouncement_Success() {
        UUID id = UUID.randomUUID();
        when(announcementService.deleteAnnouncement(id)).thenReturn(Mono.empty());

        webTestClient.mutateWith(csrf())
                .delete().uri("/api/announcements/{id}", id)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithMockUser
    void publishAnnouncement_Success() {
        UUID id = UUID.randomUUID();
        AnnouncementResponseDTO response = new AnnouncementResponseDTO();
        response.setId(id);
        response.setStatus(AnnouncementStatus.PUBLISHED);

        when(announcementService.publishAnnouncement(id)).thenReturn(Mono.just(response));

        webTestClient.mutateWith(csrf())
                .patch().uri("/api/announcements/{id}/publish", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("PUBLISHED");
    }
}
