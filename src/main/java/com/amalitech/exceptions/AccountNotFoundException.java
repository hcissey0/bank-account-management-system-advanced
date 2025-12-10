package com.amalitech.exceptions;

/** Thrown when a requested account cannot be found. */
public class AccountNotFoundException extends Exception {

  /**
   * Creates a new account not found exception with the specified message.
   *
   * @param message the error message
   */
  public AccountNotFoundException(String message) {
    super(message);
  }
}
