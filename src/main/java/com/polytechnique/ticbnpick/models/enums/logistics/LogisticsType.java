package com.polytechnique.ticbnpick.models.enums.logistics;

/**
 * Defines the type of logistics vehicle.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
public enum LogisticsType {

    BIKE("BIKE"),
    MOTORBIKE("MOTORBIKE"),
    CAR("CAR"),
    VAN("VAN"),
    TRUCK("TRUCK");

    private final String value;

    LogisticsType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static LogisticsType fromValue(String value) {
        for (LogisticsType type : LogisticsType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown logistics type: " + value);
    }
}