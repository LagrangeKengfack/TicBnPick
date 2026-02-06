package com.polytechnique.ticbnpick.models;

import com.polytechnique.ticbnpick.models.enums.announcement.AnnouncementStatus;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Represents a delivery announcement published by a client.
 * An announcement describes a delivery request.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("announcements")
public class Announcement {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("client_id")
    private UUID clientId;

    @Column("packet_id")
    private UUID packetId;

    @NotNull
    @Column("pickup_address_id")
    private UUID pickupAddressId;

    @NotNull
    @Column("delivery_address_id")
    private UUID deliveryAddressId;

    @NotNull
    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @NotNull
    @Column("status")
    private AnnouncementStatus status;

    @Column("duration")
    private Integer duration;

    @Column("recipient_name")
    private String recipientName;

    @Column("recipient_number")
    private String recipientNumber;

    @Column("recipient_email")
    private String recipientEmail;

    @Column("recipient_phone")
    private String recipientPhone;

    @Column("shipper_name")
    private String shipperName;

    @Column("shipper_email")
    private String shipperEmail;

    @Column("shipper_phone")
    private String shipperPhone;

    @Column("amount")
    private Float amount;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;
}