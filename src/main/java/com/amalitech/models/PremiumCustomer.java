package com.amalitech.models;

import com.amalitech.constants.CustomerType;

/** Represents a premium bank customer with waived fees and minimum balance requirements. */
public class PremiumCustomer extends Customer {

  private final double minimumBalance;
  private static final CustomerType CUSTOMER_TYPE = CustomerType.PREMIUM;
  private static final double MINIMUM_BALANCE = 10_000.0;

  public PremiumCustomer(String name, int age, String contact, String address, String email) {
    this.minimumBalance = MINIMUM_BALANCE;

    this.setName(name);
    this.setAge(age);
    this.setContact(contact);
    this.setAddress(address);
    this.setEmail(email);
  }

  /** Constructor for loading existing customer (for persistence). */
  public PremiumCustomer(
      String existingCustomerId,
      String name,
      int age,
      String contact,
      String address,
      String email) {
    super(existingCustomerId);
    this.minimumBalance = MINIMUM_BALANCE;
    this.setName(name);
    this.setAge(age);
    this.setContact(contact);
    this.setAddress(address);
    this.setEmail(email);
  }

  /** Returns true as premium customers have waived monthly fees. */
  public boolean hasWaivedFees() {
    return true;
  }

  @java.lang.Override
  public void displayCustomerDetails() {
    System.out.println("+------------------+");
    System.out.println("| Customer Details |");
    System.out.println("+------------------+");
    System.out.println("Customer Number: " + this.getCustomerId());
    System.out.println("Name: " + this.getName());
    System.out.println("Age: " + this.getAge());
    System.out.println("Contact: " + this.getContact());
    System.out.println("Email: " + this.getEmail());
    System.out.println("Address: " + this.getAddress());
    System.out.println("Type: " + this.getCustomerType());
    System.out.println("Minimum Balance: " + this.minimumBalance);
    System.out.println("Monthly fee: Waived");
    System.out.println("+--------------------------+");
  }

  @java.lang.Override
  public CustomerType getCustomerType() {
    return CUSTOMER_TYPE;
  }
}
