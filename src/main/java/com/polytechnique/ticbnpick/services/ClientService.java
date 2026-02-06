package com.polytechnique.ticbnpick.services;

import com.polytechnique.ticbnpick.dtos.client.ClientDTO;
import com.polytechnique.ticbnpick.dtos.client.ClientResponseDTO;
import com.polytechnique.ticbnpick.exceptions.ResourceNotFoundException;
import com.polytechnique.ticbnpick.models.Client;
import com.polytechnique.ticbnpick.models.Person;
import com.polytechnique.ticbnpick.repositories.ClientRepository;
import com.polytechnique.ticbnpick.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service layer for managing clients.
 * Handles business logic for client operations.
 *
 * @author Kenmeugne Michèle
 * @date 18/12/2025
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new client with associated person.
     *
     * @param clientDTO client data transfer object
     * @return created client response
     * @author Kenmeugne Michèle
     * @date 18/12/2025
     */
    public Mono<ClientResponseDTO> createClient(ClientDTO clientDTO) {
        Person person = new Person();
        person.setLastName(clientDTO.getLastName());
        person.setFirstName(clientDTO.getFirstName());
        person.setPhone(clientDTO.getPhone());
        person.setEmail(clientDTO.getEmail());
        person.setPassword(passwordEncoder.encode(clientDTO.getPassword()));
        person.setNationalId(clientDTO.getNationalId());
        person.setPhotoCard(clientDTO.getPhotoCard());
        person.setCriminalRecord(clientDTO.getCriminalRecord());
        person.setRating(0.0);
        person.setTotalDeliveries(0);

        return personRepository.save(person)
                .flatMap(savedPerson -> {
                    Client client = new Client();
                    client.setPersonId(savedPerson.getId());
                    client.setLoyaltyStatus(clientDTO.getLoyaltyStatus());

                    return clientRepository.save(client)
                            .map(savedClient -> mapToResponseDTO(savedClient, savedPerson));
                })
                .doOnSuccess(result -> {
                    System.out.println("Client created successfully: " + result.getId());
                })
                .doOnError(error -> {
                    System.err.println("Error creating client: " + error.getMessage());
                });
    }

    /**
     * Retrieves a client by ID.
     *
     * @param id client identifier
     * @return client response or error
     * @author Kenmeugne Michèle
     * @date 18/12/2025
     */
    public Mono<ClientResponseDTO> getClientById(UUID id) {
        return clientRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Client", "id", id)))
                .flatMap(client -> personRepository.findById(client.getPersonId())
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Person", "id", client.getPersonId())))
                        .map(person -> mapToResponseDTO(client, person)));
    }

    /**
     * Retrieves all clients.
     *
     * @return flux of all clients
     * @author kenmeugne Michèle
     * @date 18/12/2025
     */
    public Flux<ClientResponseDTO> getAllClients() {
        return clientRepository.findAll()
                .flatMap(client -> personRepository.findById(client.getPersonId())
                        .map(person -> mapToResponseDTO(client, person)));
    }

    /**
     * Updates an existing client.
     *
     * @param id        client identifier
     * @param clientDTO updated client data
     * @return updated client response or error
     * @author Kenmeugne Michèle
     * @date 18/12/2025
     */
    public Mono<ClientResponseDTO> updateClient(UUID id, ClientDTO clientDTO) {
        return clientRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Client", "id", id)))
                .flatMap(existingClient -> personRepository.findById(existingClient.getPersonId())
                        .switchIfEmpty(
                                Mono.error(new ResourceNotFoundException("Person", "id", existingClient.getPersonId())))
                        .flatMap(existingPerson -> {
                            existingPerson.setLastName(clientDTO.getLastName());
                            existingPerson.setFirstName(clientDTO.getFirstName());
                            existingPerson.setPhone(clientDTO.getPhone());
                            existingPerson.setEmail(clientDTO.getEmail());
                            existingPerson.setPassword(clientDTO.getPassword());
                            existingPerson.setNationalId(clientDTO.getNationalId());
                            existingPerson.setPhotoCard(clientDTO.getPhotoCard());
                            existingPerson.setCriminalRecord(clientDTO.getCriminalRecord());

                            return personRepository.save(existingPerson)
                                    .flatMap(updatedPerson -> {
                                        existingClient.setLoyaltyStatus(clientDTO.getLoyaltyStatus());

                                        return clientRepository.save(existingClient)
                                                .map(updatedClient -> mapToResponseDTO(updatedClient, updatedPerson));
                                    });
                        }));
    }

    /**
     * Deletes a client by ID.
     *
     * @param id client identifier
     * @return void mono or error
     * @author Kenmeugne Michele
     * @date 18/12/2025
     */
    public Mono<Void> deleteClient(UUID id) {
        return clientRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Client", "id", id)))
                .flatMap(client -> clientRepository.deleteById(id)
                        .then(personRepository.deleteById(client.getPersonId())));
    }

    /**
     * Checks if a client with the given email already exists.
     *
     * @param email email to check
     * @return true if email exists, false otherwise
     */
    public Mono<Boolean> checkEmailExists(String email) {
        return personRepository.existsByEmail(email);
    }

    public Mono<Boolean> checkNationalIdExists(String nationalId) {
        return personRepository.existsByNationalId(nationalId);
    }

    /**
     * Maps Client and Person entities to ClientResponseDTO.
     *
     * @param client client entity
     * @param person person entity
     * @return client response DTO
     * @author Kenmeugne Michèle
     * @date 18/12/2025
     */
    private ClientResponseDTO mapToResponseDTO(Client client, Person person) {
        ClientResponseDTO responseDTO = new ClientResponseDTO();
        responseDTO.setId(client.getId());
        responseDTO.setPersonId(person.getId());
        responseDTO.setLastName(person.getLastName());
        responseDTO.setFirstName(person.getFirstName());
        responseDTO.setPhone(person.getPhone());
        responseDTO.setEmail(person.getEmail());
        responseDTO.setNationalId(person.getNationalId());
        responseDTO.setPhotoCard(person.getPhotoCard());
        responseDTO.setCriminalRecord(person.getCriminalRecord());
        responseDTO.setRating(person.getRating());
        responseDTO.setTotalDeliveries(person.getTotalDeliveries());
        responseDTO.setLoyaltyStatus(client.getLoyaltyStatus());
        return responseDTO;
    }
}