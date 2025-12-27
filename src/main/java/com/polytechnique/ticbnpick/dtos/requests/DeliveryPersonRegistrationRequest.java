package com.polytechnique.ticbnpick.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for delivery person registration.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPersonRegistrationRequest {
    private String lastName;
    private String firstName;
    private String phone;
    private String email;
    private String nationalId;
    private String photoCard;
    private String commercialRegister;
    private String commercialName;
    private String nui;
    private Double commissionRate;
    private Double siret;
}
