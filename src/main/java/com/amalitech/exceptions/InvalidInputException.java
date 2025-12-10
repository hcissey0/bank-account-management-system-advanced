package com.amalitech.exceptions;

/** Thrown when input validation fails. */
public class InvalidInputException extends Exception {

  /**
   * Creates a new invalid input exception with the specified message.
   *
   * @param message the error message
   */
  public InvalidInputException(String message) {
    super(message);
  }
}
