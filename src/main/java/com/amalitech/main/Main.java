package com.amalitech.main;

import com.amalitech.services.*;
import com.amalitech.utils.ConsoleInputReader;

/** Main entry point for the Bank Account Management System. */
public class Main {

  public static void main(String[] args) {
    System.out.println(
        "+-------------------------+\n| BANK ACCOUNT MANAGEMENT |\n+-------------------------+");

    // Initialize persistence service
    FilePersistenceService persistenceService = new FilePersistenceService();

    // Initialize managers with persistence (order matters: customers -> accounts -> transactions)
    CustomerManager customerManager = new CustomerManager(persistenceService);
    AccountManager accountManager = new AccountManager(customerManager, persistenceService);
    TransactionManager transactionManager = new TransactionManager(persistenceService);

    try (ConsoleInputReader inputReader = new ConsoleInputReader()) {
      int choice;
      do {
        MenuDisplay.showMainMenu();
        choice = inputReader.readInt("Enter your choice: ", 1, 7);
        MenuHandler.handleMainMenuChoice(
            choice, accountManager, transactionManager, customerManager, inputReader);
      } while (choice != 7);

      // Save all data before exit
      DataOperations.saveAllData(accountManager, customerManager, transactionManager);
    }

    System.out.println("Thank you for using Bank Account Management System!\nGoodbye!");
  }
}
