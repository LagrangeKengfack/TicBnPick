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
 * Represents a logistics vehicle owned by a courier.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table("logistics")
public class Logistics {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("courier_id")
    private UUID courier_id;

    @NotNull
    @Column("plate_number")
    private String plate_number;

    @NotNull
    @Column("logistics_photo")
    private String logistics_photo;

    @NotNull
    @Column("logistics_type")
    private String logistics_type;

    @NotNull
    @Column("logistics_class")
    private String logistics_class;

    @Column("rating")
    private Double rating;

    @Column("created_at")
    private Instant created_at;

    @Column("updated_at")
    private Instant updated_at;

    @Column("tank_capacity")
    private Double tank_capacity;

    @Column("luggage_max_capacity")
    private Double luggage_max_capacity;

    @Column("total_seat_number")
    private Integer total_seat_number;
}