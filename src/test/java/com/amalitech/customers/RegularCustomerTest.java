package com.amalitech.customers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for RegularCustomer including constructor and type verification. */
class RegularCustomerTest {

  private RegularCustomer regularCustomer;

  @BeforeEach
  void setUp() {
    regularCustomer = new RegularCustomer("Alice", 25, "555-1234", "789 Oak Ave");
  }

  @Test
  void testConstructor() {
    assertEquals("Alice", regularCustomer.getName());
    assertEquals(25, regularCustomer.getAge());
    assertEquals("555-1234", regularCustomer.getContact());
    assertEquals("789 Oak Ave", regularCustomer.getAddress());
  }

  @Test
  void testGetCustomerType() {
    assertEquals("Regular", regularCustomer.getCustomerType());
  }

  @Test
  void testDisplayCustomerDetails() {
    // Just ensuring it doesn't throw an exception
    assertDoesNotThrow(() -> regularCustomer.displayCustomerDetails());
  }
}
