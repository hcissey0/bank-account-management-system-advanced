package com.amalitech.main;

import com.amalitech.exceptions.*;
import com.amalitech.models.*;
import com.amalitech.services.*;
import com.amalitech.utils.*;

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
        displayMainMenu();
        choice = inputReader.readInt("Enter your choice: ", 1, 7);
        handleMainMenuChoice(
            choice, accountManager, transactionManager, customerManager, inputReader);
      } while (choice != 7);

      // Save all data before exit
      saveAllData(accountManager, customerManager, transactionManager);
    }

    System.out.println("Thank you for using Bank Account Management System!\nGoodbye!");
  }

  // ==================== MAIN MENU ====================

  private static void displayMainMenu() {
    System.out.println("\n+===================+");
    System.out.println("|    MAIN MENU      |");
    System.out.println("+===================+");
    System.out.println("1. Manage Accounts");
    System.out.println("2. Manage Customers");
    System.out.println("3. Manage Transactions");
    System.out.println("4. Reports & Statements");
    System.out.println("5. Data Management");
    System.out.println("6. Run Tests");
    System.out.println("7. Exit");
    System.out.println();
  }

  private static void handleMainMenuChoice(
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
      case 6 -> runTests(inputReader);
      case 7 -> {}
      default -> System.out.println("Invalid Input. Try Again!");
    }
  }

  // ==================== ACCOUNTS MENU ====================

  private static void showAccountsMenu(
      AccountManager accountManager, CustomerManager customerManager, InputReader inputReader) {
    int choice;
    do {
      System.out.println("\n+-------------------+");
      System.out.println("|  ACCOUNTS MENU    |");
      System.out.println("+-------------------+");
      System.out.println("1. Create Account");
      System.out.println("2. View All Accounts");
      System.out.println("3. Find Account");
      System.out.println("4. View Account Summary");
      System.out.println("5. Back to Main Menu");
      System.out.println();

      choice = inputReader.readInt("Enter your choice: ", 1, 5);

      switch (choice) {
        case 1 -> createAccount(accountManager, customerManager, inputReader);
        case 2 -> accountManager.viewAllAccounts(inputReader);
        case 3 -> findAndDisplayAccount(accountManager, inputReader);
        case 4 -> displayAccountSummary(accountManager, inputReader);
        case 5 -> {}
        default -> System.out.println("Invalid Input. Try Again!");
      }
    } while (choice != 5);
  }

  // ==================== CUSTOMERS MENU ====================

  private static void showCustomersMenu(CustomerManager customerManager, InputReader inputReader) {
    int choice;
    do {
      System.out.println("\n+-------------------+");
      System.out.println("|  CUSTOMERS MENU   |");
      System.out.println("+-------------------+");
      System.out.println("1. Add Customer");
      System.out.println("2. View All Customers");
      System.out.println("3. Find Customer");
      System.out.println("4. Back to Main Menu");
      System.out.println();

      choice = inputReader.readInt("Enter your choice: ", 1, 4);

      switch (choice) {
        case 1 -> addCustomer(customerManager, inputReader);
        case 2 -> customerManager.viewAllCustomers(inputReader);
        case 3 -> findAndDisplayCustomer(customerManager, inputReader);
        case 4 -> {}
        default -> System.out.println("Invalid Input. Try Again!");
      }
    } while (choice != 4);
  }

  // ==================== TRANSACTIONS MENU ====================

  private static void showTransactionsMenu(
      AccountManager accountManager,
      TransactionManager transactionManager,
      InputReader inputReader) {
    int choice;
    do {
      System.out.println("\n+---------------------+");
      System.out.println("|  TRANSACTIONS MENU  |");
      System.out.println("+---------------------+");
      System.out.println("1. Process Transaction");
      System.out.println("2. View All Transactions");
      System.out.println("3. View Account Transactions");
      System.out.println("4. Back to Main Menu");
      System.out.println();

      choice = inputReader.readInt("Enter your choice: ", 1, 4);

      switch (choice) {
        case 1 -> processTransaction(accountManager, transactionManager, inputReader);
        case 2 -> transactionManager.viewAllTransactions(inputReader);
        case 3 -> viewTransactionHistory(transactionManager, inputReader);
        case 4 -> {}
        default -> System.out.println("Invalid Input. Try Again!");
      }
    } while (choice != 4);
  }

  // ==================== REPORTS MENU ====================

  private static void showReportsMenu(
      AccountManager accountManager,
      TransactionManager transactionManager,
      InputReader inputReader) {
    int choice;
    do {
      System.out.println("\n+------------------------+");
      System.out.println("| REPORTS & STATEMENTS   |");
      System.out.println("+------------------------+");
      System.out.println("1. Generate Bank Statement");
      System.out.println("2. View Bank Summary");
      System.out.println("3. Back to Main Menu");
      System.out.println();

      choice = inputReader.readInt("Enter your choice: ", 1, 3);

      switch (choice) {
        case 1 -> generateBankStatement(accountManager, transactionManager, inputReader);
        case 2 -> displayBankSummary(accountManager, inputReader);
        case 3 -> {}
        default -> System.out.println("Invalid Input. Try Again!");
      }
    } while (choice != 3);
  }

  // ==================== DATA MANAGEMENT MENU ====================

  private static void showDataManagementMenu(
      AccountManager accountManager,
      CustomerManager customerManager,
      TransactionManager transactionManager,
      InputReader inputReader) {
    int choice;
    do {
      System.out.println("\n+---------------------+");
      System.out.println("|  DATA MANAGEMENT    |");
      System.out.println("+---------------------+");
      System.out.println("1. Save All Data");
      System.out.println("2. Reload Data");
      System.out.println("3. Back to Main Menu");
      System.out.println();

      choice = inputReader.readInt("Enter your choice: ", 1, 3);

      switch (choice) {
        case 1 -> {
          saveAllData(accountManager, customerManager, transactionManager);
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

  // ==================== UTILITY METHODS ====================

  private static void saveAllData(
      AccountManager accountManager,
      CustomerManager customerManager,
      TransactionManager transactionManager) {
    System.out.println("\nSaving data...");
    customerManager.saveCustomers();
    accountManager.saveAccounts();
    transactionManager.saveTransactions();
    System.out.println("Data saved successfully!");
  }

  // ==================== ACCOUNT OPERATIONS ====================

  private static void findAndDisplayAccount(
      AccountManager accountManager, InputReader inputReader) {
    System.out.println("\n+---------------+\n| FIND ACCOUNT  |\n+---------------+");
    String accountNumber = inputReader.readString("\nEnter Account number: ");
    try {
      Account account = accountManager.findAccount(accountNumber);
      account.displayAccountDetails();
    } catch (AccountNotFoundException e) {
      System.out.println(e.getMessage());
    }
    inputReader.waitForEnter();
  }

  private static void displayAccountSummary(
      AccountManager accountManager, InputReader inputReader) {
    System.out.println("\n+------------------+\n| ACCOUNT SUMMARY  |\n+------------------+");
    System.out.printf("Total Accounts: %d\n", accountManager.getAccountCount());
    System.out.printf("Total Bank Balance: $%.2f\n", accountManager.getTotalBalance());
    inputReader.waitForEnter();
  }

  // ==================== CUSTOMER OPERATIONS ====================

  private static void addCustomer(CustomerManager customerManager, InputReader inputReader) {
    System.out.println("\n+---------------+\n| ADD CUSTOMER  |\n+---------------+");
    Customer customer = createCustomer(inputReader);
    customerManager.addCustomer(customer);
    customerManager.saveCustomers();
    System.out.println("Customer added successfully!");
    customer.displayCustomerDetails();
    inputReader.waitForEnter();
  }

  private static void findAndDisplayCustomer(
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

  // ==================== TRANSACTION OPERATIONS ====================

  private static void viewTransactionHistory(
      TransactionManager transactionManager, InputReader inputReader) {
    System.out.println(
        "\n"
            + "+--------------------------+\n"
            + "| VIEW TRANSACTION HISTORY |\n"
            + "+--------------------------+");
    String accountNumber = inputReader.readString("\nEnter Account number: ");
    transactionManager.viewTransactionsByAccount(accountNumber, inputReader);
  }

  // ==================== REPORT OPERATIONS ====================

  private static void displayBankSummary(AccountManager accountManager, InputReader inputReader) {
    System.out.println("\n+----------------+\n| BANK SUMMARY   |\n+----------------+");
    System.out.printf("\nTotal Accounts: %d\n", accountManager.getAccountCount());
    System.out.printf("Total Bank Balance: $%.2f\n", accountManager.getTotalBalance());
    System.out.println("\nPress Enter to continue...");
    inputReader.waitForEnter();
  }

  private static void processTransaction(
      AccountManager accountManager,
      TransactionManager transactionManager,
      InputReader inputReader) {
    System.out.println(
        "\n+---------------------+\n| PROCESS TRANSACTION |\n+---------------------+");
    String accountNumber = inputReader.readString("\nEnter Account number: ");

    try {
      Account account = accountManager.findAccount(accountNumber);
      account.displayAccountDetails();
      handleTransaction(account, transactionManager, inputReader);
    } catch (AccountNotFoundException e) {
      System.out.println(e.getMessage());
      inputReader.waitForEnter();
    }
  }

  private static void handleTransaction(
      Account account, TransactionManager transactionManager, InputReader inputReader) {
    int transactionType = promptTransactionType(inputReader);
    double amount = inputReader.readDouble("Enter amount: ", 0);

    Transaction transaction = buildTransaction(account, transactionType, amount);

    printTransactionConfirmation(transaction, account);

    if (confirm(inputReader)) {
      executeTransaction(account, transactionManager, transaction);
    } else {
      System.out.println("Transaction cancelled.");
    }
    inputReader.waitForEnter();
  }

  private static int promptTransactionType(InputReader inputReader) {
    System.out.println("\nSelect Transaction Type:\n1. Deposit\n2. Withdraw");
    return inputReader.readInt("Enter choice (1-2): ", 1, 2);
  }

  private static Transaction buildTransaction(Account account, int transactionType, double amount) {
    String type = transactionType == 1 ? "DEPOSIT" : "WITHDRAWAL";
    double newBalance =
        transactionType == 1 ? account.getBalance() + amount : account.getBalance() - amount;
    return new Transaction(account.getAccountNumber(), type, amount, newBalance);
  }

  private static void printTransactionConfirmation(Transaction transaction, Account account) {
    System.out.println(
        "\n"
            + "+--------------------------+\n"
            + "| Transaction Confirmation |\n"
            + "+--------------------------+");
    System.out.printf(
        "Transaction ID: %s\n"
            + "Account: %s\n"
            + "Type: %s\n"
            + "Amount: $%.2f\n"
            + "Previous Balance: $%.2f\n"
            + "New Balance: $%.2f\n"
            + "Date/Time: %s\n",
        transaction.getTransactionId(),
        account.getAccountNumber(),
        transaction.getType(),
        transaction.getAmount(),
        account.getBalance(),
        transaction.getBalanceAfter(),
        transaction.getTimestamp());
  }

  private static boolean confirm(InputReader inputReader) {
    return inputReader.readString("\nConfirm transaction? (y/n): ").toLowerCase().startsWith("y");
  }

  private static void executeTransaction(
      Account account, TransactionManager transactionManager, Transaction transaction) {
    try {
      account.processTransaction(transaction.getAmount(), transaction.getType());
      transactionManager.addTransaction(transaction);
      transactionManager.saveTransactions(); // Auto-save after transaction
      System.out.printf(
          "%s Successful! New Balance: $%.2f\n", transaction.getType(), account.getBalance());
    } catch (BankException e) {
      System.out.println("Transaction failed: " + e.getMessage());
    }
  }

  private static void createAccount(
      AccountManager accountManager, CustomerManager customerManager, InputReader inputReader) {
    System.out.println("\n+------------------+\n| ACCOUNT CREATION |\n+------------------+");
    Customer customer = createCustomer(inputReader);
    customerManager.addCustomer(customer);
    Account account = createAccountForCustomer(inputReader, customer);

    System.out.println("\n+--------------+\n| Confirmation |\n+--------------+");
    System.out.printf(
        "Customer Name: %s\nCustomer Type: %s\nAccount Type: %s\nInitial Deposit: $%.2f\n",
        customer.getName(),
        customer instanceof RegularCustomer ? "Regular" : "Premium",
        account.getAccountType(),
        account.getBalance());

    if (inputReader
        .readString("\nConfirm account creation? (y/n): ")
        .toLowerCase()
        .startsWith("y")) {
      accountManager.addAccount(account);
      customerManager.saveCustomers(); // Auto-save customer
      accountManager.saveAccounts(); // Auto-save account
      System.out.println("Account Created Successfully!");
      account.displayAccountDetails();
      customer.displayCustomerDetails();
    } else {
      System.out.println("Account creation cancelled.");
    }
    inputReader.waitForEnter();
  }

  private static Customer createCustomer(InputReader inputReader) {
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

  private static Account createAccountForCustomer(InputReader inputReader, Customer customer) {
    System.out.println(
        "\n"
            + "Account type:\n"
            + "1. Savings Account (Interest Rate: 3.5%, Minimum Balance: $500)\n"
            + "2. Checking Account (Overdraft Limit: $1,000, Monthly Fee: $10)");
    int accountType = inputReader.readInt("\nSelect type (1-2): ", 1, 2);

    double minDeposit =
        customer instanceof PremiumCustomer ? 10000.0 : (accountType == 1 ? 500.0 : 0.0);
    double initialDeposit = inputReader.readDouble("\nEnter initial deposit amount: ", minDeposit);

    return accountType == 1
        ? new SavingsAccount(customer, initialDeposit)
        : new CheckingAccount(customer, initialDeposit);
  }

  private static void generateBankStatement(
      AccountManager accountManager,
      TransactionManager transactionManager,
      InputReader inputReader) {
    System.out.println("\n+----------------+\n| BANK STATEMENT |\n+----------------+");
    String accountNumber = inputReader.readString("\nEnter Account number: ");

    try {
      Account account = accountManager.findAccount(accountNumber);
      account.displayAccountDetails();
      displayTransactions(transactionManager.getTransactionsForAccount(accountNumber));
      displaySummary(transactionManager, accountNumber, account.getBalance());
    } catch (AccountNotFoundException e) {
      System.out.println(e.getMessage());
    }
    inputReader.waitForEnter();
  }

  private static void displayTransactions(Transaction[] transactions) {
    System.out.println("\n--- Transactions ---");
    if (transactions.length == 0) {
      System.out.println("No transactions found.");
    } else {
      new ConsoleTablePrinter()
          .printTable(
              new String[] {"TRANSACTION ID", "TYPE", "AMOUNT", "DATE"},
              java.util.Arrays.stream(transactions)
                  .map(
                      t ->
                          new String[] {
                            t.getTransactionId(), t.getType(), "$" + t.getAmount(), t.getTimestamp()
                          })
                  .toArray(String[][]::new));
    }
  }

  private static void displaySummary(
      TransactionManager transactionManager, String accountNumber, double balance) {
    double totalDeposits = transactionManager.getTotalDeposits(accountNumber);
    double totalWithdrawals = transactionManager.getTotalWithdrawals(accountNumber);
    System.out.printf(
        "\n"
            + "--- Summary ---\n"
            + "Total Deposits: $%.2f\n"
            + "Total Withdrawals: $%.2f\n"
            + "Net Change: $%.2f\n"
            + "Closing Balance: $%.2f\n",
        totalDeposits, totalWithdrawals, totalDeposits - totalWithdrawals, balance);
  }

  private static void runTests(InputReader inputReader) {
    System.out.println("Running tests with JUnit...");
    try {
      new CustomTestRunner().runTests();
    } catch (Exception e) {
      System.out.println("Failed to run tests: " + e.getMessage());
    }
    inputReader.waitForEnter();
  }
}
