package com.polytechnique.ticbnpick.dtos.address;

import com.polytechnique.ticbnpick.models.enums.address.AddressType;
import lombok.Data;

@Data
public class AddressDTO {
    private String street;
    private String city;
    private String district;
    private String country;
    private String description;
    private AddressType type;
}
