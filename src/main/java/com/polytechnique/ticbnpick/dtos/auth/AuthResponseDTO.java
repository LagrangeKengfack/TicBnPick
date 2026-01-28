package com.polytechnique.ticbnpick.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private java.util.UUID id;
    private String lastName;
    private String firstName;
    private String email;
    private String phone;
    private String photoCard;
    
    // Additional Person fields
    private String nationalId;
    private String criminalRecord;
    private Double rating;
    private Integer totalDeliveries;

    // Client specific fields
    private java.util.UUID clientId;
    private String loyaltyStatus;
}
