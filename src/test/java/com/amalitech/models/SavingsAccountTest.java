package com.amalitech.models;

import static org.junit.jupiter.api.Assertions.*;

import com.amalitech.constants.AccountType;
import com.amalitech.constants.TransactionType;
import com.amalitech.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SavingsAccountTest {

  private SavingsAccount savingsAccount;
  private Customer customer;

  @BeforeEach
  void setUp() {
    customer = new RegularCustomer("Bob Smith", 40, "1122334455", "789 Oak St", "bob@example.com");
    savingsAccount = new SavingsAccount(customer, 1000.0);
  }

  @Test
  void testConstructor() {
    assertEquals(1000.0, savingsAccount.getBalance());
    assertEquals(3.5, savingsAccount.getInterestRate());
    assertEquals(500.0, savingsAccount.getMinimumBalance());
    assertEquals(AccountType.SAVINGS, savingsAccount.getAccountType());
  }

  @Test
  void testInitialDepositNegative() {
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> new SavingsAccount(customer, -100.0));
    assertEquals("Initial deposit must be positive", exception.getMessage());
  }

  @Test
  void testGetInterestRate() {
    assertEquals(3.5, savingsAccount.getInterestRate());
  }

  @Test
  void testGetMinimumBalance() {
    assertEquals(500.0, savingsAccount.getMinimumBalance());
  }

  @Test
  void testCalculateInterest() {
    // 1000 * 3.5% = 35.0.
    assertEquals(35.0, savingsAccount.calculateInterest());
  }

  @Test
  void testWithdrawSuccess() throws Exception {
    // Min balance 500. Balance 1000. Can withdraw 500.
    savingsAccount.withdraw(200.0);
    assertEquals(800.0, savingsAccount.getBalance());
  }

  @Test
  void testWithdrawFailure() {
    // Min balance 500. Balance 1000. Try withdraw 600. Remaining 400 < 500.
    assertThrows(
        InsufficientFundsException.class,
        () -> savingsAccount.processTransaction(600.0, TransactionType.WITHDRAWAL));
    assertEquals(1000.0, savingsAccount.getBalance());
  }

  @Test
  void testValidateWithdrawalSuccess() {
    assertDoesNotThrow(() -> savingsAccount.processTransaction(200.0, TransactionType.WITHDRAWAL));
  }

  @Test
  void testValidateWithdrawalFailure() {
    assertThrows(
        InsufficientFundsException.class,
        () -> savingsAccount.processTransaction(600.0, TransactionType.WITHDRAWAL));
  }

  @Test
  void testDisplayAccountDetails() {
    // Just ensure no exceptions are thrown during display
    assertDoesNotThrow(() -> savingsAccount.displayAccountDetails());
  }
}
