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
 * Represents a delivery announcement published by a client.
 * An announcement describes a delivery request.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table("announcements")
public class Announcement {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("client_id")
    private UUID client_id;

    @NotNull
    @Column("pickup_address_id")
    private UUID pickup_address_id;

    @NotNull
    @Column("delivery_address_id")
    private UUID delivery_address_id;

    @NotNull
    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @NotNull
    @Column("status")
    private String status;

    @Column("price")
    private Double price;

    @Column("created_at")
    private Instant created_at;

    @Column("updated_at")
    private Instant updated_at;
}