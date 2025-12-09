package com.amalitech.customers;

import static org.junit.jupiter.api.Assertions.*;

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
  void testAddCustomerLimit() {
    // Assuming limit is 100 based on code reading
    for (int i = 0; i < 100; i++) {
      customerManager.addCustomer(new RegularCustomer("User" + i, 20, "Contact", "Address"));
    }
    assertEquals(100, customerManager.getCustomerCount());

    // Try adding one more
    customerManager.addCustomer(new RegularCustomer("Overflow", 20, "Contact", "Address"));
    assertEquals(100, customerManager.getCustomerCount());
  }
}
