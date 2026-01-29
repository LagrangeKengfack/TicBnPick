package com.polytechnique.ticbnpick.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Event triggered when a password setup is requested.
 *
 * <p>Published when a user needs to set or reset their password.
 * Contains the person ID and email for notification purposes.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordSetupRequestedEvent {
    private UUID personId;
    private String email;
}
