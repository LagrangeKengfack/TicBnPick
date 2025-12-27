package com.polytechnique.ticbnpick.services;

import com.polytechnique.ticbnpick.dtos.requests.AdminDeliveryPersonValidationRequest;
import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.models.PendingDeliveryPersonUpdate;
import com.polytechnique.ticbnpick.services.deliveryperson.LectureDeliveryPersonService;
import com.polytechnique.ticbnpick.services.deliveryperson.ModificationDeliveryPersonService;
import com.polytechnique.ticbnpick.services.person.LecturePersonService;
import com.polytechnique.ticbnpick.services.support.EmailService;
import com.polytechnique.ticbnpick.services.support.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminDeliveryPersonServiceTest {

    @Mock
    private LectureDeliveryPersonService lectureDeliveryPersonService;
    @Mock
    private ModificationDeliveryPersonService modificationDeliveryPersonService;
    @Mock
    private LecturePersonService lecturePersonService;
    @Mock
    private PendingDeliveryPersonUpdateService pendingUpdateService;
    @Mock
    private TokenService tokenService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private AdminDeliveryPersonService service;

    @Test
    void validateRegistration_Approve_ShouldUpdateStatusAndSendEmail() {
        // Arrange
        UUID dpId = UUID.randomUUID();
        UUID personId = UUID.randomUUID();
        AdminDeliveryPersonValidationRequest request = new AdminDeliveryPersonValidationRequest();
        request.setDeliveryPersonId(dpId);
        request.setApproved(true);

        DeliveryPerson dp = new DeliveryPerson();
        dp.setId(dpId);
        dp.setPersonId(personId);
        dp.setStatus("PENDING");

        when(lectureDeliveryPersonService.findById(dpId)).thenReturn(Mono.just(dp));
        when(modificationDeliveryPersonService.updateDeliveryPerson(any(DeliveryPerson.class))).thenReturn(Mono.just(dp));
        when(tokenService.generateToken(personId)).thenReturn(Mono.just("mock-token"));
        
        // Act & Assert
        StepVerifier.create(service.validateRegistration(request))
                .verifyComplete();

        verify(modificationDeliveryPersonService).updateDeliveryPerson(argThat(d -> d.getStatus().equals("APPROVED")));
        verify(tokenService).generateToken(personId);
        verify(emailService).sendSimpleMessage(any(), any(), contains("mock-token"));
    }

    @Test
    void reviewUpdate_Approve_ShouldApplyChanges() {
        // Arrange
        UUID updateId = UUID.randomUUID();
        PendingDeliveryPersonUpdate update = new PendingDeliveryPersonUpdate();
        update.setId(updateId);
        update.setChangesJson("{\"status\":\"ACTIVE\"}"); 

        when(pendingUpdateService.findById(updateId)).thenReturn(Mono.just(update));
        when(modificationDeliveryPersonService.updateDeliveryPerson(any())).thenReturn(Mono.empty());
        when(pendingUpdateService.deleteById(updateId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.reviewUpdate(updateId, true, "Ok"))
                .verifyComplete();

        verify(modificationDeliveryPersonService).updateDeliveryPerson(any());
        verify(pendingUpdateService).deleteById(updateId);
        verify(emailService).sendSimpleMessage(any(), any(), any());
    }
}
