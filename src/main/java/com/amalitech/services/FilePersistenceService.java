package com.amalitech.services;

import com.amalitech.constants.AccountType;
import com.amalitech.constants.CustomerType;
import com.amalitech.constants.TransactionType;
import com.amalitech.models.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service for persisting and loading bank data using java.nio.file. Handles CSV-style file
 * operations for accounts, customers, and transactions.
 */
public class FilePersistenceService implements PersistenceService {

  private final String dataDir;
  private final String accountsFile;
  private final String customersFile;
  private final String transactionsFile;

  /** Default constructor using production data directory. */
  public FilePersistenceService() {
    this("src/main/resources/data/");
  }

  /** Constructor with custom data directory (for testing). */
  public FilePersistenceService(String dataDirectory) {
    this.dataDir = dataDirectory;
    this.accountsFile = dataDir + "accounts.txt";
    this.customersFile = dataDir + "customers.txt";
    this.transactionsFile = dataDir + "transactions.txt";
  }

  /**
   * Loads accounts from file using NIO and Stream API. File format:
   * accountType,accountNumber,customerId,balance,status
   *
   * @return HashMap of account number to Account object
   * @throws IOException if file operations fail
   */
  public HashMap<String, Account> loadAccounts(HashMap<String, Customer> customers)
      throws IOException {
    Path path = Paths.get(accountsFile);
    if (!Files.exists(path)) {
      return new HashMap<>();
    }

    try (Stream<String> lines = Files.lines(path)) {
      HashMap<String, Account> accounts =
          lines
              .skip(1) // Skip header
              .filter(line -> !line.trim().isEmpty())
              .map(line -> parseAccount(line, customers))
              .filter(account -> account != null)
              .collect(
                  Collectors.toMap(
                      Account::getAccountNumber, account -> account, (a1, a2) -> a1, HashMap::new));

      // Restore account counter to max ID found
      updateAccountCounter(accounts);
      return accounts;
    }
  }

