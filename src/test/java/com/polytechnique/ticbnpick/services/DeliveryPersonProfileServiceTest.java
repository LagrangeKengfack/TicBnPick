package com.polytechnique.ticbnpick.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonUpdateRequest;
import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.models.Logistics;
import com.polytechnique.ticbnpick.models.PendingDeliveryPersonUpdate;
import com.polytechnique.ticbnpick.models.Person;
import com.polytechnique.ticbnpick.services.deliveryperson.LectureDeliveryPersonService;
import com.polytechnique.ticbnpick.services.deliveryperson.ModificationDeliveryPersonService;
import com.polytechnique.ticbnpick.services.logistics.LectureLogisticsService;
import com.polytechnique.ticbnpick.services.logistics.ModificationLogisticsService;
import com.polytechnique.ticbnpick.services.person.LecturePersonService;
import com.polytechnique.ticbnpick.services.person.ModificationPersonService;
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

@ExtendWith(MockitoExtension.class)
class DeliveryPersonProfileServiceTest {

    @Mock private LectureDeliveryPersonService lectureDeliveryPersonService;
    @Mock private ModificationDeliveryPersonService modificationDeliveryPersonService;
    @Mock private LectureLogisticsService lectureLogisticsService;
    @Mock private ModificationLogisticsService modificationLogisticsService;
    @Mock private LecturePersonService lecturePersonService;
    @Mock private ModificationPersonService modificationPersonService;
    @Mock private PendingDeliveryPersonUpdateService pendingUpdateService;
    @Mock private ObjectMapper objectMapper;

    @InjectMocks
    private DeliveryPersonProfileService service;

    @Test
    void updateProfile_SensitiveFields_ShouldCreatePendingUpdate() throws Exception {
        // Arrange
        UUID dpId = UUID.randomUUID();
        DeliveryPersonUpdateRequest request = new DeliveryPersonUpdateRequest();
        request.setCommercialRegister("NEW_REG"); // Sensitive
        request.setPhone("123456"); // Non-sensitive

        DeliveryPerson dp = new DeliveryPerson();
        dp.setId(dpId);
        dp.setPersonId(UUID.randomUUID());

        Person person = new Person();

        when(lectureDeliveryPersonService.findById(dpId)).thenReturn(Mono.just(dp));
        when(lecturePersonService.findById(dp.getPersonId())).thenReturn(Mono.just(person));
        when(modificationPersonService.updatePerson(any())).thenReturn(Mono.just(person));
        
        // Mock non-sensitive update calls (deferred in service, so might be skipped if no changes? Phone changed.)
        
        // Mock sensitive update
        when(objectMapper.writeValueAsString(request)).thenReturn("{}");
        when(pendingUpdateService.save(any())).thenReturn(Mono.just(new PendingDeliveryPersonUpdate()));

        // Act & Assert
        StepVerifier.create(service.updateProfile(dpId, request))
                .verifyComplete();

        verify(pendingUpdateService).save(any(PendingDeliveryPersonUpdate.class));
        verify(modificationPersonService).updatePerson(any()); // Phone should be updated
    }

    @Test
    void updateProfile_NoSensitiveFields_ShouldUpdateDirectly() {
        // Arrange
        UUID dpId = UUID.randomUUID();
        DeliveryPersonUpdateRequest request = new DeliveryPersonUpdateRequest();
        request.setPhone("123456"); // Non-sensitive only

        DeliveryPerson dp = new DeliveryPerson();
        dp.setId(dpId);
        dp.setPersonId(UUID.randomUUID());

        Person person = new Person();

        when(lectureDeliveryPersonService.findById(dpId)).thenReturn(Mono.just(dp));
        when(lecturePersonService.findById(dp.getPersonId())).thenReturn(Mono.just(person));
        when(modificationPersonService.updatePerson(any())).thenReturn(Mono.just(person));
        
        // We also need to mock Logistics service because updateNonSensitiveFields calls it
        when(lectureLogisticsService.findByDeliveryPersonId(dpId)).thenReturn(Mono.empty()); // Or Mono.just(logistics) if we want to test logistics update

        // Act & Assert
        StepVerifier.create(service.updateProfile(dpId, request))
                .verifyComplete();

        verify(pendingUpdateService, never()).save(any());
        verify(modificationPersonService).updatePerson(any());
    }
}
