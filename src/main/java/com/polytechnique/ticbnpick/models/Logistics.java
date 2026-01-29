package com.polytechnique.ticbnpick.models;

import com.polytechnique.ticbnpick.models.enums.logistics.LogisticsClass;
import com.polytechnique.ticbnpick.models.enums.logistics.LogisticsType;
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
 * Represents a logistics vehicle owned by a deliveryPerson.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("logistics")
public class Logistics {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("delivery_person_id")
    private UUID deliveryPersonId;

    @NotNull
    @Column("plate_number")
    private String plateNumber;

    @NotNull
    @Column("logistics_type")
    private LogisticsType logisticsType;

    @NotNull
    @Column("logistics_class")
    private LogisticsClass logisticsClass;

    @Column("rating")
    private Double rating;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    @Column("tank_capacity")
    private Double tankCapacity;

    @Column("luggage_max_capacity")
    private Double luggageMaxCapacity;

    @Column("total_seat_number")
    private Integer totalSeatNumber;

    @Column("color")
    private String color;

    @Column("logistic_image")
    private String logisticImage;
}