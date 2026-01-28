package com.polytechnique.ticbnpick.dtos.announcement;

import com.polytechnique.ticbnpick.dtos.address.AddressDTO;
import com.polytechnique.ticbnpick.dtos.packet.PacketDTO;
import java.util.UUID;
import lombok.Data;

@Data
public class AnnouncementRequestDTO {
    private UUID clientId;
    private String title;
    private String description;

    private String recipientName;
    private String recipientNumber;
    private Float amount;
    
    private AddressDTO pickupAddress;
    private AddressDTO deliveryAddress;
    private PacketDTO packet;
}
