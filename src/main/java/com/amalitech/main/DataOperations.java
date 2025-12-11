package com.amalitech.main;

import com.amalitech.services.*;
import com.amalitech.utils.*;

/** Handles data persistence operations. */
public class DataOperations {

  public static void saveAllData(
      AccountManager accountManager,
      CustomerManager customerManager,
      TransactionManager transactionManager) {
    System.out.println("\nSaving data...");
    customerManager.saveCustomers();
    accountManager.saveAccounts();
    transactionManager.saveTransactions();
    System.out.println("Data saved successfully!");
  }

  public static void loadAllData(
      AccountManager accountManager,
      CustomerManager customerManager,
      TransactionManager transactionManager) {
    System.out.println("\nLoading data...");
    customerManager.loadCustomers();
    accountManager.loadAccounts();
    transactionManager.loadTransactions();
    System.out.println("Data loaded successfully!");
  }

  public static void runTests(InputReader inputReader) {
    System.out.println("Running tests with JUnit...");
    try {
      new CustomTestRunner().runTests();
    } catch (Exception e) {
      System.out.println("Failed to run tests: " + e.getMessage());
    }
    inputReader.waitForEnter();
  }
}
