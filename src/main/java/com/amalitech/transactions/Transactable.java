package com.amalitech.transactions;

import com.amalitech.exceptions.BankException;

/** Interface for processing financial transactions with validation. */
public interface Transactable {

  /**
   * Processes a transaction of the specified type.
   *
   * @param amount the transaction amount
   * @param type the transaction type ("Deposit" or "Withdrawal")
   * @throws BankException if the transaction fails
   */
  void processTransaction(double amount, String type) throws BankException;

  /**
   * Validates the transaction amount for the specified type.
   *
   * @param amount the transaction amount
   * @param type the transaction type
   * @throws BankException if the amount is invalid
   */
  void validateAmount(double amount, String type) throws BankException;
}
