package com.polytechnique.ticbnpick.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standard error response structure.
 * Used for all API error responses.
 *
 * @author Kengfack Lagrange
 * @date 18/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private String path;

  /**
   * Creates an error response with current timestamp.
   *
   * @param status HTTP status code
   * @param error error name
   * @param message error message
   * @param path request path
   * @author Kengfack Lagrange
   * @date 18/12/2025
   */
  public ErrorResponse(int status, String error, String message, String path) {
    this.timestamp = LocalDateTime.now();
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
  }
}