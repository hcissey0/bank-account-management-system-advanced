package com.amalitech.exceptions;

/** Base exception for all banking-related errors. */
public class BankException extends Exception {

  /**
   * Creates a new bank exception with the specified message.
   *
   * @param message the error message
   */
  public BankException(String message) {
    super(message);
  }
}
