package com.amalitech.main;

/** Displays all menu interfaces. */
public class MenuDisplay {

  public static void showMainMenu() {
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

  public static void showAccountsMenu() {
    System.out.println("\n+-------------------+");
    System.out.println("|  ACCOUNTS MENU    |");
    System.out.println("+-------------------+");
    System.out.println("1. Create Account");
    System.out.println("2. View All Accounts");
    System.out.println("3. Find Account");
    System.out.println("4. View Account Summary");
    System.out.println("5. Back to Main Menu");
    System.out.println();
  }

  public static void showCustomersMenu() {
    System.out.println("\n+-------------------+");
    System.out.println("|  CUSTOMERS MENU   |");
    System.out.println("+-------------------+");
    System.out.println("1. Add Customer");
    System.out.println("2. View All Customers");
    System.out.println("3. Find Customer");
    System.out.println("4. Back to Main Menu");
    System.out.println();
  }

  public static void showTransactionsMenu() {
    System.out.println("\n+---------------------+");
    System.out.println("|  TRANSACTIONS MENU  |");
    System.out.println("+---------------------+");
    System.out.println("1. Process Transaction");
    System.out.println("2. View All Transactions");
    System.out.println("3. View Account Transactions");
    System.out.println("4. Back to Main Menu");
    System.out.println();
  }

  public static void showReportsMenu() {
    System.out.println("\n+------------------------+");
    System.out.println("| REPORTS & STATEMENTS   |");
    System.out.println("+------------------------+");
    System.out.println("1. Generate Bank Statement");
    System.out.println("2. View Bank Summary");
    System.out.println("3. Back to Main Menu");
    System.out.println();
  }

  public static void showDataManagementMenu() {
    System.out.println("\n+---------------------+");
    System.out.println("|  DATA MANAGEMENT    |");
    System.out.println("+---------------------+");
    System.out.println("1. Save All Data");
    System.out.println("2. Reload Data");
    System.out.println("3. Back to Main Menu");
    System.out.println();
  }
}
