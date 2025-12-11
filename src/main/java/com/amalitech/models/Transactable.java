package com.amalitech.models;

/** Interface for processing financial transactions with validation. */
public interface Transactable {

  /**
   * Processes a transaction of the specified type.
   *
   * @param amount the transaction amount
   * @param type the transaction type ("Deposit" or "Withdrawal")
   * @throws Exception if the transaction fails
   */
  double processTransaction(double amount, String type) throws Exception;
}
