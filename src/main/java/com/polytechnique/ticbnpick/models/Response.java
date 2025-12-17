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
 * Represents a courier response to an announcement.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table("responses")
public class Response {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("announcement_id")
    private UUID announcement_id;

    @NotNull
    @Column("courier_id")
    private UUID courier_id;

    @NotNull
    @Column("arrival_time")
    private Instant arrival_time;

    @NotNull
    @Column("courier_amount")
    private Double courier_amount;

    @NotNull
    @Column("status")
    private String status;

    @Column("created_at")
    private Instant created_at;
}