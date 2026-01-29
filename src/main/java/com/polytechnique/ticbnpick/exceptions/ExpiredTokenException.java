package com.polytechnique.ticbnpick.exceptions;

/**
 * Exception thrown when a token is expired.
 *
 * @author Kengfack Lagrange
 * @date 19/12/2025
 */
public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String message) {
        super(message);
    }
}
