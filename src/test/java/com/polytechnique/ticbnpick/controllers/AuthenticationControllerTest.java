// package com.polytechnique.ticbnpick.controllers;
//
// import com.polytechnique.ticbnpick.dtos.auth.AuthRequestDTO;
// import com.polytechnique.ticbnpick.dtos.auth.AuthResponseDTO;
// import com.polytechnique.ticbnpick.services.AuthenticationService;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.reactive.server.WebTestClient;
// import reactor.core.publisher.Mono;
//
// import java.util.UUID;
//
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
// import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
//
// @WebFluxTest(controllers = AuthenticationController.class)
// class AuthenticationControllerTest {
//
//     @Autowired
//     private WebTestClient webTestClient;
//
//     @MockBean
//     private AuthenticationService authenticationService;
//
//     @Test
//     @WithMockUser
//     void login_Success() {
//         AuthRequestDTO request = new AuthRequestDTO("test@example.com", "password");
//         AuthResponseDTO response = new AuthResponseDTO(
//             "mockToken", UUID.randomUUID(), "Doe", "John", "test@example.com", "1234567890", "url"
//         );
//
//         when(authenticationService.login(any(AuthRequestDTO.class))).thenReturn(Mono.just(response));
//
//         webTestClient.mutateWith(csrf())
//                 .post().uri("/api/auth/login")
//                 .bodyValue(request)
//                 .exchange()
//                 .expectStatus().isOk()
//                 .expectBody()
//                 .jsonPath("$.token").isEqualTo("mockToken")
//                 .jsonPath("$.email").isEqualTo("test@example.com");
//     }
//
//     @Test
//     @WithMockUser
//     void login_Failure() {
//         AuthRequestDTO request = new AuthRequestDTO("test@example.com", "wrongpassword");
//
//         when(authenticationService.login(any(AuthRequestDTO.class)))
//                 .thenReturn(Mono.error(new RuntimeException("Invalid credentials")));
//
//         webTestClient.mutateWith(csrf())
//                 .post().uri("/api/auth/login")
//                 .bodyValue(request)
//                 .exchange()
//                 .expectStatus().is5xxServerError();
//     }
// }
