package com.polytechnique.ticbnpick.dtos.packet;

import lombok.Data;

@Data
public class PacketDTO {
    private Double width;
    private Double length;
    private Boolean fragile;
    private String description;
    private String photoPacket;
    private Boolean isPerishable;
    private Double thickness;
}
