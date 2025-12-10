package com.amalitech.main;

import com.amalitech.exceptions.AccountNotFoundException;
import com.amalitech.models.*;
import com.amalitech.services.*;
import com.amalitech.utils.InputReader;
import com.amalitech.utils.ValidationUtils;

/** Handles all account-related operations. */
public class AccountOperations {

  public static void createAccount(
      AccountManager accountManager, CustomerManager customerManager, InputReader inputReader) {
    System.out.println("\n+------------------+\n| ACCOUNT CREATION |\n+------------------+");
    Customer customer = CustomerOperations.createCustomer(inputReader);
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
      customerManager.saveCustomers();
      accountManager.saveAccounts();
      System.out.println("Account Created Successfully!");
      account.displayAccountDetails();
      customer.displayCustomerDetails();
    } else {
      System.out.println("Account creation cancelled.");
    }
    inputReader.waitForEnter();
  }

  public static void findAndDisplayAccount(AccountManager accountManager, InputReader inputReader) {
    System.out.println("\n+---------------+\n| FIND ACCOUNT  |\n+---------------+");
    String accountNumber;
    while (true) {
      accountNumber = inputReader.readString("\nEnter Account number: ");
      if (ValidationUtils.isValidAccountNumber(accountNumber)) {
        break;
      }
      System.out.println("Invalid Account Number format. Expected format: ACCxxx (e.g., ACC001)");
    }
    try {
      Account account = accountManager.findAccount(accountNumber);
      account.displayAccountDetails();
    } catch (AccountNotFoundException e) {
      System.out.println(e.getMessage());
    }
    inputReader.waitForEnter();
  }

  public static void displayAccountSummary(AccountManager accountManager, InputReader inputReader) {
    System.out.println("\n+------------------+\n| ACCOUNT SUMMARY  |\n+------------------+");
    System.out.printf("Total Accounts: %d\n", accountManager.getAccountCount());
    System.out.printf("Total Bank Balance: $%.2f\n", accountManager.getTotalBalance());
    inputReader.waitForEnter();
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
}
