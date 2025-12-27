package com.polytechnique.ticbnpick.services;

import com.polytechnique.ticbnpick.dtos.requests.SetPasswordRequest;
import com.polytechnique.ticbnpick.models.Person;
import com.polytechnique.ticbnpick.models.Token;
import com.polytechnique.ticbnpick.services.person.LecturePersonService;
import com.polytechnique.ticbnpick.services.person.ModificationPersonService;
import com.polytechnique.ticbnpick.services.support.PasswordHasherService;
import com.polytechnique.ticbnpick.services.support.TokenService;
import com.polytechnique.ticbnpick.validators.SetPasswordValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordSetupServiceTest {

    @Mock
    private TokenService tokenService;
    @Mock
    private LecturePersonService lecturePersonService;
    @Mock
    private ModificationPersonService modificationPersonService;
    @Mock
    private PasswordHasherService passwordHasherService;
    @Mock
    private SetPasswordValidator validator;

    @InjectMocks
    private PasswordSetupService service;

    @Test
    void setPassword_Success_ShouldHashAndSave() {
        // Arrange
        SetPasswordRequest request = new SetPasswordRequest();
        request.setToken("valid-token");
        request.setNewPassword("newPass123");

        Token tokenEntity = new Token();
        tokenEntity.setPersonId(UUID.randomUUID());

        Person person = new Person();

        when(tokenService.validateToken("valid-token")).thenReturn(Mono.empty()); // Success
        when(tokenService.getToken("valid-token")).thenReturn(Mono.just(tokenEntity)); // Assuming method exists
        when(lecturePersonService.findById(tokenEntity.getPersonId())).thenReturn(Mono.just(person));
        when(passwordHasherService.encode("newPass123")).thenReturn("hashedPass");
        when(modificationPersonService.updatePerson(any(Person.class))).thenReturn(Mono.just(person));
        when(tokenService.expireToken("valid-token")).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.setPassword(request))
                .verifyComplete();

        verify(passwordHasherService).encode("newPass123");
        verify(modificationPersonService).updatePerson(argThat(p -> "hashedPass".equals(p.getPassword())));
        verify(tokenService).expireToken("valid-token");
    }

    @Test
    void setPassword_TokenExpired_ShouldRejectAndNotify() {
        // Arrange
        SetPasswordRequest request = new SetPasswordRequest();
        request.setToken("expired-token");

        // Simulate validation error for expired token
        when(tokenService.validateToken("expired-token")).thenReturn(Mono.error(new RuntimeException("Token expired")));

        // Act & Assert
        StepVerifier.create(service.setPassword(request))
                .expectError(RuntimeException.class)
                .verify();

        // TODO: If requirement is to send email on rejection, mock EmailService and verify here.
        // Currently expecting error flow.
    }
}
