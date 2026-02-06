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
        logistics.setLogisticsType(request.getLogisticsType());
        logistics.setLogisticsClass(request.getLogisticsClass());
        logistics.setPlateNumber(request.getPlateNumber());
        logistics.setBackPhoto(request.getBackPhoto());
        logistics.setFrontPhoto(request.getFrontPhoto());
        logistics.setLuggageMaxCapacity(request.getLuggageMaxCapacity());
        logistics.setTankCapacity(request.getTankCapacity());
        logistics.setTotalSeatNumber(request.getTotalSeatNumber());
        logistics.setColor(request.getColor());
        return logistics;
    }
}
