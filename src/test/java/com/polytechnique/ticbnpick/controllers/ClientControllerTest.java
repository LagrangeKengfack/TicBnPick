package com.polytechnique.ticbnpick.controllers;

import com.polytechnique.ticbnpick.dtos.client.ClientDTO;
import com.polytechnique.ticbnpick.dtos.client.ClientResponseDTO;
import com.polytechnique.ticbnpick.services.ClientService;
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

@WebFluxTest(controllers = ClientController.class)
class ClientControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ClientService clientService;

    @Test
    @WithMockUser
    void createClient_Success() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setEmail("test@example.com");

        ClientResponseDTO response = new ClientResponseDTO();
        response.setId(UUID.randomUUID());
        response.setEmail("test@example.com");

        when(clientService.createClient(any(ClientDTO.class))).thenReturn(Mono.just(response));

        webTestClient.mutateWith(csrf())
                .post().uri("/api/clients")
                .bodyValue(clientDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.email").isEqualTo("test@example.com");
    }

    @Test
    @WithMockUser
    void checkEmail_Exists() {
        String email = "test@example.com";
        when(clientService.checkEmailExists(email)).thenReturn(Mono.just(true));

        webTestClient.mutateWith(csrf())
                .get().uri(uriBuilder -> uriBuilder.path("/api/clients/check-email")
                        .queryParam("email", email)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);
    }

    @Test
    @WithMockUser
    void getAllClients_Success() {
        ClientResponseDTO response = new ClientResponseDTO();
        response.setId(UUID.randomUUID());

        when(clientService.getAllClients()).thenReturn(Flux.just(response));

        webTestClient.mutateWith(csrf())
                .get().uri("/api/clients")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ClientResponseDTO.class)
                .hasSize(1);
    }

    @Test
    @WithMockUser
    void getClientById_Success() {
        UUID id = UUID.randomUUID();
        ClientResponseDTO response = new ClientResponseDTO();
        response.setId(id);

        when(clientService.getClientById(id)).thenReturn(Mono.just(response));

        webTestClient.mutateWith(csrf())
                .get().uri("/api/clients/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id.toString());
    }

    @Test
    @WithMockUser
    void updateClient_Success() {
        UUID id = UUID.randomUUID();
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setEmail("updated@example.com");

        ClientResponseDTO response = new ClientResponseDTO();
        response.setId(id);
        response.setEmail("updated@example.com");

        when(clientService.updateClient(eq(id), any(ClientDTO.class))).thenReturn(Mono.just(response));

        webTestClient.mutateWith(csrf())
                .put().uri("/api/clients/{id}", id)
                .bodyValue(clientDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo("updated@example.com");
    }

    @Test
    @WithMockUser
    void deleteClient_Success() {
        UUID id = UUID.randomUUID();
        when(clientService.deleteClient(id)).thenReturn(Mono.empty());

        webTestClient.mutateWith(csrf())
                .delete().uri("/api/clients/{id}", id)
                .exchange()
                .expectStatus().isNoContent();
    }
}
