package com.amalitech.exceptions;

/** Thrown when an account has insufficient funds for a withdrawal. */
public class InsufficientFundsException extends BankException {

  /**
   * Creates a new insufficient funds exception with the specified message.
   *
   * @param message the error message
   */
  public InsufficientFundsException(String message) {
    super(message);
  }
}
