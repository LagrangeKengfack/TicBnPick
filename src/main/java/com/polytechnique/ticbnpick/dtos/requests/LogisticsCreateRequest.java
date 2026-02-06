package com.polytechnique.ticbnpick.dtos.requests;

import com.polytechnique.ticbnpick.models.enums.logistics.LogisticsClass;
import com.polytechnique.ticbnpick.models.enums.logistics.LogisticsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating logistics.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsCreateRequest {
    private LogisticsType logisticsType;
    private LogisticsClass logisticsClass;
    private String plateNumber;
    private Double luggageMaxCapacity;
    private String backPhoto;
    private String frontPhoto;
    private Double tankCapacity;
    private Integer totalSeatNumber;
    private String color;
}
