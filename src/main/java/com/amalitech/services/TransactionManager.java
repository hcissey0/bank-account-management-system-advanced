package com.amalitech.services;

import com.amalitech.models.*;
import com.amalitech.utils.ConsoleTablePrinter;
import com.amalitech.utils.InputReader;
import com.amalitech.utils.TablePrinter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Manages a collection of transactions using ArrayList with file persistence. */
public class TransactionManager {

  private static final String DEPOSIT_TYPE = "DEPOSIT";
  private static final String WITHDRAWAL_TYPE = "WITHDRAWAL";

  private final List<Transaction> transactions;
  private final TablePrinter printer;
  private final PersistenceService persistenceService;

  public TransactionManager(PersistenceService persistenceService) {
    this.persistenceService = persistenceService;
    this.printer = new ConsoleTablePrinter();
    this.transactions = loadTransactionsFromFile();
  }

  /** Loads transactions from file on initialization. */
  private List<Transaction> loadTransactionsFromFile() {
    try {
      return persistenceService.loadTransactions();
    } catch (IOException e) {
      System.err.println("Warning: Could not load transactions from file: " + e.getMessage());
      return new ArrayList<>();
    }
  }

  /** Adds a transaction to the history. */
  public void addTransaction(Transaction transaction) {
    if (transaction == null) {
      System.out.println("Attempted to add null transaction");
      return;
    }
    transactions.add(transaction);
  }

  /** Calculates the total amount of all deposits using Stream API. */
  public double calculateTotalDeposits() {
    return transactions.stream()
        .filter(t -> t.getType().equalsIgnoreCase(DEPOSIT_TYPE))
        .mapToDouble(Transaction::getAmount)
        .sum();
  }

  /** Calculates the total amount of all withdrawals using Stream API. */
  public double calculateTotalWithdrawals() {
    return transactions.stream()
        .filter(t -> t.getType().equalsIgnoreCase(WITHDRAWAL_TYPE))
        .mapToDouble(Transaction::getAmount)
        .sum();
  }

  public int getTransactionCount() {
    return transactions.size();
  }

  /**
   * Displays a tabular view of all transactions.
   *
   * @param inputReader used to pause execution after display
   */
  public void viewAllTransactions(InputReader inputReader) {
    if (transactions.isEmpty()) {
      System.out.println("No transactions available.");
      inputReader.waitForEnter();
      return;
    }

    String[] headers = createTransactionHeaders();
    String[][] data = buildTransactionData(transactions);

    printer.printTable(headers, data);
    displayTransactionSummary(
        getTransactionCount(), calculateTotalDeposits(), calculateTotalWithdrawals());

    inputReader.waitForEnter();
  }

  /**
   * Displays transactions for a specific account using Stream API.
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

    List<Transaction> accountTransactions = getTransactionsListForAccount(accountNumber);

    if (accountTransactions.isEmpty()) {
      System.out.println("No transactions recorded for account: " + accountNumber);
      inputReader.waitForEnter();
      return;
    }

    String[] headers = createTransactionHeaders();
    String[][] data = buildTransactionData(accountTransactions);

    printer.printTable(headers, data);

    double totalDeposits = getTotalDeposits(accountNumber);
    double totalWithdrawals = getTotalWithdrawals(accountNumber);
    displayTransactionSummary(accountTransactions.size(), totalDeposits, totalWithdrawals);

    inputReader.waitForEnter();
  }

  /**
   * Returns all transactions for the specified account as an array (for backward compatibility).
   */
  public Transaction[] getTransactionsForAccount(String accountNumber) {
    return getTransactionsListForAccount(accountNumber).toArray(new Transaction[0]);
  }

  /** Returns all transactions for the specified account as a List using Stream API. */
  private List<Transaction> getTransactionsListForAccount(String accountNumber) {
    return transactions.stream().filter(t -> t.getAccountNumber().equals(accountNumber)).toList();
  }

  /** Returns total deposits for the specified account using Stream API. */
  public double getTotalDeposits(String accountNumber) {
    return transactions.stream()
        .filter(t -> t.getAccountNumber().equals(accountNumber))
        .filter(t -> t.getType().equalsIgnoreCase(DEPOSIT_TYPE))
        .mapToDouble(Transaction::getAmount)
        .sum();
  }

  /** Returns total withdrawals for the specified account using Stream API. */
  public double getTotalWithdrawals(String accountNumber) {
    return transactions.stream()
        .filter(t -> t.getAccountNumber().equals(accountNumber))
        .filter(t -> t.getType().equalsIgnoreCase(WITHDRAWAL_TYPE))
        .mapToDouble(Transaction::getAmount)
        .sum();
  }

  // ==================== HELPER METHODS ====================

  private String[] createTransactionHeaders() {
    return new String[] {"TRANSACTION ID", "ACCOUNT NUMBER", "TYPE", "AMOUNT", "DATE"};
  }

  /** Builds transaction data using Stream API. */
  private String[][] buildTransactionData(List<Transaction> transactionList) {
    return transactionList.stream()
        .map(
            tx ->
                new String[] {
                  tx.getTransactionId(),
                  tx.getAccountNumber(),
                  tx.getType().toUpperCase(),
                  formatAmount(tx.getType(), tx.getAmount()),
                  tx.getTimestamp()
                })
        .toArray(String[][]::new);
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

  /** Saves all transactions to file. */
  public void saveTransactions() {
    try {
      persistenceService.saveTransactions(transactions);
    } catch (IOException e) {
      System.err.println("Error saving transactions: " + e.getMessage());
    }
  }

  /** Returns the transactions list for persistence operations. */
  public List<Transaction> getTransactions() {
    return transactions;
  }
}
