package com.polytechnique.ticbnpick.services;

import com.polytechnique.ticbnpick.dtos.auth.AuthRequestDTO;
import com.polytechnique.ticbnpick.dtos.auth.AuthResponseDTO;
import com.polytechnique.ticbnpick.exceptions.InvalidCredentialsException;
import com.polytechnique.ticbnpick.repositories.ClientRepository;
import com.polytechnique.ticbnpick.repositories.PersonRepository;
import com.polytechnique.ticbnpick.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PersonRepository personRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Mono<AuthResponseDTO> login(AuthRequestDTO request) {
        return personRepository.findByEmail(request.getEmail())
                .filter(person -> passwordEncoder.matches(request.getPassword(), person.getPassword()))
                .flatMap(person -> {
                    String token = jwtUtil.generateToken(person.getEmail());
                    
                    // Create base response with Person data
                    AuthResponseDTO response = new AuthResponseDTO();
                    response.setToken(token);
                    response.setId(person.getId());
                    response.setLastName(person.getLast_name());
                    response.setFirstName(person.getFirst_name());
                    response.setEmail(person.getEmail());
                    response.setPhone(person.getPhone());
                    response.setPhotoCard(person.getPhotoCard());
                    response.setNationalId(person.getNationalId());
                    response.setCriminalRecord(person.getCriminalRecord());
                    response.setRating(person.getRating());
                    response.setTotalDeliveries(person.getTotalDeliveries());

                    // Try to fetch Client details
                    return clientRepository.findByPersonId(person.getId())
                            .map(client -> {
                                response.setClientId(client.getId());
                                response.setLoyaltyStatus(client.getLoyaltyStatus());
                                return response;
                            })
                            // If not a client, just return the response as is
                            .defaultIfEmpty(response);
                })
                .switchIfEmpty(Mono.error(new InvalidCredentialsException("Invalid credentials")));
    }
}
