package com.polytechnique.ticbnpick.services;

import com.polytechnique.ticbnpick.dtos.requests.AdminDeliveryPersonValidationRequest;
import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.models.Person;
import com.polytechnique.ticbnpick.models.enums.deliveryPerson.DeliveryPersonStatus;
import com.polytechnique.ticbnpick.services.deliveryperson.LectureDeliveryPersonService;
import com.polytechnique.ticbnpick.services.deliveryperson.ModificationDeliveryPersonService;
import com.polytechnique.ticbnpick.services.person.LecturePersonService;
import com.polytechnique.ticbnpick.services.support.EmailService;
import com.polytechnique.ticbnpick.services.support.KafkaEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AdminDeliveryPersonService.
 *
 * <p>Tests cover:
 * <ul>
 *   <li>Registration validation (approve/reject)</li>
 *   <li>Account suspension</li>
 *   <li>Account revocation</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class AdminDeliveryPersonServiceTest {

    @Mock
    private LectureDeliveryPersonService lectureDeliveryPersonService;
    @Mock
    private ModificationDeliveryPersonService modificationDeliveryPersonService;
    @Mock
    private LecturePersonService lecturePersonService;
    @Mock
    private EmailService emailService;
    @Mock
    private KafkaEventPublisher kafkaEventPublisher;

    @InjectMocks
    private AdminDeliveryPersonService service;

    @Test
    void validateRegistration_Approve_ShouldUpdateStatusToApproved() {
        // Arrange
        UUID dpId = UUID.randomUUID();
        UUID personId = UUID.randomUUID();
        AdminDeliveryPersonValidationRequest request = new AdminDeliveryPersonValidationRequest();
        request.setDeliveryPersonId(dpId);
        request.setApproved(true);

        DeliveryPerson dp = new DeliveryPerson();
        dp.setId(dpId);
        dp.setPersonId(personId);
        dp.setStatus(DeliveryPersonStatus.PENDING);

        Person person = new Person();
        person.setEmail("test@example.com");

        when(lectureDeliveryPersonService.findById(dpId)).thenReturn(Mono.just(dp));
        when(modificationDeliveryPersonService.updateDeliveryPerson(any(DeliveryPerson.class))).thenReturn(Mono.just(dp));
        when(lecturePersonService.findById(personId)).thenReturn(Mono.just(person));

        // Act & Assert
        StepVerifier.create(service.validateRegistration(request))
                .verifyComplete();

        verify(modificationDeliveryPersonService).updateDeliveryPerson(argThat(d -> d.getStatus() == DeliveryPersonStatus.APPROVED));
        verify(emailService).sendAccountApproved("test@example.com");
    }

    @Test
    void validateRegistration_Reject_ShouldUpdateStatusToRejected() {
        // Arrange
        UUID dpId = UUID.randomUUID();
        UUID personId = UUID.randomUUID();
        AdminDeliveryPersonValidationRequest request = new AdminDeliveryPersonValidationRequest();
        request.setDeliveryPersonId(dpId);
        request.setApproved(false);
        request.setReason("Invalid documents");

        DeliveryPerson dp = new DeliveryPerson();
        dp.setId(dpId);
        dp.setPersonId(personId);
        dp.setStatus(DeliveryPersonStatus.PENDING);

        Person person = new Person();
        person.setEmail("test@example.com");

        when(lectureDeliveryPersonService.findById(dpId)).thenReturn(Mono.just(dp));
        when(modificationDeliveryPersonService.updateDeliveryPerson(any(DeliveryPerson.class))).thenReturn(Mono.just(dp));
        when(lecturePersonService.findById(personId)).thenReturn(Mono.just(person));

        // Act & Assert
        StepVerifier.create(service.validateRegistration(request))
                .verifyComplete();

        verify(modificationDeliveryPersonService).updateDeliveryPerson(argThat(d -> d.getStatus() == DeliveryPersonStatus.REJECTED));
        verify(emailService).sendAccountRejected("test@example.com", "Invalid documents");
    }

    @Test
    void suspendDeliveryPerson_ShouldUpdateStatusToSuspended() {
        // Arrange
        UUID dpId = UUID.randomUUID();
        UUID personId = UUID.randomUUID();

        DeliveryPerson dp = new DeliveryPerson();
        dp.setId(dpId);
        dp.setPersonId(personId);
        dp.setStatus(DeliveryPersonStatus.APPROVED);

        Person person = new Person();
        person.setEmail("test@example.com");

        when(lectureDeliveryPersonService.findById(dpId)).thenReturn(Mono.just(dp));
        when(modificationDeliveryPersonService.updateDeliveryPerson(any(DeliveryPerson.class))).thenReturn(Mono.just(dp));
        when(lecturePersonService.findById(personId)).thenReturn(Mono.just(person));

        // Act & Assert
        StepVerifier.create(service.suspendDeliveryPerson(dpId))
                .verifyComplete();

        verify(modificationDeliveryPersonService).updateDeliveryPerson(argThat(d -> d.getStatus() == DeliveryPersonStatus.SUSPENDED));
        verify(emailService).sendAccountSuspended("test@example.com");
    }

    @Test
    void revokeDeliveryPerson_ShouldUpdateStatusToRejected() {
        // Arrange
        UUID dpId = UUID.randomUUID();
        UUID personId = UUID.randomUUID();

        DeliveryPerson dp = new DeliveryPerson();
        dp.setId(dpId);
        dp.setPersonId(personId);
        dp.setStatus(DeliveryPersonStatus.APPROVED);

        Person person = new Person();
        person.setEmail("test@example.com");

        when(lectureDeliveryPersonService.findById(dpId)).thenReturn(Mono.just(dp));
        when(modificationDeliveryPersonService.updateDeliveryPerson(any(DeliveryPerson.class))).thenReturn(Mono.just(dp));
        when(lecturePersonService.findById(personId)).thenReturn(Mono.just(person));

        // Act & Assert
        StepVerifier.create(service.revokeDeliveryPerson(dpId))
                .verifyComplete();

        verify(modificationDeliveryPersonService).updateDeliveryPerson(argThat(d -> d.getStatus() == DeliveryPersonStatus.REJECTED));
        verify(emailService).sendAccountRevoked("test@example.com");
    }
}
