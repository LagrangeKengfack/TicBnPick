package com.polytechnique.ticbnpick.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Event triggered when a delivery person is created.
 *
 * <p>Published to Kafka topic "delivery-person-created" during registration.
 * Contains the delivery person ID and email for downstream consumers.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPersonCreatedEvent {
    private UUID deliveryPersonId;
    private String email;
}
