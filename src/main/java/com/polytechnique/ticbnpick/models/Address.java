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
 * Represents a physical address in the system.
 * An address can be associated with clients, couriers or deliveries.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table("addresses")
public class Address {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("street")
    private String street;

    @NotNull
    @Column("city")
    private String city;

    @NotNull
    @Column("district")
    private String district;

    @NotNull
    @Column("country")
    private String country;

    @Column("latitude")
    private Double latitude;

    @Column("longitude")
    private Double longitude;

    @Column("description")
    private String description;
}