package com.amalitech.main;

import com.amalitech.constants.TransactionType;
import com.amalitech.exceptions.AccountNotFoundException;
import com.amalitech.exceptions.InvalidInputException;
import com.amalitech.models.*;
import com.amalitech.services.*;
import com.amalitech.utils.InputReader;
import com.amalitech.utils.ValidationUtils;

/** Handles all transaction-related operations. */
public class TransactionOperations {

  public static void processTransaction(
      AccountManager accountManager,
      TransactionManager transactionManager,
      InputReader inputReader) {
    System.out.println(
        "\n+---------------------+\n| PROCESS TRANSACTION |\n+---------------------+");
    String accountNumber;
    while (true) {
      accountNumber = inputReader.readString("\nEnter Account number: ");
      try {
        ValidationUtils.validateAccountNumber(accountNumber);
        break;
      } catch (InvalidInputException e) {
        System.out.println(e.getMessage());
      }
    }

    try {
      Account account = accountManager.findAccount(accountNumber);
      account.displayAccountDetails();
      handleTransaction(accountManager, account, transactionManager, inputReader);
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
    String accountNumber;
    while (true) {
      accountNumber = inputReader.readString("\nEnter Account number: ");
      try {
        ValidationUtils.validateAccountNumber(accountNumber);
        break;
      } catch (InvalidInputException e) {
        System.out.println(e.getMessage());
      }
    }
    transactionManager.viewTransactionsByAccount(accountNumber, inputReader);
  }

  public static void viewTransactionHistory(
      AccountManager accountManager,
      TransactionManager transactionManager,
      InputReader inputReader) {
    System.out.println(
        "\n"
            + "+---------------------------+\n"
            + "| VIEW TRANSACTION HISTORY |\n"
            + "+---------------------------+");
    String accountNumber;
    while (true) {
      accountNumber = inputReader.readString("\nEnter Account number: ");
      try {
        ValidationUtils.validateAccountNumber(accountNumber);
        break;
      } catch (InvalidInputException e) {
        System.out.println(e.getMessage());
      }
    }

    try {
      Account account = accountManager.findAccount(accountNumber);
      transactionManager.viewTransactionsByAccount(account.getAccountNumber(), inputReader);
    } catch (AccountNotFoundException e) {
      System.out.println(e.getMessage());
      inputReader.waitForEnter();
    }
  }

  private static void handleTransaction(
      AccountManager accountManager,
      Account account,
      TransactionManager transactionManager,
      InputReader inputReader) {
    int transactionType = promptTransactionType(inputReader);

    if (transactionType == 3) {
      performTransfer(accountManager, transactionManager, account, inputReader);
      return;
    }

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
    System.out.println("\nSelect Transaction Type:\n1. Deposit\n2. Withdraw\n3. Transfer");
    return inputReader.readInt("Enter choice (1-3): ", 1, 3);
  }

  private static void performTransfer(
      AccountManager accountManager,
      TransactionManager transactionManager,
      Account fromAccount,
      InputReader inputReader) {
    String toAccountNumber;
    while (true) {
      toAccountNumber = inputReader.readString("\nEnter Destination Account number: ");
      try {
        ValidationUtils.validateAccountNumber(toAccountNumber);
        if (fromAccount.getAccountNumber().equals(toAccountNumber)) {
          System.out.println("Source and destination accounts cannot be the same.");
          continue;
        }
        break;
      } catch (InvalidInputException e) {
        System.out.println(e.getMessage());
      }
    }

    try {
      Account toAccount = accountManager.findAccount(toAccountNumber);

      double amount = inputReader.readDouble("Enter amount to transfer: ", 0);

      if (inputReader.readString("Confirm transfer? (y/n): ").toLowerCase().startsWith("y")) {
        accountManager.transfer(fromAccount.getAccountNumber(), toAccountNumber, amount);

        // Record transactions
        Transaction debit =
            new Transaction(
                fromAccount.getAccountNumber(),
                TransactionType.TRANSFER_OUT,
                amount,
                fromAccount.getBalance());
        Transaction credit =
            new Transaction(
                toAccountNumber, TransactionType.TRANSFER_IN, amount, toAccount.getBalance());

        transactionManager.addTransaction(debit);
        transactionManager.addTransaction(credit);

        System.out.println("Transfer Successful!");
        System.out.println("New Source Balance: $" + fromAccount.getBalance());
      } else {
        System.out.println("Transfer cancelled.");
      }

    } catch (AccountNotFoundException e) {
      System.out.println(e.getMessage());
    } catch (Exception e) {
      System.out.println("Transfer failed: " + e.getMessage());
    }
    inputReader.waitForEnter();
  }

  private static Transaction buildTransaction(Account account, int transactionType, double amount) {
    TransactionType type =
        transactionType == 1 ? TransactionType.DEPOSIT : TransactionType.WITHDRAWAL;
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
    } catch (Exception e) {
      System.out.println("Transaction failed: " + e.getMessage());
    }
  }
}
