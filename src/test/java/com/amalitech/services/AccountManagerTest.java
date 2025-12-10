package com.amalitech.services;

import static org.junit.jupiter.api.Assertions.*;

import com.amalitech.exceptions.AccountNotFoundException;
import com.amalitech.models.*;
import com.amalitech.utils.InputReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountManagerTest {

  private AccountManager accountManager;
  private Customer customer;

  @BeforeEach
  void setUp() {
    accountManager = new AccountManager();
    customer = new RegularCustomer("Alice", 28, "555-0101", "321 Pine St");
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
