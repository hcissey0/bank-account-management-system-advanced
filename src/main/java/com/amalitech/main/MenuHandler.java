package com.amalitech.main;

import com.amalitech.services.*;
import com.amalitech.utils.InputReader;

/** Handles menu navigation and user input routing. */
public class MenuHandler {

  public static void handleMainMenuChoice(
      int choice,
      AccountManager accountManager,
      TransactionManager transactionManager,
      CustomerManager customerManager,
      InputReader inputReader) {
    switch (choice) {
      case 1 -> showAccountsMenu(accountManager, customerManager, inputReader);
      case 2 -> showCustomersMenu(customerManager, inputReader);
      case 3 -> showTransactionsMenu(accountManager, transactionManager, inputReader);
      case 4 -> showReportsMenu(accountManager, transactionManager, inputReader);
      case 5 ->
          showDataManagementMenu(accountManager, customerManager, transactionManager, inputReader);
      case 6 -> DataOperations.runTests(inputReader);
      case 7 -> {}
      default -> System.out.println("Invalid Input. Try Again!");
    }
  }

  // ==================== ACCOUNTS MENU ====================

  public static void showAccountsMenu(
      AccountManager accountManager, CustomerManager customerManager, InputReader inputReader) {
    int choice;
    do {
      MenuDisplay.showAccountsMenu();
      choice = inputReader.readInt("Enter your choice: ", 1, 5);

      switch (choice) {
        case 1 -> AccountOperations.createAccount(accountManager, customerManager, inputReader);
        case 2 -> accountManager.viewAllAccounts(inputReader);
        case 3 -> AccountOperations.findAndDisplayAccount(accountManager, inputReader);
        case 4 -> AccountOperations.displayAccountSummary(accountManager, inputReader);
        case 5 -> {}
        default -> System.out.println("Invalid Input. Try Again!");
      }
    } while (choice != 5);
  }

  // ==================== CUSTOMERS MENU ====================

  public static void showCustomersMenu(CustomerManager customerManager, InputReader inputReader) {
    int choice;
    do {
      MenuDisplay.showCustomersMenu();
      choice = inputReader.readInt("Enter your choice: ", 1, 4);

      switch (choice) {
        case 1 -> CustomerOperations.addCustomer(customerManager, inputReader);
        case 2 -> customerManager.viewAllCustomers(inputReader);
        case 3 -> CustomerOperations.findAndDisplayCustomer(customerManager, inputReader);
        case 4 -> {}
        default -> System.out.println("Invalid Input. Try Again!");
      }
    } while (choice != 4);
  }

  // ==================== TRANSACTIONS MENU ====================

  public static void showTransactionsMenu(
      AccountManager accountManager,
      TransactionManager transactionManager,
      InputReader inputReader) {
    int choice;
    do {
      MenuDisplay.showTransactionsMenu();
      choice = inputReader.readInt("Enter your choice: ", 1, 4);

      switch (choice) {
        case 1 ->
            TransactionOperations.processTransaction(
                accountManager, transactionManager, inputReader);
        case 2 -> transactionManager.viewAllTransactions(inputReader);
        case 3 -> TransactionOperations.viewTransactionHistory(transactionManager, inputReader);
        case 4 -> {}
        default -> System.out.println("Invalid Input. Try Again!");
      }
    } while (choice != 4);
  }

  // ==================== REPORTS MENU ====================

  public static void showReportsMenu(
      AccountManager accountManager,
      TransactionManager transactionManager,
      InputReader inputReader) {
    int choice;
    do {
      MenuDisplay.showReportsMenu();
      choice = inputReader.readInt("Enter your choice: ", 1, 3);

      switch (choice) {
        case 1 ->
            ReportOperations.generateBankStatement(accountManager, transactionManager, inputReader);
        case 2 -> ReportOperations.displayBankSummary(accountManager, inputReader);
        case 3 -> {}
        default -> System.out.println("Invalid Input. Try Again!");
      }
    } while (choice != 3);
  }

  // ==================== DATA MANAGEMENT MENU ====================

  public static void showDataManagementMenu(
      AccountManager accountManager,
      CustomerManager customerManager,
      TransactionManager transactionManager,
      InputReader inputReader) {
    int choice;
    do {
      MenuDisplay.showDataManagementMenu();
      choice = inputReader.readInt("Enter your choice: ", 1, 3);

      switch (choice) {
        case 1 -> {
          DataOperations.saveAllData(accountManager, customerManager, transactionManager);
          inputReader.waitForEnter();
        }
        case 2 -> {
          System.out.println("\nData reload requires application restart.");
          System.out.println("Current session data will be lost.");
          if (inputReader.readString("Continue? (y/n): ").toLowerCase().startsWith("y")) {
            System.out.println("Please restart the application to reload data.");
          }
          inputReader.waitForEnter();
        }
        case 3 -> {}
        default -> System.out.println("Invalid Input. Try Again!");
      }
    } while (choice != 3);
  }
}
