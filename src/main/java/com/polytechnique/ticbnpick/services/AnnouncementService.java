package com.polytechnique.ticbnpick.services;

import com.polytechnique.ticbnpick.dtos.address.AddressDTO;
import com.polytechnique.ticbnpick.dtos.announcement.AnnouncementRequestDTO;
import com.polytechnique.ticbnpick.dtos.announcement.AnnouncementResponseDTO;
import com.polytechnique.ticbnpick.dtos.packet.PacketDTO;
import com.polytechnique.ticbnpick.models.Address;
import com.polytechnique.ticbnpick.models.Announcement;
import com.polytechnique.ticbnpick.models.Packet;
import com.polytechnique.ticbnpick.models.enums.announcement.AnnouncementStatus;
import com.polytechnique.ticbnpick.repositories.AddressRepository;
import com.polytechnique.ticbnpick.repositories.AnnouncementRepository;
import com.polytechnique.ticbnpick.events.AnnouncementPublishedEvent;
import com.polytechnique.ticbnpick.repositories.PacketRepository;
import com.polytechnique.ticbnpick.services.support.KafkaEventPublisher;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AddressRepository addressRepository;
    private final PacketRepository packetRepository;
    private final KafkaEventPublisher kafkaEventPublisher;

    @Transactional("connectionFactoryTransactionManager")
    public Mono<AnnouncementResponseDTO> createAnnouncement(AnnouncementRequestDTO request) {
        // 1. Save Packet
        Packet packet = new Packet();
        packet.setWidth(request.getPacket().getWidth());
        packet.setLength(request.getPacket().getLength());
        packet.setFragile(request.getPacket().getFragile());
        packet.setDescription(request.getPacket().getDescription());
        packet.setPhotoPacket(request.getPacket().getPhotoPacket());
        packet.setIsPerishable(request.getPacket().getIsPerishable());
        packet.setThickness(request.getPacket().getThickness());
        packet.setDesignation(request.getPacket().getDesignation());

        return packetRepository.save(packet).flatMap(savedPacket -> {
            // 2. Save Pickup Address
            Address pickup = maptoAddress(request.getPickupAddress());
            return addressRepository.save(pickup).flatMap(savedPickup -> {
                // 3. Save Delivery Address
                Address delivery = maptoAddress(request.getDeliveryAddress());
                return addressRepository.save(delivery).flatMap(savedDelivery -> {
                    // 4. Save Announcement
                    Announcement announcement = new Announcement();
                    announcement.setClientId(request.getClientId());
                    announcement.setPacketId(savedPacket.getId());
                    announcement.setPickupAddressId(savedPickup.getId());
                    announcement.setDeliveryAddressId(savedDelivery.getId());
                    announcement.setTitle(request.getTitle());
                    announcement.setDescription(request.getDescription());
                    announcement.setStatus(AnnouncementStatus.PUBLISHED); // Default status
                    announcement.setCreatedAt(Instant.now());
                    announcement.setRecipientName(request.getRecipientName());
                    announcement.setRecipientNumber(request.getRecipientNumber());
                    announcement.setRecipientEmail(request.getRecipientEmail());
                    announcement.setRecipientPhone(request.getRecipientPhone());
                    announcement.setShipperName(request.getShipperName());
                    announcement.setShipperEmail(request.getShipperEmail());
                    announcement.setShipperPhone(request.getShipperPhone());
                    announcement.setAmount(request.getAmount());

                    return announcementRepository.save(announcement)
                            .map(savedAnnouncement -> mapToResponse(savedAnnouncement, savedPickup, savedDelivery,
                                    savedPacket));
                });
            });
        });
    }

    public Flux<AnnouncementResponseDTO> getAllAnnouncements() {
        return announcementRepository.findAll().flatMap(this::populateDetails);
    }

    public Mono<AnnouncementResponseDTO> getAnnouncement(UUID id) {
        return announcementRepository.findById(id).flatMap(this::populateDetails);
    }

    public Mono<Void> deleteAnnouncement(UUID id) {
        return announcementRepository.deleteById(id);
    }

    private Mono<AnnouncementResponseDTO> populateDetails(Announcement announcement) {
        Mono<Address> pickupMono = addressRepository.findById(announcement.getPickupAddressId());
        Mono<Address> deliveryMono = addressRepository.findById(announcement.getDeliveryAddressId());
        Mono<Packet> packetMono = announcement.getPacketId() != null
                ? packetRepository.findById(announcement.getPacketId())
                : Mono.just(new Packet());

        return Mono.zip(pickupMono, deliveryMono, packetMono)
                .map(tuple -> mapToResponse(announcement, tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

    private Address maptoAddress(AddressDTO dto) {
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setCountry(dto.getCountry());
        address.setDescription(dto.getDescription());
        address.setType(dto.getType());
        return address;
    }

    private AnnouncementResponseDTO mapToResponse(Announcement announcement, Address pickup, Address delivery,
            Packet packet) {
        AnnouncementResponseDTO response = new AnnouncementResponseDTO();
        response.setId(announcement.getId());
        response.setClientId(announcement.getClientId());
        response.setTitle(announcement.getTitle());
        response.setDescription(announcement.getDescription());
        response.setStatus(announcement.getStatus());
        response.setCreatedAt(announcement.getCreatedAt());
        response.setUpdatedAt(announcement.getUpdatedAt());
        response.setRecipientName(announcement.getRecipientName());
        response.setRecipientNumber(announcement.getRecipientNumber());
        response.setRecipientEmail(announcement.getRecipientEmail());
        response.setRecipientPhone(announcement.getRecipientPhone());
        response.setShipperName(announcement.getShipperName());
        response.setShipperEmail(announcement.getShipperEmail());
        response.setShipperPhone(announcement.getShipperPhone());
        response.setAmount(announcement.getAmount());

        response.setPickupAddress(mapToAddressDTO(pickup));
        response.setDeliveryAddress(mapToAddressDTO(delivery));
        response.setPacket(mapToPacketDTO(packet));

        return response;
    }

    private AddressDTO mapToAddressDTO(Address address) {
        if (address == null)
            return null;
        AddressDTO dto = new AddressDTO();
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setDistrict(address.getDistrict());
        dto.setCountry(address.getCountry());
        dto.setDescription(address.getDescription());
        dto.setType(address.getType());
        return dto;
    }

    private PacketDTO mapToPacketDTO(Packet packet) {
        if (packet == null)
            return null;
        PacketDTO dto = new PacketDTO();
        dto.setWidth(packet.getWidth());
        dto.setLength(packet.getLength());
        dto.setFragile(packet.getFragile());
        dto.setDescription(packet.getDescription());
        dto.setPhotoPacket(packet.getPhotoPacket());
        dto.setIsPerishable(packet.getIsPerishable());
        dto.setThickness(packet.getThickness());
        dto.setDesignation(packet.getDesignation());
        return dto;
    }

    @Transactional("connectionFactoryTransactionManager")
    public Mono<AnnouncementResponseDTO> updateAnnouncement(UUID id, AnnouncementRequestDTO request) {
        return announcementRepository.findById(id)
                .flatMap(announcement -> {
                    announcement.setTitle(request.getTitle());
                    announcement.setDescription(request.getDescription());
                    announcement.setTitle(request.getTitle());
                    announcement.setDescription(request.getDescription());
                    announcement.setRecipientName(request.getRecipientName());
                    announcement.setRecipientNumber(request.getRecipientNumber());
                    announcement.setRecipientEmail(request.getRecipientEmail());
                    announcement.setRecipientPhone(request.getRecipientPhone());
                    announcement.setShipperName(request.getShipperName());
                    announcement.setShipperEmail(request.getShipperEmail());
                    announcement.setShipperPhone(request.getShipperPhone());
                    announcement.setAmount(request.getAmount());
                    announcement.setUpdatedAt(Instant.now());

                    return announcementRepository.save(announcement).flatMap(savedAnnouncement -> {
                        Mono<Packet> packetUpdate = Mono.just(new Packet());
                        if (savedAnnouncement.getPacketId() != null) {
                            packetUpdate = packetRepository.findById(savedAnnouncement.getPacketId())
                                    .flatMap(packet -> {
                                        if (request.getPacket() != null) {
                                            packet.setWidth(request.getPacket().getWidth());
                                            packet.setLength(request.getPacket().getLength());
                                            packet.setFragile(request.getPacket().getFragile());
                                            packet.setDescription(request.getPacket().getDescription());
                                            packet.setPhotoPacket(request.getPacket().getPhotoPacket());
                                            packet.setIsPerishable(request.getPacket().getIsPerishable());
                                            packet.setThickness(request.getPacket().getThickness());
                                            packet.setDesignation(request.getPacket().getDesignation());
                                            return packetRepository.save(packet);
                                        }
                                        return Mono.just(packet);
                                    });
                        }

                        Mono<Address> pickupUpdate = addressRepository.findById(savedAnnouncement.getPickupAddressId())
                                .flatMap(address -> {
                                    if (request.getPickupAddress() != null) {
                                        updateAddressFromDTO(address, request.getPickupAddress());
                                        return addressRepository.save(address);
                                    }
                                    return Mono.just(address);
                                });

                        Mono<Address> deliveryUpdate = addressRepository
                                .findById(savedAnnouncement.getDeliveryAddressId())
                                .flatMap(address -> {
                                    if (request.getDeliveryAddress() != null) {
                                        updateAddressFromDTO(address, request.getDeliveryAddress());
                                        return addressRepository.save(address);
                                    }
                                    return Mono.just(address);
                                });

                        return Mono.zip(packetUpdate, pickupUpdate, deliveryUpdate)
                                .map(tuple -> mapToResponse(savedAnnouncement, tuple.getT2(), tuple.getT3(),
                                        tuple.getT1()));
                    });
                });
    }

    private void updateAddressFromDTO(Address address, AddressDTO dto) {
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setCountry(dto.getCountry());
        address.setDescription(dto.getDescription());
        address.setType(dto.getType());
    }

    @Transactional("connectionFactoryTransactionManager")
    public Mono<AnnouncementResponseDTO> publishAnnouncement(UUID id) {
        return announcementRepository.findById(id)
                .flatMap(announcement -> {
                    announcement.setStatus(AnnouncementStatus.PUBLISHED);
                    return announcementRepository.save(announcement)
                            .flatMap(this::populateDetails)
                            .doOnSuccess(dto -> {
                                AnnouncementPublishedEvent event = new AnnouncementPublishedEvent();
                                event.setAnnouncement(dto);
                                kafkaEventPublisher.publishAnnouncementPublished(event);
                            });
                });
    }

    public Mono<Void> initiateSubscription(UUID announcementId, UUID deliveryPersonId) {
        return Mono.fromRunnable(() -> {
            com.polytechnique.ticbnpick.events.SubscriptionAttemptEvent event = new com.polytechnique.ticbnpick.events.SubscriptionAttemptEvent(
                    announcementId,
                    deliveryPersonId,
                    Instant.now());
            kafkaEventPublisher.publishSubscriptionAttempt(event);
        });
    }
}
