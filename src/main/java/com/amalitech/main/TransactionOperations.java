package com.amalitech.main;

import com.amalitech.exceptions.AccountNotFoundException;
import com.amalitech.exceptions.BankException;
import com.amalitech.models.*;
import com.amalitech.services.*;
import com.amalitech.utils.InputReader;

/** Handles all transaction-related operations. */
public class TransactionOperations {

  public static void processTransaction(
      AccountManager accountManager,
      TransactionManager transactionManager,
      InputReader inputReader) {
    System.out.println(
        "\n+---------------------+\n| PROCESS TRANSACTION |\n+---------------------+");
    String accountNumber = inputReader.readString("\nEnter Account number: ");

    try {
      Account account = accountManager.findAccount(accountNumber);
      account.displayAccountDetails();
      handleTransaction(account, transactionManager, inputReader);
    } catch (AccountNotFoundException e) {
      System.out.println(e.getMessage());
      inputReader.waitForEnter();
    }
  }

  public static void viewTransactionHistory(
      TransactionManager transactionManager, InputReader inputReader) {
    System.out.println(
        "\n"
            + "+--------------------------+\n"
            + "| VIEW TRANSACTION HISTORY |\n"
            + "+--------------------------+");
    String accountNumber = inputReader.readString("\nEnter Account number: ");
    transactionManager.viewTransactionsByAccount(accountNumber, inputReader);
  }

  private static void handleTransaction(
      Account account, TransactionManager transactionManager, InputReader inputReader) {
    int transactionType = promptTransactionType(inputReader);
    double amount = inputReader.readDouble("Enter amount: ", 0);

    Transaction transaction = buildTransaction(account, transactionType, amount);

    printTransactionConfirmation(transaction, account);

    if (confirm(inputReader)) {
      executeTransaction(account, transactionManager, transaction);
    } else {
      System.out.println("Transaction cancelled.");
    }
    inputReader.waitForEnter();
  }

  private static int promptTransactionType(InputReader inputReader) {
    System.out.println("\nSelect Transaction Type:\n1. Deposit\n2. Withdraw");
    return inputReader.readInt("Enter choice (1-2): ", 1, 2);
  }

  private static Transaction buildTransaction(Account account, int transactionType, double amount) {
    String type = transactionType == 1 ? "DEPOSIT" : "WITHDRAWAL";
    double newBalance =
        transactionType == 1 ? account.getBalance() + amount : account.getBalance() - amount;
    return new Transaction(account.getAccountNumber(), type, amount, newBalance);
  }

  private static void printTransactionConfirmation(Transaction transaction, Account account) {
    System.out.println(
        "\n"
            + "+--------------------------+\n"
            + "| Transaction Confirmation |\n"
            + "+--------------------------+");
    System.out.printf(
        "Transaction ID: %s\n"
            + "Account: %s\n"
            + "Type: %s\n"
            + "Amount: $%.2f\n"
            + "Previous Balance: $%.2f\n"
            + "New Balance: $%.2f\n"
            + "Date/Time: %s\n",
        transaction.getTransactionId(),
        account.getAccountNumber(),
        transaction.getType(),
        transaction.getAmount(),
        account.getBalance(),
        transaction.getBalanceAfter(),
        transaction.getTimestamp());
  }

  private static boolean confirm(InputReader inputReader) {
    return inputReader.readString("\nConfirm transaction? (y/n): ").toLowerCase().startsWith("y");
  }

  private static void executeTransaction(
      Account account, TransactionManager transactionManager, Transaction transaction) {
    try {
      account.processTransaction(transaction.getAmount(), transaction.getType());
      transactionManager.addTransaction(transaction);
      transactionManager.saveTransactions();
      System.out.printf(
          "%s Successful! New Balance: $%.2f\n", transaction.getType(), account.getBalance());
    } catch (BankException e) {
      System.out.println("Transaction failed: " + e.getMessage());
    }
  }
}
