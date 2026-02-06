package com.polytechnique.ticbnpick.controllers;

import com.polytechnique.ticbnpick.dtos.requests.AddressCreateRequest;
import com.polytechnique.ticbnpick.mappers.AddressMapper;
import com.polytechnique.ticbnpick.models.Address;
import com.polytechnique.ticbnpick.services.address.CreationAddressService;
import com.polytechnique.ticbnpick.services.address.LectureAddressService;
import com.polytechnique.ticbnpick.services.address.ModificationAddressService;
import com.polytechnique.ticbnpick.services.address.SuppressionAddressService;
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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest(controllers = AddressController.class)
class AddressControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CreationAddressService creationAddressService;
    @MockBean
    private LectureAddressService lectureAddressService;
    @MockBean
    private ModificationAddressService modificationAddressService;
    @MockBean
    private SuppressionAddressService suppressionAddressService;
    @MockBean
    private AddressMapper addressMapper;

    @Test
    @WithMockUser
    void createAddress_Success() {
        AddressCreateRequest request = new AddressCreateRequest();
        request.setStreet("123 Main St");

        Address address = new Address();
        address.setId(UUID.randomUUID());
        address.setStreet("123 Main St");

        when(addressMapper.toEntity(any(AddressCreateRequest.class))).thenReturn(address);
        when(creationAddressService.createAddress(any(Address.class))).thenReturn(Mono.just(address));

        webTestClient.mutateWith(csrf())
                .post().uri("/api/addresses")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.street").isEqualTo("123 Main St");
    }

    @Test
    @WithMockUser
    void getAllAddresses_Success() {
        Address address = new Address();
        address.setId(UUID.randomUUID());

        when(lectureAddressService.getAllAddresses()).thenReturn(Flux.just(address));

        webTestClient.mutateWith(csrf())
                .get().uri("/api/addresses")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Address.class)
                .hasSize(1);
    }

    @Test
    @WithMockUser
    void getAddress_Success() {
        UUID id = UUID.randomUUID();
        Address address = new Address();
        address.setId(id);

        when(lectureAddressService.getAddressById(id)).thenReturn(Mono.just(address));

        webTestClient.mutateWith(csrf())
                .get().uri("/api/addresses/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id.toString());
    }

    @Test
    @WithMockUser
    void updateAddress_Success() {
        UUID id = UUID.randomUUID();
        AddressCreateRequest request = new AddressCreateRequest();

        Address existingAddress = new Address();
        existingAddress.setId(id);

        Address updatedAddress = new Address();
        updatedAddress.setId(id);
        updatedAddress.setStreet("Updated St");

        when(lectureAddressService.getAddressById(id)).thenReturn(Mono.just(existingAddress));
        when(addressMapper.toEntity(any(AddressCreateRequest.class))).thenReturn(updatedAddress);
        when(modificationAddressService.updateAddress(any(Address.class))).thenReturn(Mono.just(updatedAddress));

        webTestClient.mutateWith(csrf())
                .put().uri("/api/addresses/{id}", id)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.street").isEqualTo("Updated St");
    }

    @Test
    @WithMockUser
    void deleteAddress_Success() {
        UUID id = UUID.randomUUID();
        when(suppressionAddressService.deleteAddress(id)).thenReturn(Mono.empty());

        webTestClient.mutateWith(csrf())
                .delete().uri("/api/addresses/{id}", id)
                .exchange()
                .expectStatus().isNoContent();
    }
}
