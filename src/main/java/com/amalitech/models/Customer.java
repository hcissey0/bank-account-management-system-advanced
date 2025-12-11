package com.amalitech.models;

import com.amalitech.constants.CustomerType;

/** Abstract base class representing a bank customer with personal details. */
public abstract class Customer {
  private static int customerCounter = 0;
  private final String customerId;
  private String name;
  private int age;
  private String contact;
  private String address;
  private String email;

  Customer() {
    this.customerId = generateCustomerId();
  }

  /** Generates a unique customer ID in the format "CUSxxx". */
  private static String generateCustomerId() {
    return "CUS" + String.format("%03d", ++customerCounter);
  }

  /** Returns the total number of customers created. */
  public static int getCustomerCounter() {
    return customerCounter;
  }

  /** Sets the customer counter (used for persistence restoration). */
  public static void setCustomerCounter(int counter) {
    customerCounter = counter;
  }

  /** Constructor for loading existing customer with preserved ID (for persistence). */
  protected Customer(String existingCustomerId) {
    this.customerId = existingCustomerId;
  }

  public String getCustomerId() {
    return customerId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  /** Displays the customer's details to the console. */
  public abstract void displayCustomerDetails();

  /** Returns the type of customer (e.g., "Regular" or "Premium"). */
  public abstract CustomerType getCustomerType();
}
