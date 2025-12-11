package com.amalitech.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.amalitech.models.CheckingAccount;
import com.amalitech.models.Customer;
import com.amalitech.models.RegularCustomer;
import org.junit.jupiter.api.Test;

class ConcurrencyTest {

  @Test
  void testConcurrencySimulation() throws InterruptedException {
    Customer customer =
        new RegularCustomer("John Doe", 30, "555-0100", "john@example.com", "123 Main St");
    // Initial balance 1000.0
    CheckingAccount account = new CheckingAccount(customer, 1000.0);

    // Run simulation: 100 threads deposit 10.0, 100 threads withdraw 10.0
    // Net change should be 0.
    ConcurrencyUtils.runSimulation(account);

    // Final balance should be 1000.0
    assertEquals(
        1000.0,
        account.getBalance(),
        "Balance should remain consistent after concurrent operations");
  }
}
