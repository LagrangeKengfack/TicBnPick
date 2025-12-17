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
 * Represents a courier in the system.
 * A courier is a person who can perform deliveries.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("couriers")
public class DeliveryPerson {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("person_id")
    private UUID personId;

    @NotNull
    @Column("location")
    private String location;

    @NotNull
    @Column("commercial_register")
    private String commercialRegister;

    @NotNull
    @Column("commercial_name")
    private String commercialName;

    @NotNull
    @Column("nui")
    private String nui;

    @NotNull
    @Column("status")
    private String status;

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
}