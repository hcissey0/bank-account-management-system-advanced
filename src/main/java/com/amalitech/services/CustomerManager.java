package com.amalitech.services;

import com.amalitech.models.*;
import com.amalitech.utils.ConsoleTablePrinter;
import com.amalitech.utils.InputReader;
import com.amalitech.utils.TablePrinter;
import java.io.IOException;
import java.util.HashMap;

/** Manages a collection of bank customers using HashMap with file persistence. */
public class CustomerManager {

  private final HashMap<String, Customer> customers;
  private final TablePrinter printer;
  private final FilePersistenceService persistenceService;

  public CustomerManager(FilePersistenceService persistenceService) {
    this.persistenceService = persistenceService;
    this.printer = new ConsoleTablePrinter();
    this.customers = loadCustomersFromFile();
  }

  /** Loads customers from file on initialization. */
  private HashMap<String, Customer> loadCustomersFromFile() {
    try {
      return persistenceService.loadCustomers();
    } catch (IOException e) {
      System.err.println("Warning: Could not load customers from file: " + e.getMessage());
      return new HashMap<>();
    }
  }

  /** Adds a customer to the system. */
  public void addCustomer(Customer customer) {
    if (customer == null) {
      System.out.println("Cannot add null customer.");
      return;
    }
    customers.put(customer.getCustomerId(), customer);
  }

  /**
   * Finds a customer by their unique ID.
   *
   * @param customerId the ID of the customer to find
   * @return the customer if found, or null if not found
   */
  public Customer findCustomer(String customerId) {
    return customers.get(customerId);
  }

  /**
   * Displays a tabular view of all registered customers.
   *
   * @param inputReader used to pause execution after display
   */
  public void viewAllCustomers(InputReader inputReader) {
    String[] headers = {"CUSTOMER ID", "NAME", "TYPE", "AGE", "CONTACT", "ADDRESS"};

    if (customers.isEmpty()) {
      System.out.println("No customers available.");
      inputReader.waitForEnter();
      return;
    }

    String[][] data = buildTableData();

    printer.printTable(headers, data);

    System.out.println("Total Customers: " + getCustomerCount());
    inputReader.waitForEnter();
  }

  /** Constructs a 2D array of formatted customer data for tabular display using Stream API. */
  private String[][] buildTableData() {
    return customers.values().stream()
        .map(
            customer ->
                new String[] {
                  customer.getCustomerId(),
                  customer.getName(),
                  customer.getCustomerType(),
                  String.valueOf(customer.getAge()),
                  customer.getContact(),
                  customer.getAddress()
                })
        .toArray(String[][]::new);
  }

  public int getCustomerCount() {
    return customers.size();
  }

  /** Saves all customers to file. */
  public void saveCustomers() {
    try {
      persistenceService.saveCustomers(customers);
    } catch (IOException e) {
      System.err.println("Error saving customers: " + e.getMessage());
    }
  }

  /** Returns the customers HashMap for persistence operations. */
  public HashMap<String, Customer> getCustomers() {
    return customers;
  }
}
