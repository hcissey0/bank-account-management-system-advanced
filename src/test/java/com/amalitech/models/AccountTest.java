package com.amalitech.models;

import static org.junit.jupiter.api.Assertions.*;

import com.amalitech.constants.AccountType;
import com.amalitech.constants.TransactionType;
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
    public AccountType getAccountType() {
      return AccountType.CHECKING;
    }

    @Override
    public double withdraw(double amount) throws Exception {
      if (amount > getBalance()) {
        throw new InvalidAmountException("Insufficient funds");
      }
      double newBalance = getBalance() - amount;
      setBalance(newBalance);
      return newBalance;
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
  void testDeposit() throws Exception {
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
    account.processTransaction(100.0, TransactionType.DEPOSIT);
    assertEquals(100.0, account.getBalance());
  }

  @Test
  void testProcessTransactionWithdrawal() throws Exception {
    account.deposit(100.0);
    account.processTransaction(50.0, TransactionType.WITHDRAWAL);
    assertEquals(50.0, account.getBalance());
  }

  @Test
  void testProcessTransactionWithWrongType() {
    assertThrows(
        IllegalArgumentException.class,
        () -> account.processTransaction(100.0, TransactionType.TRANSFER_IN));
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
  void testValidateAmountWithdrawalInvalid() {
    // TestAccount implementation throws if amount > balance
    assertThrows(InvalidAmountException.class, () -> account.withdraw(50.0));
  }
}
