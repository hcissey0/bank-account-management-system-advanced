package com.amalitech.models;

import static org.junit.jupiter.api.Assertions.*;

import com.amalitech.constants.CustomerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for the abstract Customer class using a concrete test implementation. */
class CustomerTest {

  private TestCustomer customer;

  /** Concrete implementation for testing abstract Customer class. */
  private static class TestCustomer extends Customer {
    TestCustomer(String name, int age, String contact, String address, String email) {
      this.setName(name);
      this.setAge(age);
      this.setContact(contact);
      this.setAddress(address);
      this.setEmail(email);
    }

    @Override
    public void displayCustomerDetails() {
      // Intentionally empty for testing purposes
    }

    @Override
    public CustomerType getCustomerType() {
      return CustomerType.REGULAR;
    }
  }

  @BeforeEach
  void setUp() {
    customer = new TestCustomer("John Doe", 30, "1234567890", "123 Main St", "john@example.com");
  }

  @Test
  void testCustomerCreation() {
    assertNotNull(customer.getCustomerId());
    assertTrue(customer.getCustomerId().startsWith("CUS"));
  }

  @Test
  void testGettersAndSetters() {
    assertNotEquals(0, Customer.getCustomerCounter());
    assertEquals("John Doe", customer.getName());
    assertEquals(30, customer.getAge());
    assertEquals("1234567890", customer.getContact());
    assertEquals("123 Main St", customer.getAddress());
    customer.setAddress("456 Elm St");
    assertEquals("456 Elm St", customer.getAddress());
    assertEquals(CustomerType.REGULAR, customer.getCustomerType());

    customer.setName("Jane Doe");
    assertEquals("Jane Doe", customer.getName());

    customer.setAge(31);
    assertEquals(31, customer.getAge());

    customer.setContact("0987654321");
    assertEquals("0987654321", customer.getContact());

    customer.setAddress("456 Elm St");
    assertEquals("456 Elm St", customer.getAddress());
    assertEquals(CustomerType.REGULAR, customer.getCustomerType());
  }
}
