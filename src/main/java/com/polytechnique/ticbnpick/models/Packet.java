package com.polytechnique.ticbnpick.models;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Represents a packet to be delivered.
 * A packet is associated with an announcement.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("packets")
public class Packet {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("announcement_id")
    private UUID announcement_id;

    @NotNull
    @Column("weight")
    private Double weight;

    @NotNull
    @Column("height")
    private Double height;

    @NotNull
    @Column("width")
    private Double width;

    @NotNull
    @Column("length")
    private Double length;

    @NotNull
    @Column("fragile")
    private Boolean fragile;

    @Column("description")
    private String description;

    @NotNull
    @Column("photo_packet")
    private String photoPacket;

    @NotNull
    @Column("perishable")
    private Boolean perishable;
}