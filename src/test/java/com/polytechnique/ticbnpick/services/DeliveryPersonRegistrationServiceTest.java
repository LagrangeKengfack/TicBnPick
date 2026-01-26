package com.polytechnique.ticbnpick.services;

import com.polytechnique.ticbnpick.dtos.requests.DeliveryPersonRegistrationRequest;
import com.polytechnique.ticbnpick.dtos.responses.DeliveryPersonRegistrationResponse;
import com.polytechnique.ticbnpick.exceptions.EmailAlreadyUsedException;
import com.polytechnique.ticbnpick.mappers.DeliveryPersonMapper;
import com.polytechnique.ticbnpick.models.Address;
import com.polytechnique.ticbnpick.models.DeliveryPerson;
import com.polytechnique.ticbnpick.models.Logistics;
import com.polytechnique.ticbnpick.models.Person;
import com.polytechnique.ticbnpick.models.enums.deliveryPerson.DeliveryPersonStatus;
import com.polytechnique.ticbnpick.services.address.CreationAddressService;
import com.polytechnique.ticbnpick.services.deliveryperson.CreationDeliveryPersonService;
import com.polytechnique.ticbnpick.services.logistics.CreationLogisticsService;
import com.polytechnique.ticbnpick.services.person.CreationPersonService;
import com.polytechnique.ticbnpick.services.person.LecturePersonService;
import com.polytechnique.ticbnpick.services.support.EmailService;
import com.polytechnique.ticbnpick.services.support.PasswordHasherService;
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
    private PasswordHasherService passwordHasherService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private DeliveryPersonRegistrationService service;

    @Test
    void register_Success_ShouldReturnPendingStatus() {
        // Arrange
        DeliveryPersonRegistrationRequest request = new DeliveryPersonRegistrationRequest();
        request.setEmail("test@test.com");
        request.setPassword("plainPassword");
        
        Person person = new Person();
        person.setEmail("test@test.com");
        
        Person savedPerson = new Person();
        savedPerson.setId(UUID.randomUUID());
        savedPerson.setEmail("test@test.com");

        DeliveryPerson deliveryPerson = new DeliveryPerson();
        DeliveryPerson savedDeliveryPerson = new DeliveryPerson();
        savedDeliveryPerson.setId(UUID.randomUUID());
        savedDeliveryPerson.setStatus(DeliveryPersonStatus.PENDING);

        Logistics logistics = new Logistics();
        Address address = new Address();

        when(validator.validate(request)).thenReturn(Mono.just(request));
        when(lecturePersonService.existsByEmail(anyString())).thenReturn(Mono.just(false));
        when(passwordHasherService.encode("plainPassword")).thenReturn("hashedPassword");
        
        when(mapper.toPerson(request)).thenReturn(person);
        when(mapper.toDeliveryPerson(request)).thenReturn(deliveryPerson);
        when(mapper.toLogistics(request)).thenReturn(logistics);
        when(mapper.toAddress(request)).thenReturn(address);

        when(creationPersonService.createPerson(any(Person.class))).thenReturn(Mono.just(savedPerson));
        when(creationDeliveryPersonService.createDeliveryPerson(any(DeliveryPerson.class))).thenReturn(Mono.just(savedDeliveryPerson));
        when(creationLogisticsService.createLogistics(any(Logistics.class))).thenReturn(Mono.just(new Logistics()));
        when(creationAddressService.createAddress(any(Address.class))).thenReturn(Mono.just(new Address()));
        
        // Act & Assert
        StepVerifier.create(service.register(request))
                .expectNextMatches(response -> 
                    "PENDING".equals(response.getStatus()) && 
                    response.getDeliveryPersonId() != null
                )
                .verifyComplete();

        verify(passwordHasherService).encode("plainPassword");
        verify(person).setPassword("hashedPassword");
        // verify(emailService).sendSimpleMessage(eq("test@test.com"), anyString(), anyString()); // If email is sent
    }

    @Test
    void register_EmailAlreadyExists_ShouldThrowError() {
        // Arrange
        DeliveryPersonRegistrationRequest request = new DeliveryPersonRegistrationRequest();
        request.setEmail("test@test.com");

        when(validator.validate(request)).thenReturn(Mono.just(request));
        when(lecturePersonService.existsByEmail(anyString())).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(service.register(request))
                .expectError(EmailAlreadyUsedException.class)
                .verify();
                
        verify(creationPersonService, never()).createPerson(any());
    }
}
