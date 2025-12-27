package com.polytechnique.ticbnpick.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for delivery person update.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPersonUpdateRequest {
    private String phone;
    private String commercialName;
    // Add other updatable fields as needed
}
