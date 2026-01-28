package com.polytechnique.ticbnpick.controllers;

import com.polytechnique.ticbnpick.dtos.auth.AuthRequestDTO;
import com.polytechnique.ticbnpick.dtos.auth.AuthResponseDTO;
import com.polytechnique.ticbnpick.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public Mono<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        return authenticationService.login(request);
    }
}
