package com.polytechnique.ticbnpick.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a pending update request for a delivery person.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("pending_delivery_person_updates")
public class PendingDeliveryPersonUpdate {

    @Id
    @Column("id")
    private UUID id;

    @Column("delivery_person_id")
    private UUID deliveryPersonId;

    @Column("new_data_json")
    private String newDataJson;

    @Column("status")
    private String status;

    @Column("created_at")
    private Instant createdAt;
}
