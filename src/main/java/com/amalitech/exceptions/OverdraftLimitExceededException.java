package com.amalitech.exceptions;

/** Thrown when a withdrawal exceeds the overdraft limit. */
public class OverdraftLimitExceededException extends Exception {

  /**
   * Creates a new overdraft limit exceeded exception with the specified message.
   *
   * @param message the error message
   */
  public OverdraftLimitExceededException(String message) {
    super(message);
  }
}
