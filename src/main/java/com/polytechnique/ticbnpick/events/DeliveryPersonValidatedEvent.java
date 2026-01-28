package com.polytechnique.ticbnpick.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Event triggered when a delivery person is validated (approved/rejected).
 *
 * <p>Published to Kafka topic "delivery-person-validated" after admin decision.
 * Contains the delivery person ID and approval status for downstream consumers.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPersonValidatedEvent {
    private UUID deliveryPersonId;
    private boolean approved;
}
