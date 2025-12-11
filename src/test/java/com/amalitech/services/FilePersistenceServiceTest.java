package com.amalitech.services;

import static org.junit.jupiter.api.Assertions.*;

import com.amalitech.constants.AccountType;
import com.amalitech.constants.CustomerType;
import com.amalitech.constants.TransactionType;
import com.amalitech.models.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for FilePersistenceService verifying NIO-based file operations. */
class FilePersistenceServiceTest {

  private FilePersistenceService persistenceService;
  private static final String TEST_DATA_DIR = "src/test/resources/data/";

  @BeforeEach
  void setUp() throws IOException {
    // Create test data directory if it doesn't exist
    Path dataDir = Paths.get(TEST_DATA_DIR);
    if (!Files.exists(dataDir)) {
      Files.createDirectories(dataDir);
    }
    persistenceService = new FilePersistenceService(TEST_DATA_DIR);
  }

  @AfterEach
  void tearDown() throws IOException {
    // Clean up test files after each test
    deleteFileIfExists(TEST_DATA_DIR + "accounts.txt");
    deleteFileIfExists(TEST_DATA_DIR + "customers.txt");
    deleteFileIfExists(TEST_DATA_DIR + "transactions.txt");

    // Recreate with headers
    Files.writeString(
        Paths.get(TEST_DATA_DIR + "accounts.txt"),
        "accountType,accountNumber,customerId,balance,status\n");
    Files.writeString(
        Paths.get(TEST_DATA_DIR + "customers.txt"),
        "customerType,customerId,name,age,contact,address,email\n");
    Files.writeString(
        Paths.get(TEST_DATA_DIR + "transactions.txt"),
        "transactionId,accountNumber,type,amount,balanceAfter,timestamp\n");
  }

  private void deleteFileIfExists(String filePath) throws IOException {
    Path path = Paths.get(filePath);
    if (Files.exists(path)) {
      Files.delete(path);
    }
  }

  @Test
  void testSaveAndLoadCustomers() throws IOException {
    // Create test customers
    HashMap<String, Customer> customers = new HashMap<>();
    Customer regular =
        new RegularCustomer("John Doe", 30, "555-1234", "123 Main St", "john@example.com");
    Customer premium =
        new PremiumCustomer("Jane Smith", 40, "555-5678", "456 Oak Ave", "jane@example.com");
    customers.put(regular.getCustomerId(), regular);
    customers.put(premium.getCustomerId(), premium);

    // Save customers
    persistenceService.saveCustomers(customers);

    // Load customers
    HashMap<String, Customer> loadedCustomers = persistenceService.loadCustomers();

    // Verify count and data integrity
    assertEquals(2, loadedCustomers.size());

    // Find customers by name since IDs change
    Customer loadedRegular =
        loadedCustomers.values().stream()
            .filter(c -> c.getName().equals("John Doe"))
            .findFirst()
            .orElse(null);

    assertNotNull(loadedRegular);
    assertEquals(30, loadedRegular.getAge());
    assertEquals(CustomerType.REGULAR, loadedRegular.getCustomerType());
    assertEquals("555-1234", loadedRegular.getContact());
    assertEquals("john@example.com", loadedRegular.getEmail());
  }

  @Test
  void testSaveAndLoadAccountsWithPreExistingCustomers() throws IOException {
    // Note: This test verifies that account save/load works within a single session
    // where customer references are maintained
    HashMap<String, Customer> customers = new HashMap<>();
    Customer customer =
        new RegularCustomer("Test User", 25, "555-0000", "789 Pine St", "test@example.com");
    customers.put(customer.getCustomerId(), customer);

    // Create test accounts with the customer reference
    HashMap<String, Account> accounts = new HashMap<>();
    Account savings = new SavingsAccount(customer, 1000.0);
    Account checking = new CheckingAccount(customer, 500.0);
    accounts.put(savings.getAccountNumber(), savings);
    accounts.put(checking.getAccountNumber(), checking);

    // Save accounts
    persistenceService.saveAccounts(accounts);

    // Load accounts with same customer map (simulates single session)
    HashMap<String, Account> loadedAccounts = persistenceService.loadAccounts(customers);

    // Verify count and data integrity
    assertEquals(2, loadedAccounts.size());

    // Find savings account by balance and type
    Account loadedSavings =
        loadedAccounts.values().stream()
            .filter(a -> a.getAccountType() == AccountType.SAVINGS)
            .findFirst()
            .orElse(null);

    assertNotNull(loadedSavings);
    assertEquals(1000.0, loadedSavings.getBalance(), 0.01);
    assertEquals(AccountType.SAVINGS, loadedSavings.getAccountType());
  }

  @Test
  void testSaveAndLoadTransactions() throws IOException {
    // Create test transactions
    ArrayList<Transaction> transactions = new ArrayList<>();
    transactions.add(new Transaction("ACC001", TransactionType.DEPOSIT, 100.0, 100.0));
    transactions.add(new Transaction("ACC001", TransactionType.WITHDRAWAL, 50.0, 50.0));
    transactions.add(new Transaction("ACC002", TransactionType.DEPOSIT, 200.0, 200.0));

    // Save transactions
    persistenceService.saveTransactions(transactions);

    // Load transactions
    List<Transaction> loadedTransactions = persistenceService.loadTransactions();

    // Verify
    assertEquals(3, loadedTransactions.size());

    // Verify first transaction
    Transaction first = loadedTransactions.get(0);
    assertEquals("ACC001", first.getAccountNumber());
    assertEquals(TransactionType.DEPOSIT, first.getType());
    assertEquals(100.0, first.getAmount(), 0.01);
  }

