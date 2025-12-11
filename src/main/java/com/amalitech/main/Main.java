package com.amalitech.main;

import com.amalitech.services.*;
import com.amalitech.utils.ConsoleInputReader;

/** Main entry point for the Bank Account Management System. */
public class Main {

  public static void main(String[] args) {
    System.out.println(
        "+-------------------------+\n| BANK ACCOUNT MANAGEMENT |\n+-------------------------+");

    // Initialize persistence and configuration services
    FilePersistenceService persistenceService = new FilePersistenceService();
    ConfigurationService configService = new ConfigurationService();

    // Initialize managers with persistence (order matters: customers -> accounts -> transactions)
    CustomerManager customerManager = new CustomerManager(persistenceService);
    AccountManager accountManager = new AccountManager(customerManager, persistenceService);
    TransactionManager transactionManager = new TransactionManager(persistenceService);

    // Auto-load data if configured
    if (configService.isAutoLoadOnStartup()) {
      DataOperations.loadAllData(accountManager, customerManager, transactionManager);
    }

    try (ConsoleInputReader inputReader = new ConsoleInputReader()) {
      int choice;
      do {
        MenuDisplay.showMainMenu();
        choice = inputReader.readInt("Enter your choice: ", 0, 7);
        MenuHandler.handleMainMenuChoice(
            choice,
            accountManager,
            transactionManager,
            customerManager,
            inputReader,
            configService);
      } while (choice != 0);

      // Save all data before exit if configured
      if (configService.isSaveOnExit()) {
        DataOperations.saveAllData(accountManager, customerManager, transactionManager);
      }
    }

    System.out.println("Thank you for using Bank Account Management System!\nGoodbye!");
  }
}
