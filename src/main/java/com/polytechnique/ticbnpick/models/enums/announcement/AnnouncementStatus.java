package com.polytechnique.ticbnpick.models.enums.announcement;

/**
 * Defines the lifecycle status of an announcement.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
public enum AnnouncementStatus {

    DRAFT("DRAFT"),
    PUBLISHED("PUBLISHED"),
    IN_NEGOTIATION("IN_NEGOTIATION"),
    ASSIGNED("ASSIGNED"),
    CANCELLED("CANCELLED"),
    COMPLETED("COMPLETED");

    private final String value;

    AnnouncementStatus(String value) {
        this.value = value;
    }

    /**
     * Returns the database value of the announcement status.
     *
     * @return announcement status value
     */
    public String getValue() {
        return value;
    }

    /**
     * Converts a string to an AnnouncementStatus enum.
     *
     * @param value database value
     * @return matching AnnouncementStatus
     * @throws IllegalArgumentException if value is unknown
     */
    public static AnnouncementStatus fromValue(String value) {
        for (AnnouncementStatus status : AnnouncementStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown announcement status: " + value);
    }
}