package com.polytechnique.ticbnpick.controllers;

import com.polytechnique.ticbnpick.dtos.client.ClientDTO;
import com.polytechnique.ticbnpick.dtos.client.ClientResponseDTO;
import com.polytechnique.ticbnpick.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ClientResponseDTO> createClient(@RequestBody ClientDTO clientDTO) {
        return clientService.createClient(clientDTO);
    }

    @GetMapping("/check-email")
    public Mono<Boolean> checkEmail(@RequestParam String email) {
        return clientService.checkEmailExists(email);
    }

    @GetMapping("/check-national-id")
    public Mono<Boolean> checkNationalId(@RequestParam String nationalId) {
        return clientService.checkNationalIdExists(nationalId);
    }

    @GetMapping
    public Flux<ClientResponseDTO> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public Mono<ClientResponseDTO> getClientById(@PathVariable UUID id) {
        return clientService.getClientById(id);
    }

    @PutMapping("/{id}")
    public Mono<ClientResponseDTO> updateClient(@PathVariable UUID id, @RequestBody ClientDTO clientDTO) {
        return clientService.updateClient(id, clientDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteClient(@PathVariable UUID id) {
        return clientService.deleteClient(id);
    }
}
