package com.polytechnique.ticbnpick.controllers;

import com.polytechnique.ticbnpick.dtos.requests.SetPasswordRequest;
import com.polytechnique.ticbnpick.services.PasswordSetupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controller for password setup.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordSetupController {

    private final PasswordSetupService passwordSetupService;

    @PostMapping("/setup-password")
    public Mono<ResponseEntity<Void>> setPassword(@Valid @RequestBody SetPasswordRequest request) {
        // TODO: Call service to set password
        return passwordSetupService.setPassword(request)
                .map(v -> ResponseEntity.ok().build());
    }
}
