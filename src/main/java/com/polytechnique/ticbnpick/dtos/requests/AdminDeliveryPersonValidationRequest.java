package com.polytechnique.ticbnpick.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for admin validation of delivery person.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDeliveryPersonValidationRequest {
    private UUID deliveryPersonId;
    private boolean approved;
    private String rejectionReason;
}
