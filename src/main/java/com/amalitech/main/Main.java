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
        choice = inputReader.readInt("Enter your choice: ", 1, 10);
        handleMenuChoice(choice, accountManager, transactionManager, customerManager, inputReader);
      } while (choice != 10);

      // Save all data before exit
      saveAllData(accountManager, customerManager, transactionManager);
    }

    System.out.println("Thank you for using Bank Account Management System!\nGoodbye!");
  }

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

  private static void displayMainMenu() {
    System.out.println("\n+-----------+\n| MAIN MENU |\n+-----------+");
    System.out.println(
        "1. Create Account\n2. View Accounts\n3. View Customers\n4. Process Transaction");
    System.out.println(
        "5. View Transaction History for an account\n6. View all Transaction Histories");
    System.out.println("7. Generate Bank Statement\n8. Run Tests\n9. Save Data\n10. Exit\n");
  }

  private static void handleMenuChoice(
      int choice,
      AccountManager accountManager,
      TransactionManager transactionManager,
      CustomerManager customerManager,
      InputReader inputReader) {
    switch (choice) {
      case 1 -> createAccount(accountManager, customerManager, inputReader);
      case 2 -> accountManager.viewAllAccounts(inputReader);
      case 3 -> customerManager.viewAllCustomers(inputReader);
      case 4 -> processTransaction(accountManager, transactionManager, inputReader);
      case 5 -> viewTransactionHistory(transactionManager, inputReader);
      case 6 -> transactionManager.viewAllTransactions(inputReader);
      case 7 -> generateBankStatement(accountManager, transactionManager, inputReader);
      case 8 -> runTests(inputReader);
      case 9 -> {
        saveAllData(accountManager, customerManager, transactionManager);
        inputReader.waitForEnter();
      }
      case 10 -> {}
      default -> System.out.println("Invalid Input. Try Again!");
    }
  }

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
