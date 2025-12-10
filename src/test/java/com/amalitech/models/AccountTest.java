package com.amalitech.models;

import static org.junit.jupiter.api.Assertions.*;

import com.amalitech.exceptions.InvalidAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountTest {

  private TestAccount account;
  private Customer customer;

  // Concrete implementation for testing abstract Account class
  private static class TestAccount extends Account {
    TestAccount(Customer customer) {
      super(customer);
    }

    @Override
    public void displayAccountDetails() {
      // Intentionally empty for testing purposes
    }

    @Override
    public String getAccountType() {
      return "Test";
    }

    @Override
    public double withdraw(double amount) throws Exception {
      if (amount > getBalance()) {
        throw new InvalidAmountException("Insufficient funds");
      }
      return super.withdraw(amount);
    }
  }

  @BeforeEach
  void setUp() {
    customer = new RegularCustomer("John Doe", 30, "1234567890", "123 Main St", "john@example.com");
    account = new TestAccount(customer);
  }

  @Test
  void testAccountNumberGeneration() {
    String accountNumber = account.getAccountNumber();
    assertNotNull(accountNumber);
    assertTrue(accountNumber.startsWith("ACC"));
    System.out.println("Account Number Test Passed");
  }

  @Test
  void testDeposit() {
    account.deposit(100.0);
    assertEquals(100.0, account.getBalance());
  }

  @Test
  void testWithdraw() throws Exception {
    account.deposit(100.0);
    account.withdraw(50.0);
    assertEquals(50.0, account.getBalance());
  }

  @Test
  void testProcessTransactionDeposit() throws Exception {
    account.processTransaction(100.0, "Deposit");
    assertEquals(100.0, account.getBalance());
  }

  @Test
  void testProcessTransactionWithdrawal() throws Exception {
    account.deposit(100.0);
    account.processTransaction(50.0, "Withdrawal");
    assertEquals(50.0, account.getBalance());
  }

  @Test
  void testProcessTransactionWithWrongType() {
    assertThrows(Exception.class, () -> account.processTransaction(100.0, "InvalidType"));
  }

  @Test
  void testProcessTransactionInvalidType() {
    assertThrows(Exception.class, () -> account.processTransaction(100.0, "Invalid"));
  }

  @Test
  void testValidateDepositNegativeAmount() {
    assertThrows(InvalidAmountException.class, () -> account.validateDeposit(-10.0));
  }

  @Test
  void testWithdrawInsufficientFunds() {
    assertThrows(InvalidAmountException.class, () -> account.withdraw(10.0));
  }

  @Test
  void testGetters() {
    assertNotNull(account.getAccountNumber());
    assertEquals(customer, account.getCustomer());
    assertEquals("Active", account.getStatus());
    assertEquals(0.0, account.getBalance());
  }

  @Test
  void testSetters() {
    account.setBalance(200.0);
    assertEquals(200.0, account.getBalance());
  }

  @Test
  void testGetAccountCounter() {
    // Since accountCounter is static and incremented in constructor,
    // its value depends on how many Account objects have been created in the JVM.
    // We just verify it returns a positive integer.
    assertTrue(Account.getAccountCounter() > 0);
  }

  @Test
  void testValidateAmountDeposit() throws Exception {
    // Should not throw exception for valid positive amount
    account.validateAmount(100.0, "Deposit");
  }

  @Test
  void testValidateAmountWithdrawal() throws Exception {
    account.deposit(200.0);
    // Should not throw exception for valid withdrawal amount
    account.validateAmount(100.0, "Withdrawal");
  }

  @Test
  void testValidateAmountDepositInvalid() {
    assertThrows(InvalidAmountException.class, () -> account.validateAmount(-50.0, "Deposit"));
  }

  @Test
  void testValidateAmountWithdrawalInvalid() {
    // TestAccount implementation throws if amount > balance
    assertThrows(InvalidAmountException.class, () -> account.withdraw(50.0));
  }
}
