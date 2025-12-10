package com.amalitech.services;

import static org.junit.jupiter.api.Assertions.*;

import com.amalitech.models.*;
import com.amalitech.utils.InputReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for TransactionManager including add, totals, and capacity limit operations. */
class TransactionManagerTest {

  private TransactionManager transactionManager;

  @BeforeEach
  void setUp() {
    transactionManager = new TransactionManager();
  }

  @Test
  void testAddTransaction() {
    Transaction transaction = new Transaction("ACC001", "DEPOSIT", 100.0, 100.0);
    transactionManager.addTransaction(transaction);
    assertEquals(1, transactionManager.getTransactionCount());
  }

  @Test
  void testAddNullTransaction() {
    transactionManager.addTransaction(null);
    assertEquals(0, transactionManager.getTransactionCount());
  }

  @Test
  void testCalculateTotalDeposits() {
    transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 100.0, 100.0));
    transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 50.0, 150.0));
    transactionManager.addTransaction(new Transaction("ACC001", "WITHDRAWAL", 30.0, 120.0));

    assertEquals(150.0, transactionManager.calculateTotalDeposits());
  }

  @Test
  void testCalculateTotalWithdrawals() {
    transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 100.0, 100.0));
    transactionManager.addTransaction(new Transaction("ACC001", "WITHDRAWAL", 20.0, 80.0));
    transactionManager.addTransaction(new Transaction("ACC001", "WITHDRAWAL", 30.0, 50.0));

    assertEquals(50.0, transactionManager.calculateTotalWithdrawals());
  }

  @Test
  void testTransactionLimit() {
    // With ArrayList, there's no fixed capacity limit
    // Verify we can add more than the old array limit of 200
    for (int i = 0; i < 200; i++) {
      transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 1.0, 1.0));
    }
    assertEquals(200, transactionManager.getTransactionCount());

    // Add one more - should succeed with ArrayList
    transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 1.0, 1.0));
    assertEquals(201, transactionManager.getTransactionCount());
  }

  @Test
  void testGetTransactionCount() {
    assertEquals(0, transactionManager.getTransactionCount());
    transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 100.0, 100.0));
    assertEquals(1, transactionManager.getTransactionCount());
  }

  @Test
  void testViewAllTransactions() {
    // Since viewAllTransactions prints to console, we can only ensure no exceptions are thrown
    Transaction transaction1 = new Transaction("ACC001", "DEPOSIT", 100.0, 100.0);
    Transaction transaction2 = new Transaction("ACC002", "WITHDRAWAL", 50.0, 50.0);
    transactionManager.addTransaction(transaction1);
    transactionManager.addTransaction(transaction2);

    assertDoesNotThrow(
        () ->
            transactionManager.viewAllTransactions(
                new InputReader() {
                  @Override
                  public String readString(String prompt) {
                    return null;
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
                    // No-op for testing
                  }
                }));
  }

  @Test
  void testViewAllTransactionsEmpty() {
    // Since viewAllTransactions prints to console, we can only ensure no exceptions are thrown

    assertDoesNotThrow(
        () ->
            transactionManager.viewAllTransactions(
                new InputReader() {
                  @Override
                  public String readString(String prompt) {
                    return null;
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
                    // No-op for testing
                  }
                }));
  }

  @Test
  void testViewTransactionsByAccount() {
    Transaction transaction1 = new Transaction("ACC001", "DEPOSIT", 100.0, 100.0);
    Transaction transaction2 = new Transaction("ACC002", "WITHDRAWAL", 50.0, 50.0);
    transactionManager.addTransaction(transaction1);
    transactionManager.addTransaction(transaction2);
    assertDoesNotThrow(
        () ->
            transactionManager.viewTransactionsByAccount(
                "ACC001",
                new InputReader() {
                  @Override
                  public String readString(String prompt) {
                    return null;
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
                    // No-op for testing
                  }
                }));
  }

  @Test
  void testViewTransactionsByAccountEmpty() {
    assertDoesNotThrow(
        () ->
            transactionManager.viewTransactionsByAccount(
                "ACC001",
                new InputReader() {
                  @Override
                  public String readString(String prompt) {
                    return null;
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
                    // No-op for testing
                  }
                }));
  }

  @Test
  void testGetTransactionsForAccount() {
    Transaction transaction1 = new Transaction("ACC001", "DEPOSIT", 100.0, 100.0);
    Transaction transaction2 = new Transaction("ACC002", "WITHDRAWAL", 50.0, 50.0);
    transactionManager.addTransaction(transaction1);
    transactionManager.addTransaction(transaction2);

    assertNotEquals(0, transactionManager.getTransactionsForAccount("ACC001").length);
  }

  @Test
  void testGetTotalDepositsForAccount() {
    transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 100.0, 100.0));
    transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 50.0, 150.0));
    transactionManager.addTransaction(new Transaction("ACC001", "WITHDRAWAL", 30.0, 120.0));

    assertEquals(150.0, transactionManager.getTotalDeposits("ACC001"));
  }

  @Test
  void getTotalWithdrawalsForAccount() {
    transactionManager.addTransaction(new Transaction("ACC001", "DEPOSIT", 100.0, 100.0));
    transactionManager.addTransaction(new Transaction("ACC001", "WITHDRAWAL", 20.0, 80.0));
    transactionManager.addTransaction(new Transaction("ACC001", "WITHDRAWAL", 30.0, 50.0));

    assertEquals(50.0, transactionManager.getTotalWithdrawals("ACC001"));
  }
}
