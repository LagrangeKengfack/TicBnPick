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
@RequiredArgsConstructor
@Table("deliveries")
public class Delivery {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("announcement_id")
    private UUID announcement_id;

    @NotNull
    @Column("packet_id")
    private UUID packet_id;

    @NotNull
    @Column("courier_id")
    private UUID courier_id;

    @NotNull
    @Column("sender_phone")
    private String sender_phone;

    @NotNull
    @Column("receiver_phone")
    private String receiver_phone;

    @NotNull
    @Column("pickup_time_min")
    private Instant pickup_time_min;

    @NotNull
    @Column("pickup_time_max")
    private Instant pickup_time_max;

    @NotNull
    @Column("delivery_time_min")
    private Instant delivery_time_min;

    @NotNull
    @Column("delivery_time_max")
    private Instant delivery_time_max;

    @Column("estimated_delivery")
    private Instant estimated_delivery;

    @Column("distance_km")
    private Double distance_km;

    @NotNull
    @Column("urgency")
    private Boolean urgency;

    @Column("delivery_note")
    private String delivery_note;

    @NotNull
    @Column("status")
    private String status;

    @Column("created_at")
    private Instant created_at;

    @Column("updated_at")
    private Instant updated_at;
}