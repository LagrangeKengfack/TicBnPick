package com.polytechnique.ticbnpick.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Event triggered when a delivery person is created.
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
    // TODO: Add any other necessary event data
}
