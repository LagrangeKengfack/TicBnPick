package com.polytechnique.ticbnpick.models;

import com.polytechnique.ticbnpick.models.enums.response.ResponseStatus;
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
 * Represents a deliveryPerson response to an announcement.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("responses")
public class Response {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("announcement_id")
    private UUID announcementId;

    @NotNull
    @Column("delivery_person_id")
    private UUID deliveryPersonId;

    @NotNull
    @Column("arrival_time")
    private Instant arrivalTime;

    @NotNull
    @Column("delivery_person_amount")
    private Double deliveryPersonAmount;

    @NotNull
    @Column("status")
    private ResponseStatus status;

    @Column("created_at")
    private Instant createdAt;
}