  @Test
  void testLoadEmptyFiles() throws IOException {
    // Ensure files are empty (only headers)
    HashMap<String, Customer> customers = persistenceService.loadCustomers();
    HashMap<String, Account> accounts = persistenceService.loadAccounts(customers);
    List<Transaction> transactions = persistenceService.loadTransactions();

    // Verify all collections are empty
    assertTrue(customers.isEmpty());
    assertTrue(accounts.isEmpty());
    assertTrue(transactions.isEmpty());
  }

  @Test
  void testFileSaveCreatesFileWithCorrectFormat() throws IOException {
    // Test that save operations create well-formed CSV files
    HashMap<String, Customer> customers = new HashMap<>();
    Customer customer =
        new RegularCustomer("Alice", 28, "555-1111", "321 Elm St", "alice@example.com");
    customers.put(customer.getCustomerId(), customer);

    // Save and verify file exists and has content
    persistenceService.saveCustomers(customers);

    Path customersFile = Paths.get(TEST_DATA_DIR + "customers.txt");
    assertTrue(Files.exists(customersFile));

    List<String> lines = Files.readAllLines(customersFile);
    assertTrue(lines.size() >= 2); // Header + at least one data line
    assertTrue(lines.get(0).contains("customerType")); // Header check
    assertTrue(lines.get(1).contains("Alice")); // Data check
  }

  @Test
  void testSaveWithNullData() {
    // Should not throw exception
    assertDoesNotThrow(
        () -> {
          persistenceService.saveCustomers(new HashMap<>());
          persistenceService.saveAccounts(new HashMap<>());
          persistenceService.saveTransactions(new ArrayList<>());
        });
  }

  @Test
  void testTransactionTimestampPreservation() throws IOException {
    // Create a transaction with a specific timestamp using the persistence constructor
    String specificTimestamp = "05-12-2025 14:30:45";
    Transaction original =
        new Transaction(
            "TXN999", "ACC001", TransactionType.DEPOSIT, 500.0, 1500.0, specificTimestamp);

    // Save transaction
    ArrayList<Transaction> transactions = new ArrayList<>();
    transactions.add(original);
    persistenceService.saveTransactions(transactions);

    // Load transaction back
    List<Transaction> loaded = persistenceService.loadTransactions();

    // Verify timestamp was preserved
    assertFalse(loaded.isEmpty());
    Transaction loadedTransaction = loaded.get(0);
    assertEquals(
        specificTimestamp,
        loadedTransaction.getTimestamp(),
        "Timestamp should be preserved when loading from file");
    assertEquals("TXN999", loadedTransaction.getTransactionId());
    assertEquals("ACC001", loadedTransaction.getAccountNumber());
    assertEquals(500.0, loadedTransaction.getAmount());
  }

  @Test
  void testIdPreservationAcrossSessions() throws IOException {
    // Create customers with specific IDs
    Customer customer1 =
        new RegularCustomer("CUS050", "Alice", 28, "555-1234", "123 Main St", "alice@example.com");
    Customer customer2 =
        new PremiumCustomer("CUS075", "Bob", 35, "555-5678", "456 Oak Ave", "bob@example.com");

    HashMap<String, Customer> customers = new HashMap<>();
    customers.put(customer1.getCustomerId(), customer1);
    customers.put(customer2.getCustomerId(), customer2);

    // Save customers
    persistenceService.saveCustomers(customers);

    // Load customers back
    HashMap<String, Customer> loadedCustomers = persistenceService.loadCustomers();

    // Verify IDs were preserved
    assertTrue(loadedCustomers.containsKey("CUS050"), "Customer CUS050 should be preserved");
    assertTrue(loadedCustomers.containsKey("CUS075"), "Customer CUS075 should be preserved");
    assertEquals("Alice", loadedCustomers.get("CUS050").getName());
    assertEquals("Bob", loadedCustomers.get("CUS075").getName());

    // Create accounts with specific numbers
    Account account1 = new SavingsAccount("ACC100", customer1, 1000.0);
    Account account2 = new CheckingAccount("ACC200", customer2, 500.0);

    HashMap<String, Account> accounts = new HashMap<>();
    accounts.put(account1.getAccountNumber(), account1);
    accounts.put(account2.getAccountNumber(), account2);

    // Save accounts
    persistenceService.saveAccounts(accounts);

    // Load accounts back
    HashMap<String, Account> loadedAccounts = persistenceService.loadAccounts(loadedCustomers);

    // Verify account numbers were preserved
    assertTrue(loadedAccounts.containsKey("ACC100"), "Account ACC100 should be preserved");
    assertTrue(loadedAccounts.containsKey("ACC200"), "Account ACC200 should be preserved");
    assertEquals(1000.0, loadedAccounts.get("ACC100").getBalance());
    // Verify counter was updated (new entities should get IDs higher than loaded ones)
    Customer newCustomer =
        new RegularCustomer("TestUser", 30, "555-9999", "789 Oak St", "test@example.com");
    assertTrue(
        Integer.parseInt(newCustomer.getCustomerId().replace("CUS", "")) > 75,
        "New customer ID should be higher than loaded max (75)");

    Account newAccount = new SavingsAccount(customer1, 100.0);
    assertTrue(
        Integer.parseInt(newAccount.getAccountNumber().replace("ACC", "")) > 200,
        "New account number should be higher than loaded max (200)");
  }
}
