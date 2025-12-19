package com.polytechnique.ticbnpick.models.enums.logistics;

/**
 * Defines the capacity class of a logistics vehicle.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
public enum LogisticsClass {

    STANDARD("STANDARD"),
    DAMAGED("DAMAGED"),
    PREMIUM("PREMIUM"),
    RESTRICTED("RESTRICTED");

    private final String value;

    LogisticsClass(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static LogisticsClass fromValue(String value) {
        for (LogisticsClass logisticsClass : LogisticsClass.values()) {
            if (logisticsClass.value.equalsIgnoreCase(value)) {
                return logisticsClass;
            }
        }
        throw new IllegalArgumentException("Unknown logistics class: " + value);
    }
}