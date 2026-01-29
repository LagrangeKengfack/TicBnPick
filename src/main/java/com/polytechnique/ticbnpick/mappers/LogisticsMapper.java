package com.polytechnique.ticbnpick.mappers;

import com.polytechnique.ticbnpick.dtos.requests.LogisticsCreateRequest;
import com.polytechnique.ticbnpick.models.Logistics;
import org.springframework.stereotype.Component;

/**
 * Mapper for Logistics entities and DTOs.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Component
public class LogisticsMapper {

    /**
     * Maps a LogisticsCreateRequest to a Logistics entity.
     *
     * @param request the DTO to map from
     * @return the mapped Logistics entity
     */
    public Logistics toEntity(LogisticsCreateRequest request) {
        if (request == null) {
            return null;
        }
        Logistics logistics = new Logistics();
        logistics.setLogisticsType(request.getType());
        logistics.setLogisticsClass(request.getLogisticsClass());
        logistics.setPlateNumber(request.getLicensePlate());
        return logistics;
    }
}
