package com.amalitech.main;

import com.amalitech.constants.TransactionType;
import com.amalitech.exceptions.AccountNotFoundException;
import com.amalitech.exceptions.InvalidInputException;
import com.amalitech.models.*;
import com.amalitech.services.*;
import com.amalitech.utils.ConsoleTablePrinter;
import com.amalitech.utils.InputReader;
import com.amalitech.utils.ValidationUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;

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

  public static void searchTransactions(
      TransactionManager transactionManager, InputReader inputReader) {
    System.out.println("\n+--------------------+\n| SEARCH TRANSACTIONS |\n+--------------------+");

    Predicate<Transaction> predicate = t -> true;

    predicate = predicate.and(getAccountNumberFilter(inputReader));
    predicate = predicate.and(getTransactionTypeFilter(inputReader));
    predicate = predicate.and(getAmountRangeFilter(inputReader));
    predicate = predicate.and(getDateRangeFilter(inputReader));

    List<Transaction> results = transactionManager.searchTransactions(predicate);
    displaySearchResults(results);
    inputReader.waitForEnter();
  }

  private static Predicate<Transaction> getAccountNumberFilter(InputReader inputReader) {
    if (!promptYesNo(inputReader, "Filter by Account Number?")) {
      return t -> true;
    }
    String tempAccountNumber;
    while (true) {
      tempAccountNumber = inputReader.readString("Enter Account Number: ");
      try {
        ValidationUtils.validateAccountNumber(tempAccountNumber);
        break;
      } catch (InvalidInputException e) {
        System.out.println(e.getMessage());
      }
    }
    String accountNumber = tempAccountNumber;
    return t -> t.getAccountNumber().equals(accountNumber);
  }

  private static Predicate<Transaction> getTransactionTypeFilter(InputReader inputReader) {
    if (!promptYesNo(inputReader, "Filter by Transaction Type?")) {
      return t -> true;
    }
    System.out.println("1. DEPOSIT\n2. WITHDRAWAL\n3. TRANSFER_IN\n4. TRANSFER_OUT");
    int typeChoice = inputReader.readInt("Select Type (1-4): ", 1, 4);
    TransactionType type =
        switch (typeChoice) {
          case 1 -> TransactionType.DEPOSIT;
          case 2 -> TransactionType.WITHDRAWAL;
          case 3 -> TransactionType.TRANSFER_IN;
          case 4 -> TransactionType.TRANSFER_OUT;
          default -> null;
        };
    return type != null ? t -> t.getType() == type : t -> true;
  }

  private static Predicate<Transaction> getAmountRangeFilter(InputReader inputReader) {
    if (!promptYesNo(inputReader, "Filter by Amount Range?")) {
      return t -> true;
    }
    double min = inputReader.readDouble("Enter Min Amount (0 for no min): ", 0);
    double max = inputReader.readDouble("Enter Max Amount (0 for no max): ", 0);

    Predicate<Transaction> p = t -> true;
    if (min > 0) {
      double finalMin = min;
      p = p.and(t -> t.getAmount() >= finalMin);
    }
    if (max > 0) {
      double finalMax = max;
      p = p.and(t -> t.getAmount() <= finalMax);
    }
    return p;
  }

  private static Predicate<Transaction> getDateRangeFilter(InputReader inputReader) {
    if (!promptYesNo(inputReader, "Filter by Date Range (Last N days)?")) {
      return t -> true;
    }
    int days = inputReader.readInt("Enter number of days (e.g., 7 for last week): ", 1, 3650);
    LocalDate cutoffDate = LocalDate.now().minusDays(days);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    return t -> {
      try {
        LocalDateTime txnTime = LocalDateTime.parse(t.getTimestamp(), formatter);
        return txnTime.toLocalDate().isAfter(cutoffDate.minusDays(1));
      } catch (Exception e) {
        return false;
      }
    };
  }

  private static void displaySearchResults(List<Transaction> results) {
    System.out.println("\n--- Search Results (" + results.size() + ") ---");
    if (results.isEmpty()) {
      System.out.println("No transactions found matching criteria.");
    } else {
      new ConsoleTablePrinter()
          .printTable(
              new String[] {"ID", "ACCOUNT", "TYPE", "AMOUNT", "DATE"},
              results.stream()
                  .map(
                      t ->
                          new String[] {
                            t.getTransactionId(),
                            t.getAccountNumber(),
                            t.getType().toString(),
                            String.format("$%.2f", t.getAmount()),
                            t.getTimestamp()
                          })
                  .toArray(String[][]::new));
    }
  }

  private static boolean promptYesNo(InputReader inputReader, String prompt) {
    return inputReader.readString(prompt + " (y/n): ").toLowerCase().startsWith("y");
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
