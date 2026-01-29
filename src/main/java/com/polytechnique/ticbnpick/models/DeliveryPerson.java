package com.polytechnique.ticbnpick.models;

import com.polytechnique.ticbnpick.models.enums.deliveryPerson.DeliveryPersonStatus;
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
 * Represents a deliveryPerson in the system.
 * A deliveryPerson is a person who can perform deliveries.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("delivery_persons")
public class DeliveryPerson {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("person_id")
    private UUID personId;

    @NotNull
    @Column("commercial_register")
    private String commercialRegister;

    @NotNull
    @Column("commercial_name")
    private String commercialName;

    @NotNull
    @Column("taxpayer_number")
    private String taxpayerNumber;

    @NotNull
    @Column("status")
    private DeliveryPersonStatus status;

    @NotNull
    @Column("is_active")
    private Boolean isActive;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    @Column("commission_rate")
    private Double commissionRate;

    @Column("siret")
    private Double siret;

    @Column("longitude_gps")
    private Float longitudeGps;

    @Column("latitude_gps")
    private Float latitudeGps;

    @Column("remaining_deliveries")
    private Integer remainingDeliveries;

    @Column("failed_deliveries")
    private Integer failedDeliveries;

    @Column("subscription_id")
    private UUID subscriptionId;
}