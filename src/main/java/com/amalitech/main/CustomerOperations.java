package com.amalitech.main;

import com.amalitech.models.*;
import com.amalitech.services.CustomerManager;
import com.amalitech.utils.InputReader;

/** Handles all customer-related operations. */
public class CustomerOperations {

  public static void addCustomer(CustomerManager customerManager, InputReader inputReader) {
    System.out.println("\n+---------------+\n| ADD CUSTOMER  |\n+---------------+");
    Customer customer = createCustomer(inputReader);
    customerManager.addCustomer(customer);
    customerManager.saveCustomers();
    System.out.println("Customer added successfully!");
    customer.displayCustomerDetails();
    inputReader.waitForEnter();
  }

  public static void findAndDisplayCustomer(
      CustomerManager customerManager, InputReader inputReader) {
    System.out.println("\n+----------------+\n| FIND CUSTOMER  |\n+----------------+");
    String customerId = inputReader.readString("\nEnter Customer ID: ");
    Customer customer = customerManager.findCustomer(customerId);
    if (customer != null) {
      customer.displayCustomerDetails();
    } else {
      System.out.println("Customer not found!");
    }
    inputReader.waitForEnter();
  }

  public static Customer createCustomer(InputReader inputReader) {
    String name = inputReader.readString("\nEnter customer name: ");
    int age = inputReader.readInt("Enter customer age: ", 0, 150);
    String contact = inputReader.readString("Enter customer contact: ");
    String address = inputReader.readString("Enter customer address: ");

    System.out.println(
        "\n"
            + "Customer type:\n"
            + "1. Regular Customer (Standard Banking Services)\n"
            + "2. Premium Customer (Enhanced Benefits, Minimum Balance $10,000)");
    return inputReader.readInt("\nSelect type (1-2): ", 1, 2) == 1
        ? new RegularCustomer(name, age, contact, address)
        : new PremiumCustomer(name, age, contact, address);
  }
}
