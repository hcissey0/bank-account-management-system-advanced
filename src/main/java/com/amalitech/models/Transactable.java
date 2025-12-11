package com.amalitech.models;

import com.amalitech.constants.TransactionType;

/** Interface for processing financial transactions with validation. */
public interface Transactable {

  /**
   * Processes a transaction of the specified type.
   *
   * @param amount the transaction amount
   * @param type the transaction type
   * @throws Exception if the transaction fails
   */
  double processTransaction(double amount, TransactionType type) throws Exception;
}
