package com.amalitech.services;

import static org.junit.jupiter.api.Assertions.*;

import com.amalitech.models.*;
import com.amalitech.utils.InputReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for CustomerManager including add, find, and capacity limit operations. */
class CustomerManagerTest {

  private CustomerManager customerManager;

  @BeforeEach
  void setUp() {
    customerManager = new CustomerManager();
  }

  @Test
  void testAddCustomer() {
    Customer customer = new RegularCustomer("Charlie", 30, "555-9999", "101 Maple Dr");
    customerManager.addCustomer(customer);
    assertEquals(1, customerManager.getCustomerCount());
  }

  @Test
  void testFindCustomerSuccess() {
    Customer customer = new RegularCustomer("Charlie", 30, "555-9999", "101 Maple Dr");
    customerManager.addCustomer(customer);

    Customer found = customerManager.findCustomer(customer.getCustomerId());
    assertNotNull(found);
    assertEquals(customer, found);
  }

  @Test
  void testFindCustomerFailure() {
    Customer found = customerManager.findCustomer("NON_EXISTENT_ID");
    assertNull(found);
  }

  @Test
  void testViewAllCustomersEmpty() {
    // Since viewAllCustomers prints to console, we can only ensure no exceptions are thrown
    Customer customer = new RegularCustomer("Alice", 28, "555-1234", "123 Main St");
    Customer customer2 = new PremiumCustomer("Bob", 35, "555-5678", "456 Oak Ave");
    customerManager.addCustomer(customer);
    customerManager.addCustomer(customer2);
    assertDoesNotThrow(
        () ->
            customerManager.viewAllCustomers(
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
  void testAddCustomerLimit() {
    // With HashMap, there's no fixed capacity limit
    // Verify we can add more than the old array limit of 100
    for (int i = 0; i < 100; i++) {
      customerManager.addCustomer(new RegularCustomer("User" + i, 20, "Contact", "Address"));
    }
    assertEquals(100, customerManager.getCustomerCount());

    // Add one more - should succeed with HashMap
    customerManager.addCustomer(new RegularCustomer("Overflow", 20, "Contact", "Address"));
    assertEquals(101, customerManager.getCustomerCount());
  }
}
