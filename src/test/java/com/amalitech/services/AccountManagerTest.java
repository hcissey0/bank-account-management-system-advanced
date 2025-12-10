package com.amalitech.services;

import static org.junit.jupiter.api.Assertions.*;

import com.amalitech.exceptions.AccountNotFoundException;
import com.amalitech.models.*;
import com.amalitech.utils.InputReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountManagerTest {

  private AccountManager accountManager;
  private CustomerManager customerManager;
  private FilePersistenceService persistenceService;
  private Customer customer;

  @BeforeEach
  void setUp() throws IOException {
    // Clear data files before each test
    clearDataFiles();
    persistenceService = new FilePersistenceService("src/test/resources/data/");
    customerManager = new CustomerManager(persistenceService);
    accountManager = new AccountManager(customerManager, persistenceService);
    customer = new RegularCustomer("Alice", 28, "555-0101", "321 Pine St", "alice@example.com");
    customerManager.addCustomer(customer);
  }

  @AfterEach
  void tearDown() throws IOException {
    // Clear data files after each test
    clearDataFiles();
  }

  private void clearDataFiles() throws IOException {
    Path accountsFile = Paths.get("src/test/resources/data/accounts.txt");
    Path customersFile = Paths.get("src/test/resources/data/customers.txt");
    Path transactionsFile = Paths.get("src/test/resources/data/transactions.txt");

    // Create directory if it doesn't exist
    Path dataDir = Paths.get("src/test/resources/data");
    if (!Files.exists(dataDir)) {
      Files.createDirectories(dataDir);
    }

    if (Files.exists(accountsFile)) {
      Files.write(accountsFile, "accountType,accountNumber,customerId,balance,status\n".getBytes());
    }
    if (Files.exists(customersFile)) {
      Files.write(customersFile, "customerType,customerId,name,age,contact,address\n".getBytes());
    }
    if (Files.exists(transactionsFile)) {
      Files.write(
          transactionsFile,
          "transactionId,accountNumber,type,amount,balanceAfter,timestamp\n".getBytes());
    }
  }

  @Test
  void testGetAccountCountInitiallyZero() {
    assertEquals(0, accountManager.getAccountCount());
  }

  @Test
  void testAddAccount() {
    Account account = new CheckingAccount(customer, 500.0);
    accountManager.addAccount(account);
    assertEquals(1, accountManager.getAccountCount());
  }

  @Test
  void testFindAccountSuccess() throws AccountNotFoundException {
    Account account = new CheckingAccount(customer, 500.0);
    accountManager.addAccount(account);
    Account found = accountManager.findAccount(account.getAccountNumber());
    assertEquals(account, found);
  }

  @Test
  void testFindAccountFailure() {
    assertThrows(AccountNotFoundException.class, () -> accountManager.findAccount("NON_EXISTENT"));
  }

  @Test
  void testViewAllAccounts() {

    System.out.println("This is running");
    // Since viewAllAccounts prints to console, we will just ensure no exceptions are thrown
    Account account1 = new CheckingAccount(customer, 100.0);
    Account account2 = new SavingsAccount(customer, 200.0);
    accountManager.addAccount(account1);
    accountManager.addAccount(account2);
    assertDoesNotThrow(
        () ->
            accountManager.viewAllAccounts(
                new InputReader() {
                  @Override
                  public String readString(String prompt) {
                    return "";
                  }

                  @Override
                  public int readInt(String prompt, int min, int max) {
                    return 0;
                  }

                  @Override
                  public double readDouble(String prompt, double min) {
                    return 0;
                  }

                  @Override
                  public void waitForEnter() {
                    // Do nothing for test
                  }
                }));
  }

  @Test
  void testGetTotalBalance() {
    accountManager.addAccount(new CheckingAccount(customer, 100.0));
    accountManager.addAccount(new SavingsAccount(customer, 200.0));
    assertEquals(300.0, accountManager.getTotalBalance());
  }

  @Test
  void testAddAccountLimit() {
    // With HashMap, there's no fixed capacity limit
    // Verify we can add more than the old array limit of 50
    for (int i = 0; i < 50; i++) {
      accountManager.addAccount(new CheckingAccount(customer, 100.0));
    }
    assertEquals(50, accountManager.getAccountCount());

    // Add the 51st account - should succeed with HashMap
    accountManager.addAccount(new CheckingAccount(customer, 100.0));

    // Verify that the count increased beyond old limit
    assertEquals(51, accountManager.getAccountCount());
  }

  @Test
  void testGetTotalBalanceWithNoAccounts() {
    assertEquals(0.0, accountManager.getTotalBalance());
  }
}
