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
      case 4 -> showReportsMenu(accountManager, transactionManager, customerManager, inputReader);
      case 5 ->
          showDataManagementMenu(
              accountManager, customerManager, transactionManager, inputReader, configService);
      case 6 -> showConcurrencyMenu(accountManager, inputReader);
      case 7 -> DataOperations.runTests(inputReader);
      case 0 -> {}
      default -> System.out.println("Invalid Input. Try Again!");
    }
  }

  // ==================== CONCURRENCY MENU ====================

  public static void showConcurrencyMenu(AccountManager accountManager, InputReader inputReader) {
    int choice;
    do {
      MenuDisplay.showConcurrencyMenu();
      choice = inputReader.readInt("Enter your choice: ", 0, 2);

      switch (choice) {
        case 1 -> {
          String accNum = inputReader.readString("Enter Account Number to test: ");
          try {
            com.amalitech.models.Account account = accountManager.findAccount(accNum);
            System.out.println("Starting standard simulation (100 deposits, 100 withdrawals)...");
            double startBalance = account.getBalance();
            com.amalitech.utils.ConcurrencyUtils.runCustomSimulation(account, 100, 100, true);
            System.out.println("Simulation complete.");
            System.out.println("Start Balance: " + startBalance);
            System.out.println("Final Balance: " + account.getBalance());
            if (Math.abs(startBalance - account.getBalance()) < 0.001) {
              System.out.println("SUCCESS: Balance matches!");
            } else {
              System.out.println("FAILURE: Balance mismatch!");
            }
          } catch (com.amalitech.exceptions.AccountNotFoundException e) {
            System.out.println(e.getMessage());
          } catch (InterruptedException e) {
            System.err.println("Simulation interrupted: " + e.getMessage());
          }
          inputReader.waitForEnter();
        }
        case 2 -> {
          String accNum = inputReader.readString("Enter Account Number to test: ");
          try {
            com.amalitech.models.Account account = accountManager.findAccount(accNum);
            int deposits = inputReader.readInt("Enter number of deposits: ", 1, 1000);
            int withdrawals = inputReader.readInt("Enter number of withdrawals: ", 1, 1000);
            System.out.println("Starting custom simulation...");
            double startBalance = account.getBalance();
            com.amalitech.utils.ConcurrencyUtils.runCustomSimulation(
                account, deposits, withdrawals, true);
            System.out.println("Simulation complete.");
            System.out.println("Start Balance: " + startBalance);
            System.out.println("Final Balance: " + account.getBalance());
          } catch (com.amalitech.exceptions.AccountNotFoundException e) {
            System.out.println(e.getMessage());
          } catch (InterruptedException e) {
            System.err.println("Simulation interrupted: " + e.getMessage());
          }
          inputReader.waitForEnter();
        }
        case 0 -> {}
        default -> System.out.println("Invalid Input. Try Again!");
      }
    } while (choice != 0);
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
      CustomerManager customerManager,
      InputReader inputReader) {
    int choice;
    do {
      MenuDisplay.showReportsMenu();
      choice = inputReader.readInt("Enter your choice: ", 0, 2);

      switch (choice) {
        case 1 ->
            ReportOperations.generateBankStatement(accountManager, transactionManager, inputReader);
        case 2 ->
            ReportOperations.displayBankSummary(
                accountManager, customerManager, transactionManager, inputReader);
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
