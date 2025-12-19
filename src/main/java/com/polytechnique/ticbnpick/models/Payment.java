package com.polytechnique.ticbnpick.models;

import com.polytechnique.ticbnpick.models.enums.payment.PaymentMethod;
import com.polytechnique.ticbnpick.models.enums.payment.PaymentStatus;
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
@Table("payments")
public class Payment {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("delivery_id")
    private UUID deliveryId;

    @NotNull
    @Column("amount")
    private Double amount;

    @NotNull
    @Column("payment_method")
    private PaymentMethod paymentMethod;

    @NotNull
    @Column("status")
    private PaymentStatus status;

    @Column("transaction_reference")
    private String transactionReference;

    @Column("paid_at")
    private Instant paidAt;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;
}