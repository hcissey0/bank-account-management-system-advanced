package com.amalitech.exceptions;

/** Thrown when a transaction amount is invalid (e.g., negative or zero). */
public class InvalidAmountException extends Exception {

  /**
   * Creates a new invalid amount exception with the specified message.
   *
   * @param message the error message
   */
  public InvalidAmountException(String message) {
    super(message);
  }
}
