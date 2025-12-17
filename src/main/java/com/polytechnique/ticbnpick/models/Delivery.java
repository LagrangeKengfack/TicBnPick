package com.polytechnique.ticbnpick.models;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Represents a delivery execution.
 * Time windows are defined using minimum and maximum bounds.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("deliveries")
public class Delivery {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("announcement_id")
    private UUID announcementId;

    @NotNull
    @Column("packet_id")
    private UUID packetId;

    @NotNull
    @Column("courier_id")
    private UUID courierId;

    @NotNull
    @Column("sender_phone")
    private String senderPhone;

    @NotNull
    @Column("receiver_phone")
    private String receiverPhone;

    @NotNull
    @Column("pickup_time_min")
    private Instant pickupTimeMin;

    @NotNull
    @Column("pickup_time_max")
    private Instant pickupTimeMax;

    @NotNull
    @Column("delivery_time_min")
    private Instant deliveryTimeMin;

    @NotNull
    @Column("delivery_time_max")
    private Instant deliveryTimeMax;

    @Column("estimated_delivery")
    private Instant estimatedDelivery;

    @Column("distance_km")
    private Double distanceKm;

    @NotNull
    @Column("urgency")
    private Boolean urgency;

    @Column("delivery_note")
    private String deliveryNote;

    @NotNull
    @Column("status")
    private String status;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;
}