package com.polytechnique.ticbnpick.services;

import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonRegistrationRequest;
import com.polytechnique.ticbnpick.dtos.responses.DeliveryPersonRegistrationResponse;
import com.polytechnique.ticbnpick.mappers.DeliveryPersonMapper;
import com.polytechnique.ticbnpick.models.Address;
import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.models.Logistics;
import com.polytechnique.ticbnpick.models.Person;
import com.polytechnique.ticbnpick.services.address.CreationAddressService;
import com.polytechnique.ticbnpick.services.deliveryperson.CreationDeliveryPersonService;
import com.polytechnique.ticbnpick.services.logistics.CreationLogisticsService;
import com.polytechnique.ticbnpick.services.person.CreationPersonService;
import com.polytechnique.ticbnpick.services.person.LecturePersonService;
import com.polytechnique.ticbnpick.services.support.EmailService;
import com.polytechnique.ticbnpick.services.support.TokenService;
import com.polytechnique.ticbnpick.validators.DeliveryPersonRegistrationValidator;
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
class DeliveryPersonRegistrationServiceTest {

    @Mock
    private CreationPersonService creationPersonService;
    @Mock
    private LecturePersonService lecturePersonService;
    @Mock
    private CreationDeliveryPersonService creationDeliveryPersonService;
    @Mock
    private CreationLogisticsService creationLogisticsService;
    @Mock
    private CreationAddressService creationAddressService;
    @Mock
    private DeliveryPersonRegistrationValidator validator;
    @Mock
    private DeliveryPersonMapper mapper;
    @Mock
    private TokenService tokenService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private DeliveryPersonRegistrationService service;

    @Test
    void register_Success_ShouldReturnPendingStatus() {
        // Arrange
        DeliveryPersonRegistrationRequest request = new DeliveryPersonRegistrationRequest();
        request.setEmail("test@test.com");
        
        Person savedPerson = new Person();
        savedPerson.setId(UUID.randomUUID());
        savedPerson.setEmail("test@test.com");

        DeliveryPerson deliveryPerson = new DeliveryPerson();
        DeliveryPerson savedDeliveryPerson = new DeliveryPerson();
        savedDeliveryPerson.setId(UUID.randomUUID());
        savedDeliveryPerson.setStatus("PENDING");

        Logistics logistics = new Logistics();
        Address address = new Address();

        when(lecturePersonService.existsByEmail(anyString())).thenReturn(Mono.just(false));
        when(mapper.toPerson(any())).thenReturn(new Person());
        when(mapper.toDeliveryPerson(any())).thenReturn(deliveryPerson);
        when(mapper.toLogistics(any())).thenReturn(logistics);
        when(mapper.toAddress(any())).thenReturn(address);

        when(creationPersonService.createPerson(any())).thenReturn(Mono.just(savedPerson));
        when(creationDeliveryPersonService.createDeliveryPerson(any())).thenReturn(Mono.just(savedDeliveryPerson));
        when(creationLogisticsService.createLogistics(any())).thenReturn(Mono.just(new Logistics()));
        when(creationAddressService.createAddress(any())).thenReturn(Mono.just(new Address()));
        
        // Act & Assert
        StepVerifier.create(service.register(request))
                .expectNextMatches(response -> 
                    response.getStatus().equals("PENDING") && 
                    response.getDeliveryPersonId() != null
                )
                .verifyComplete();

        verify(emailService).sendSimpleMessage(eq("test@test.com"), anyString(), anyString());
    }

    @Test
    void register_EmailAlreadyExists_ShouldThrowError() {
        // Arrange
        DeliveryPersonRegistrationRequest request = new DeliveryPersonRegistrationRequest();
        request.setEmail("existing@test.com");

        when(lecturePersonService.existsByEmail("existing@test.com")).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(service.register(request))
                .expectError() // Expecting generic error or specific EmailAlreadyUsedException
                .verify();

        verify(creationPersonService, never()).createPerson(any());
    }
}
