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
 * Represents a payment related to a delivery.
 * A payment records financial information and payment status.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table("payments")
public class Payment {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("delivery_id")
    private UUID delivery_id;

    @NotNull
    @Column("amount")
    private Double amount;

    @NotNull
    @Column("payment_method")
    private String payment_method;

    @NotNull
    @Column("status")
    private String status;

    @Column("transaction_reference")
    private String transaction_reference;

    @Column("paid_at")
    private Instant paid_at;

    @Column("created_at")
    private Instant created_at;

    @Column("updated_at")
    private Instant updated_at;
}