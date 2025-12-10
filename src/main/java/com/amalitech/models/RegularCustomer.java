package com.amalitech.models;

/** Represents a regular bank customer with standard banking services. */
public class RegularCustomer extends Customer {
  private static final String CUSTOMER_TYPE = "Regular";

  public RegularCustomer(String name, int age, String contact, String address, String email) {
    this.setName(name);
    this.setAge(age);
    this.setContact(contact);
    this.setAddress(address);
    this.setEmail(email);
  }

  /** Constructor for loading existing customer (for persistence). */
  public RegularCustomer(
      String existingCustomerId,
      String name,
      int age,
      String contact,
      String address,
      String email) {
    super(existingCustomerId);
    this.setName(name);
    this.setAge(age);
    this.setContact(contact);
    this.setAddress(address);
    this.setEmail(email);
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
    System.out.println("+--------------------------+");
  }

  @java.lang.Override
  public String getCustomerType() {
    return CUSTOMER_TYPE;
  }
}
