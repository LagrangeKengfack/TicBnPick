package com.polytechnique.ticbnpick.dtos.announcement;

import com.polytechnique.ticbnpick.dtos.address.AddressDTO;
import com.polytechnique.ticbnpick.dtos.packet.PacketDTO;
import com.polytechnique.ticbnpick.models.enums.announcement.AnnouncementStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class AnnouncementResponseDTO {
    private UUID id;
    private UUID clientId;
    private String title;
    private String description;
    private AnnouncementStatus status;

    private Instant createdAt;
    private Instant updatedAt;
    private String recipientName;
    private String recipientNumber;
    private Float amount;
    
    private AddressDTO pickupAddress;
    private AddressDTO deliveryAddress;
    private PacketDTO packet;
}
