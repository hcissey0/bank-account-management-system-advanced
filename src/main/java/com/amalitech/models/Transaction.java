package com.amalitech.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Represents a financial transaction with auto-generated ID and timestamp. */
public class Transaction {

  private static int transactionCounter = 0;

  private final String transactionId;
  private final String accountNumber;
  private final String type;
  private final double amount;
  private final double balanceAfter;
  private final String timestamp;

  /**
   * Creates a new transaction record.
   *
   * @param accountNumber the account associated with the transaction
   * @param type the transaction type ("DEPOSIT" or "WITHDRAWAL")
   * @param amount the transaction amount
   * @param balanceAfterTransaction the account balance after this transaction
   */
  public Transaction(
      String accountNumber, String type, double amount, double balanceAfterTransaction) {
    this.transactionId = generateTransactionId();
    this.accountNumber = accountNumber;
    this.type = type;
    this.amount = amount;
    this.balanceAfter = balanceAfterTransaction;
    LocalDateTime time = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    this.timestamp = formatter.format(time);
  }

  private static String generateTransactionId() { // Generates a transactionId
    return "TXN" + String.format("%03d", ++transactionCounter);
  }

  public static int getTransactionCounter() {
    return transactionCounter;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public double getAmount() {
    return amount;
  }

  public double getBalanceAfter() {
    return balanceAfter;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public String getType() {
    return type;
  }
}
