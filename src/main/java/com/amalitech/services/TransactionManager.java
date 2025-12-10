package com.amalitech.services;

import com.amalitech.utils.ConsoleTablePrinter;
import com.amalitech.utils.InputReader;
import com.amalitech.utils.TablePrinter;
import com.amalitech.models.*;

/** Manages a collection of transactions with fixed capacity. */
public class TransactionManager {

  private static final int MAX_TRANSACTIONS = 200;
  private static final String DEPOSIT_TYPE = "DEPOSIT";
  private static final String WITHDRAWAL_TYPE = "WITHDRAWAL";

  private final Transaction[] transactions;
  private int transactionCount;
  private final TablePrinter printer;

  public TransactionManager() {
    this.transactions = new Transaction[MAX_TRANSACTIONS];
    this.transactionCount = 0;
    this.printer = new ConsoleTablePrinter();
  }

  /** Adds a transaction to the history if capacity allows. */
  public void addTransaction(Transaction transaction) {
    if (transaction == null) {
      System.out.println("Attempted to add null transaction");
      return;
    }

    if (transactionCount >= MAX_TRANSACTIONS) {
      System.out.println("Transaction limit reached. Cannot add more transactions.");
      return;
    }

    this.transactions[this.transactionCount++] = transaction;
  }

  /** Calculates the total amount of all deposits. */
  public double calculateTotalDeposits() {
    return calculateTotalByType(DEPOSIT_TYPE);
  }

  /** Calculates the total amount of all withdrawals. */
  public double calculateTotalWithdrawals() {
    return calculateTotalByType(WITHDRAWAL_TYPE);
  }

  public int getTransactionCount() {
    return this.transactionCount;
  }

  /**
   * Displays a tabular view of all transactions.
   *
   * @param inputReader used to pause execution after display
   */
  public void viewAllTransactions(InputReader inputReader) {
    if (isTransactionListEmpty(inputReader)) {
      return;
    }

    String[] headers = createTransactionHeaders();
    String[][] data = buildTransactionData(transactions, transactionCount);

    printer.printTable(headers, data);
    displayTransactionSummary(
        transactionCount, calculateTotalDeposits(), calculateTotalWithdrawals());

    waitForUserInput(inputReader);
  }

  /**
   * Displays transactions for a specific account.
   *
   * @param accountNumber the account to filter by
   * @param inputReader used to pause execution after display
   */
  public void viewTransactionsByAccount(String accountNumber, InputReader inputReader) {
    if (accountNumber == null || accountNumber.trim().isEmpty()) {
      System.out.println("Invalid account number provided");
      inputReader.waitForEnter();
      return;
    }

    Transaction[] accountTransactions = filterTransactionsByAccount(accountNumber);
    int count = countNonNullTransactions(accountTransactions);

    if (count == 0) {
      System.out.println("No transactions recorded for account: " + accountNumber);
      inputReader.waitForEnter();
      return;
    }

    String[] headers = createTransactionHeaders();
    String[][] data = buildTransactionData(accountTransactions, count);

    printer.printTable(headers, data);

    double totalDeposits = calculateTotalByTypeForAccount(accountNumber, DEPOSIT_TYPE);
    double totalWithdrawals = calculateTotalByTypeForAccount(accountNumber, WITHDRAWAL_TYPE);
    displayTransactionSummary(count, totalDeposits, totalWithdrawals);

    waitForUserInput(inputReader);
  }

  /** Returns all transactions for the specified account. */
  public Transaction[] getTransactionsForAccount(String accountNumber) {
    return filterTransactionsByAccount(accountNumber);
  }

  /** Returns total deposits for the specified account. */
  public double getTotalDeposits(String accountNumber) {
    return calculateTotalByTypeForAccount(accountNumber, DEPOSIT_TYPE);
  }

  /** Returns total withdrawals for the specified account. */
  public double getTotalWithdrawals(String accountNumber) {
    return calculateTotalByTypeForAccount(accountNumber, WITHDRAWAL_TYPE);
  }

  // ==================== HELPER METHODS ====================

  private double calculateTotalByType(String type) {
    double total = 0;
    for (int i = 0; i < transactionCount; i++) {
      if (transactions[i] != null && transactions[i].getType().equalsIgnoreCase(type)) {
        total += transactions[i].getAmount();
      }
    }
    return total;
  }

  private Transaction[] filterTransactionsByAccount(String accountNumber) {
    Transaction[] filtered = new Transaction[transactionCount];
    int index = 0;

    for (int i = transactionCount - 1; i >= 0; i--) {
      if (transactions[i] != null && transactions[i].getAccountNumber().equals(accountNumber)) {
        filtered[index++] = transactions[i];
      }
    }

    return filtered;
  }

  private int countNonNullTransactions(Transaction[] transactionArray) {
    int count = 0;
    for (Transaction tx : transactionArray) {
      if (tx != null) {
        count++;
      }
    }
    return count;
  }

  private String[] createTransactionHeaders() {
    return new String[] {"TRANSACTION ID", "ACCOUNT NUMBER", "TYPE", "AMOUNT", "DATE"};
  }

  private String[][] buildTransactionData(Transaction[] transactionArray, int count) {
    String[][] data = new String[count][5];
    int rowIndex = 0;

    for (int i = 0; i < transactionArray.length && rowIndex < count; i++) {
      Transaction tx = transactionArray[i];
      if (tx != null) {
        data[rowIndex][0] = tx.getTransactionId();
        data[rowIndex][1] = tx.getAccountNumber();
        data[rowIndex][2] = tx.getType().toUpperCase();
        data[rowIndex][3] = formatAmount(tx.getType(), tx.getAmount());
        data[rowIndex][4] = tx.getTimestamp();
        rowIndex++;
      }
    }

    return data;
  }

  private String formatAmount(String type, double amount) {
    String prefix = type.equalsIgnoreCase(DEPOSIT_TYPE) ? "+$" : "-$";
    return String.format("%s%.2f", prefix, amount);
  }

  private void displayTransactionSummary(int count, double totalDeposits, double totalWithdrawals) {
    System.out.println("Number of transactions: " + count);
    System.out.println(String.format("Total Deposits: $%.2f", totalDeposits));
    System.out.println(String.format("Total Withdrawals: $%.2f", totalWithdrawals));
  }

  private double calculateTotalByTypeForAccount(String accountNumber, String type) {
    double total = 0;
    for (int i = 0; i < transactionCount; i++) {
      if (transactions[i] != null
          && transactions[i].getAccountNumber().equals(accountNumber)
          && transactions[i].getType().equalsIgnoreCase(type)) {
        total += transactions[i].getAmount();
      }
    }
    return total;
  }

  private boolean isTransactionListEmpty(InputReader inputReader) {
    if (transactionCount == 0) {
      System.out.println("No transactions available.");
      inputReader.waitForEnter();
      return true;
    }
    return false;
  }

  private void waitForUserInput(InputReader inputReader) {
    inputReader.waitForEnter();
  }
}