  /**
   * Saves accounts to file using NIO.
   *
   * @param accounts HashMap of accounts to save
   * @throws IOException if file operations fail
   */
  public void saveAccounts(HashMap<String, Account> accounts) throws IOException {
    Path path = Paths.get(accountsFile);
    ensureDirectoryExists(path.getParent());

    List<String> lines = new ArrayList<>();
    lines.add("accountType,accountNumber,customerId,balance,status");

    accounts.values().stream().map(this::accountToCsv).forEach(lines::add);

    Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  /**
   * Loads customers from file using NIO and Stream API. File format:
   * customerType,customerId,name,age,contact,address
   *
   * @return HashMap of customer ID to Customer object
   * @throws IOException if file operations fail
   */
  public HashMap<String, Customer> loadCustomers() throws IOException {
    Path path = Paths.get(customersFile);
    if (!Files.exists(path)) {
      return new HashMap<>();
    }

    try (Stream<String> lines = Files.lines(path)) {
      HashMap<String, Customer> customers =
          lines
              .skip(1) // Skip header
              .filter(line -> !line.trim().isEmpty())
              .map(this::parseCustomer)
              .filter(customer -> customer != null)
              .collect(
                  Collectors.toMap(
                      Customer::getCustomerId, customer -> customer, (c1, c2) -> c1, HashMap::new));

      // Restore customer counter to max ID found
      updateCustomerCounter(customers);
      return customers;
    }
  }

  /**
   * Saves customers to file using NIO.
   *
   * @param customers HashMap of customers to save
   * @throws IOException if file operations fail
   */
  public void saveCustomers(HashMap<String, Customer> customers) throws IOException {
    Path path = Paths.get(customersFile);
    ensureDirectoryExists(path.getParent());

    List<String> lines = new ArrayList<>();
    lines.add("customerType,customerId,name,age,contact,address,email");

    customers.values().stream().map(this::customerToCsv).forEach(lines::add);

    Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  /**
   * Loads transactions from file using NIO and Stream API. File format:
   * transactionId,accountNumber,type,amount,balanceAfter,timestamp
   *
   * @return List of Transaction objects
   * @throws IOException if file operations fail
   */
  @Override
  public List<Transaction> loadTransactions() throws IOException {
    Path path = Paths.get(transactionsFile);
    if (!Files.exists(path)) {
      return new ArrayList<>();
    }

    try (Stream<String> lines = Files.lines(path)) {
      ArrayList<Transaction> transactions =
          lines
              .skip(1) // Skip header
              .filter(line -> !line.trim().isEmpty())
              .map(this::parseTransaction)
              .filter(transaction -> transaction != null)
              .collect(Collectors.toCollection(ArrayList::new));

      // Restore transaction counter to max ID found
      updateTransactionCounter(transactions);
      return transactions;
    }
  }

  /**
   * Saves transactions to file using NIO.
   *
   * @param transactions List of transactions to save
   * @throws IOException if file operations fail
   */
  public void saveTransactions(List<Transaction> transactions) throws IOException {
    Path path = Paths.get(transactionsFile);
    ensureDirectoryExists(path.getParent());

    List<String> lines = new ArrayList<>();
    lines.add("transactionId,accountNumber,type,amount,balanceAfter,timestamp");

    transactions.stream().map(this::transactionToCsv).forEach(lines::add);

    Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  // ==================== HELPER METHODS ====================

  private void ensureDirectoryExists(Path directory) throws IOException {
    if (!Files.exists(directory)) {
      Files.createDirectories(directory);
    }
  }

  private Account parseAccount(String line, HashMap<String, Customer> customers) {
    try {
      String[] parts = line.split(",");
      if (parts.length < 5) return null;

      String accountType = parts[0].trim();
      String accountNumber = parts[1].trim();
      String customerId = parts[2].trim();
      double balance = Double.parseDouble(parts[3].trim());
      String status = parts[4].trim();

      Customer customer = customers.get(customerId);
      if (customer == null) {
        System.err.println("Customer not found for account: " + accountNumber);
        return null;
      }

      Account account;
      AccountType type = AccountType.valueOf(accountType.toUpperCase());

      if (type == AccountType.SAVINGS) {
        account = new SavingsAccount(accountNumber, customer, balance);
      } else if (type == AccountType.CHECKING) {
        account = new CheckingAccount(accountNumber, customer, balance);
      } else {
        return null;
      }

      return account;

    } catch (Exception e) {
      System.err.println("Error parsing account line: " + line + " - " + e.getMessage());
      return null;
    }
  }

  private String accountToCsv(Account account) {
    return String.format(
        "%s,%s,%s,%.2f,%s",
        account.getAccountType(),
        account.getAccountNumber(),
        account.getCustomer().getCustomerId(),
        account.getBalance(),
        account.getStatus());
  }

  private Customer parseCustomer(String line) {
    try {
      String[] parts = line.split(",");
      if (parts.length < 7) return null;

      String customerType = parts[0].trim();
      String customerId = parts[1].trim();
      String name = parts[2].trim();
      int age = Integer.parseInt(parts[3].trim());
      String contact = parts[4].trim();
      String address = parts[5].trim();
      String email = parts[6].trim();

      Customer customer;
      CustomerType type = CustomerType.valueOf(customerType.toUpperCase());

      if (type == CustomerType.REGULAR) {
        customer = new RegularCustomer(customerId, name, age, contact, address, email);
      } else if (type == CustomerType.PREMIUM) {
        customer = new PremiumCustomer(customerId, name, age, contact, address, email);
      } else {
        return null;
      }

      return customer;

    } catch (Exception e) {
      System.err.println("Error parsing customer line: " + line + " - " + e.getMessage());
      return null;
    }
  }

  private String customerToCsv(Customer customer) {
    return String.format(
        "%s,%s,%s,%d,%s,%s,%s",
        customer.getCustomerType(),
        customer.getCustomerId(),
        customer.getName(),
        customer.getAge(),
        customer.getContact(),
        customer.getAddress(),
        customer.getEmail());
  }

  private Transaction parseTransaction(String line) {
    try {
      String[] parts = line.split(",");
      if (parts.length < 6) return null;

      String transactionId = parts[0].trim();
      String accountNumber = parts[1].trim();
      String typeStr = parts[2].trim();
      double amount = Double.parseDouble(parts[3].trim());
      double balanceAfter = Double.parseDouble(parts[4].trim());
      String timestamp = parts[5].trim();

      TransactionType type = TransactionType.valueOf(typeStr.toUpperCase());

      // Use constructor that preserves ID and timestamp from file
      return new Transaction(transactionId, accountNumber, type, amount, balanceAfter, timestamp);

    } catch (Exception e) {
      System.err.println("Error parsing transaction line: " + line + " - " + e.getMessage());
      return null;
    }
  }

  private String transactionToCsv(Transaction transaction) {
    return String.format(
        "%s,%s,%s,%.2f,%.2f,%s",
        transaction.getTransactionId(),
        transaction.getAccountNumber(),
        transaction.getType(),
        transaction.getAmount(),
        transaction.getBalanceAfter(),
        transaction.getTimestamp());
  }

  /**
   * Updates the customer counter based on loaded customers. Extracts the numeric part from customer
   * IDs and sets counter to max.
   */
  private void updateCustomerCounter(HashMap<String, Customer> customers) {
    int maxId =
        customers.keySet().stream()
            .map(id -> id.replace("CUS", ""))
            .filter(numStr -> numStr.matches("\\d+"))
            .mapToInt(Integer::parseInt)
            .max()
            .orElse(0);
    Customer.setCustomerCounter(maxId);
  }

  /**
   * Updates the account counter based on loaded accounts. Extracts the numeric part from account
   * numbers and sets counter to max.
   */
  private void updateAccountCounter(HashMap<String, Account> accounts) {
    int maxId =
        accounts.keySet().stream()
            .map(id -> id.replace("ACC", ""))
            .filter(numStr -> numStr.matches("\\d+"))
            .mapToInt(Integer::parseInt)
            .max()
            .orElse(0);
    Account.setAccountCounter(maxId);
  }

  /**
   * Updates the transaction counter based on loaded transactions. Extracts the numeric part from
   * transaction IDs and sets counter to max.
   */
  private void updateTransactionCounter(ArrayList<Transaction> transactions) {
    int maxId =
        transactions.stream()
            .map(Transaction::getTransactionId)
            .map(id -> id.replace("TXN", ""))
            .filter(numStr -> numStr.matches("\\d+"))
            .mapToInt(Integer::parseInt)
            .max()
            .orElse(0);
    Transaction.setTransactionCounter(maxId);
  }
}
