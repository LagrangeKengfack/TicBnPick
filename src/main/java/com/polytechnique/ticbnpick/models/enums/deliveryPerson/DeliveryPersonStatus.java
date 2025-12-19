package com.polytechnique.ticbnpick.models.enums.deliveryPerson;

/**
 * Defines the lifecycle status of a deliveryPerson account.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
public enum DeliveryPersonStatus {

    PENDING("PENDING"),
    APPROVED("APPROVED"),
    SUSPENDED("SUSPENDED"),
    REJECTED("REJECTED");

    private final String value;

    DeliveryPersonStatus(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation stored in the database.
     *
     * @return deliveryPerson status value
     */
    public String getValue() {
        return value;
    }

    /**
     * Converts a database value to a DeliveryPersonStatus enum.
     *
     * @param value database value
     * @return matching DeliveryPersonStatus
     * @throws IllegalArgumentException if value is invalid
     */
    public static DeliveryPersonStatus fromValue(String value) {
        for (DeliveryPersonStatus status : DeliveryPersonStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown deliveryPerson status: " + value);
    }
}