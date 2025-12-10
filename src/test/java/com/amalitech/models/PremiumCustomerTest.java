package com.amalitech.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for PremiumCustomer including waived fees and type verification. */
class PremiumCustomerTest {

  private PremiumCustomer premiumCustomer;

  @BeforeEach
  void setUp() {
    premiumCustomer = new PremiumCustomer("Bob", 40, "555-5678", "321 Pine Rd", "bob@example.com");
  }

  @Test
  void testConstructor() {
    assertEquals("Bob", premiumCustomer.getName());
    assertEquals(40, premiumCustomer.getAge());
    assertEquals("555-5678", premiumCustomer.getContact());
    assertEquals("321 Pine Rd", premiumCustomer.getAddress());
    assertEquals("bob@example.com", premiumCustomer.getEmail());
  }

  @Test
  void testGetCustomerType() {
    assertEquals("Premium", premiumCustomer.getCustomerType());
  }

  @Test
  void testHasWaivedFees() {
    assertTrue(premiumCustomer.hasWaivedFees());
  }

  @Test
  void testDisplayCustomerDetails() {
    // Just ensuring it doesn't throw an exception
    assertDoesNotThrow(() -> premiumCustomer.displayCustomerDetails());
  }
}
