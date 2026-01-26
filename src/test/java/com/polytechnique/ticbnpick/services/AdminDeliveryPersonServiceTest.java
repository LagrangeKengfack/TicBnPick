package com.polytechnique.ticbnpick.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytechnique.ticbnpick.dtos.requests.AdminDeliveryPersonValidationRequest;
import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonUpdateRequest;
import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.models.Logistics;
import com.polytechnique.ticbnpick.models.PendingDeliveryPersonUpdate;
import com.polytechnique.ticbnpick.models.enums.deliveryPerson.DeliveryPersonStatus;
import com.polytechnique.ticbnpick.models.enums.logistics.LogisticsType;
import com.polytechnique.ticbnpick.services.deliveryperson.LectureDeliveryPersonService;
import com.polytechnique.ticbnpick.services.deliveryperson.ModificationDeliveryPersonService;
import com.polytechnique.ticbnpick.services.logistics.LectureLogisticsService;
import com.polytechnique.ticbnpick.services.logistics.ModificationLogisticsService;
import com.polytechnique.ticbnpick.services.person.LecturePersonService;
import com.polytechnique.ticbnpick.services.support.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
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
    private LectureLogisticsService lectureLogisticsService;
    @Mock
    private ModificationLogisticsService modificationLogisticsService;
    @Mock
    private EmailService emailService;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AdminDeliveryPersonService service;

    @Test
    void validateRegistration_Approve_ShouldUpdateStatus() {
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

        when(lectureDeliveryPersonService.findById(dpId)).thenReturn(Mono.just(dp));
        when(modificationDeliveryPersonService.updateDeliveryPerson(any(DeliveryPerson.class))).thenReturn(Mono.just(dp));
        
        // Act & Assert
        StepVerifier.create(service.validateRegistration(request))
                .verifyComplete();

        verify(modificationDeliveryPersonService).updateDeliveryPerson(argThat(d -> d.getStatus() == DeliveryPersonStatus.APPROVED));
        // verify(emailService).sendApproval(any());
    }

    @Test
    void reviewUpdate_Approve_ShouldApplyChanges() throws Exception {
        // Arrange
        UUID updateId = UUID.randomUUID();
        UUID dpId = UUID.randomUUID();
        PendingDeliveryPersonUpdate update = new PendingDeliveryPersonUpdate();
        update.setId(updateId);
        update.setDeliveryPersonId(dpId);
        update.setNewDataJson("{\"logisticsType\":\"VAN\"}"); 

        DeliveryPerson dp = new DeliveryPerson();
        dp.setId(dpId);
        
        Logistics logistics = new Logistics();
        logistics.setLogisticsType(LogisticsType.BIKE); // old value

        DeliveryPersonUpdateRequest requestDto = new DeliveryPersonUpdateRequest();
        requestDto.setLogisticsType("VAN");

        when(pendingUpdateService.findById(updateId)).thenReturn(Mono.just(update));
        when(objectMapper.readValue(anyString(), eq(DeliveryPersonUpdateRequest.class))).thenReturn(requestDto);
        when(lectureDeliveryPersonService.findById(dpId)).thenReturn(Mono.just(dp));
        when(modificationDeliveryPersonService.updateDeliveryPerson(any())).thenReturn(Mono.just(dp));
        when(lectureLogisticsService.findAllByCourierId(dpId)).thenReturn(Flux.just(logistics));
        when(modificationLogisticsService.updateLogistics(any())).thenReturn(Mono.just(logistics));
        when(pendingUpdateService.deleteById(updateId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.reviewUpdate(updateId, true, "Ok"))
                .verifyComplete();

        verify(modificationLogisticsService).updateLogistics(argThat(l -> l.getLogisticsType() == LogisticsType.VAN));
        verify(pendingUpdateService).deleteById(updateId);
    }
}
