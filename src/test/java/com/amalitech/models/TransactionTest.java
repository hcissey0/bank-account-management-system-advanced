package com.amalitech.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Unit tests for Transaction including ID generation and timestamp creation. */
class TransactionTest {

  @Test
  void testTransactionCreation() {
    Transaction transaction = new Transaction("ACC001", "DEPOSIT", 100.0, 200.0);

    assertNotEquals(0, Transaction.getTransactionCounter());
    assertNotNull(transaction.getTransactionId());
    assertTrue(transaction.getTransactionId().startsWith("TXN"));
    assertEquals("ACC001", transaction.getAccountNumber());
    assertEquals("DEPOSIT", transaction.getType());
    assertEquals(100.0, transaction.getAmount());
    assertEquals(200.0, transaction.getBalanceAfter());
    assertNotNull(transaction.getTimestamp());
  }
}
