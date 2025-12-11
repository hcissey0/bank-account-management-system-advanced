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
      InputReader inputReader,
      ConfigurationService configService) {
    switch (choice) {
      case 1 ->
          showAccountsMenu(
              accountManager, customerManager, transactionManager, inputReader, configService);
      case 2 ->
          showCustomersMenu(
              customerManager, accountManager, transactionManager, inputReader, configService);
      case 3 ->
          showTransactionsMenu(
              accountManager, transactionManager, customerManager, inputReader, configService);
      case 4 -> showReportsMenu(accountManager, transactionManager, inputReader);
      case 5 ->
          showDataManagementMenu(
              accountManager, customerManager, transactionManager, inputReader, configService);
      case 6 -> DataOperations.runTests(inputReader);
      case 7 -> {}
      default -> System.out.println("Invalid Input. Try Again!");
    }
  }

  // ==================== ACCOUNTS MENU ====================

  public static void showAccountsMenu(
      AccountManager accountManager,
      CustomerManager customerManager,
      TransactionManager transactionManager,
      InputReader inputReader,
      ConfigurationService configService) {
    int choice;
    do {
      MenuDisplay.showAccountsMenu();
      choice = inputReader.readInt("Enter your choice: ", 0, 4);

      switch (choice) {
        case 1 -> {
          AccountOperations.createAccount(accountManager, customerManager, inputReader);
          if (configService.isAutoSave()) {
            DataOperations.saveAllData(accountManager, customerManager, transactionManager);
          }
        }
        case 2 -> accountManager.viewAllAccounts(inputReader);
        case 3 -> AccountOperations.findAndDisplayAccount(accountManager, inputReader);
        case 4 -> AccountOperations.displayAccountSummary(accountManager, inputReader);
        case 0 -> {}
        default -> System.out.println("Invalid Input. Try Again!");
      }
    } while (choice != 0);
  }

  // ==================== CUSTOMERS MENU ====================

  public static void showCustomersMenu(
      CustomerManager customerManager,
      AccountManager accountManager,
      TransactionManager transactionManager,
      InputReader inputReader,
      ConfigurationService configService) {
    int choice;
    do {
      MenuDisplay.showCustomersMenu();
      choice = inputReader.readInt("Enter your choice: ", 0, 3);

      switch (choice) {
        case 1 -> {
          CustomerOperations.addCustomer(customerManager, inputReader);
          if (configService.isAutoSave()) {
            DataOperations.saveAllData(accountManager, customerManager, transactionManager);
          }
        }
        case 2 -> customerManager.viewAllCustomers(inputReader);
        case 3 -> CustomerOperations.findAndDisplayCustomer(customerManager, inputReader);
        case 0 -> {}
        default -> System.out.println("Invalid Input. Try Again!");
      }
    } while (choice != 0);
  }

  // ==================== TRANSACTIONS MENU ====================

  public static void showTransactionsMenu(
      AccountManager accountManager,
      TransactionManager transactionManager,
      CustomerManager customerManager,
      InputReader inputReader,
      ConfigurationService configService) {
    int choice;
    do {
      MenuDisplay.showTransactionsMenu();
      choice = inputReader.readInt("Enter your choice: ", 0, 3);

      switch (choice) {
        case 1 -> {
          TransactionOperations.processTransaction(accountManager, transactionManager, inputReader);
          if (configService.isAutoSave()) {
            DataOperations.saveAllData(accountManager, customerManager, transactionManager);
          }
        }
        case 2 -> transactionManager.viewAllTransactions(inputReader);
        case 3 ->
            TransactionOperations.viewTransactionHistory(
                accountManager, transactionManager, inputReader);
        case 0 -> {}
        default -> System.out.println("Invalid Input. Try Again!");
      }
    } while (choice != 0);
  }

  // ==================== REPORTS MENU ====================

  public static void showReportsMenu(
      AccountManager accountManager,
      TransactionManager transactionManager,
      InputReader inputReader) {
    int choice;
    do {
      MenuDisplay.showReportsMenu();
      choice = inputReader.readInt("Enter your choice: ", 0, 2);

      switch (choice) {
        case 1 ->
            ReportOperations.generateBankStatement(accountManager, transactionManager, inputReader);
        case 2 -> ReportOperations.displayBankSummary(accountManager, inputReader);
        case 0 -> {}
        default -> System.out.println("Invalid Input. Try Again!");
      }
    } while (choice != 0);
  }

  // ==================== DATA MANAGEMENT MENU ====================

  public static void showDataManagementMenu(
      AccountManager accountManager,
      CustomerManager customerManager,
      TransactionManager transactionManager,
      InputReader inputReader,
      ConfigurationService configService) {
    int choice;
    do {
      MenuDisplay.showDataManagementMenu(configService);
      choice = inputReader.readInt("Enter your choice: ", 0, 5);

      switch (choice) {
        case 1 -> {
          DataOperations.saveAllData(accountManager, customerManager, transactionManager);
          inputReader.waitForEnter();
        }
        case 2 -> {
          DataOperations.loadAllData(accountManager, customerManager, transactionManager);
          inputReader.waitForEnter();
        }
        case 3 -> {
          configService.setAutoSave(!configService.isAutoSave());
          System.out.println(
              "Auto-save is now " + (configService.isAutoSave() ? "ENABLED" : "DISABLED"));
          inputReader.waitForEnter();
        }
        case 4 -> {
          configService.setAutoLoadOnStartup(!configService.isAutoLoadOnStartup());
          System.out.println(
              "Auto-load on startup is now "
                  + (configService.isAutoLoadOnStartup() ? "ENABLED" : "DISABLED"));
          inputReader.waitForEnter();
        }
        case 5 -> {
          configService.setSaveOnExit(!configService.isSaveOnExit());
          System.out.println(
              "Save on exit is now " + (configService.isSaveOnExit() ? "ENABLED" : "DISABLED"));
          inputReader.waitForEnter();
        }
        case 0 -> {}
        default -> System.out.println("Invalid Input. Try Again!");
      }
    } while (choice != 0);
  }
}
