package com.polytechnique.ticbnpick.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Event triggered when a delivery person is validated (approved/rejected).
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPersonValidatedEvent {
    private UUID deliveryPersonId;
    private boolean isApproved;
    // TODO: Add any other necessary event data
}
