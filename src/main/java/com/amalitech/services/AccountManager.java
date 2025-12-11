package com.amalitech.services;

import com.amalitech.exceptions.AccountNotFoundException;
import com.amalitech.models.*;
import com.amalitech.utils.ConsoleTablePrinter;
import com.amalitech.utils.InputReader;
import com.amalitech.utils.TablePrinter;
import java.io.IOException;
import java.util.HashMap;

/** Manages a collection of bank accounts using HashMap with file persistence. */
public class AccountManager {
  private final HashMap<String, Account> accounts;
  private final TablePrinter printer;
  private final PersistenceService persistenceService;
  private final CustomerManager customerManager;

  public AccountManager(CustomerManager customerManager, PersistenceService persistenceService) {
    this.customerManager = customerManager;
    this.persistenceService = persistenceService;
    this.printer = new ConsoleTablePrinter();
    this.accounts = new HashMap<>();
  }

  /** Loads accounts from file. */
  public void loadAccounts() {
    try {
      this.accounts.clear();
      this.accounts.putAll(persistenceService.loadAccounts(customerManager.getCustomers()));
    } catch (IOException e) {
      System.err.println("Warning: Could not load accounts from file: " + e.getMessage());
    }
  }

  public int getAccountCount() {
    return accounts.size();
  }

  public void addAccount(Account account) {
    if (account == null) {
      System.out.println("Cannot add null account.");
      return;
    }
    accounts.put(account.getAccountNumber(), account);
  }

  public Account findAccount(String accountNumber) throws AccountNotFoundException {
    Account account = accounts.get(accountNumber);
    if (account == null) {
      throw new AccountNotFoundException("Account with number " + accountNumber + " not found.");
    }
    return account;
  }

  /**
   * Displays a tabular view of all accounts and summary statistics.
   *
   * @param inputReader used to pause execution after display
   */
  public void viewAllAccounts(InputReader inputReader) {
    String[] headers = {"ACCOUNT NUMBER", "CUSTOMER NAME", "TYPE", "BALANCE", "STATUS"};

    if (accounts.isEmpty()) {
      System.out.println("No accounts available.");
      inputReader.waitForEnter();
      return;
    }

    String[][] data = buildTableData();

    printer.printTable(headers, data);

    System.out.println();
    System.out.println("Total Accounts: " + getAccountCount());
    System.out.println("Total Bank Balance: $" + getTotalBalance());

    inputReader.waitForEnter();
  }

  /** Constructs a 2D array of formatted account data for tabular display using Stream API. */
  private String[][] buildTableData() {
    return accounts.values().stream()
        .map(
            acc ->
                new String[] {
                  acc.getAccountNumber(),
                  formatCustomerName(acc),
                  formatAccountType(acc),
                  formatAccountBalance(acc),
                  acc.getStatus()
                })
        .toArray(String[][]::new);
  }

  private String formatAccountBalance(Account account) {
    return "$" + String.format("%.2f", account.getBalance());
  }

  /** Formats customer name with account-specific details (overdraft limit or interest rate). */
  private String formatCustomerName(Account acc) {
    if (acc instanceof CheckingAccount checkingAccount) {
      return acc.getCustomer().getName()
          + " (Overdraft Limit: $"
          + checkingAccount.getOverdraftLimit()
          + ")";
    } else if (acc instanceof SavingsAccount savingsAccount) {
      return acc.getCustomer().getName()
          + " (Interest Rate: "
          + savingsAccount.getInterestRate()
          + "%)";
    }
    return acc.getCustomer().getName();
  }

  /** Formats account type with specific details (monthly fee or minimum balance). */
  private String formatAccountType(Account acc) {
    if (acc instanceof CheckingAccount checkingAccount) {
      return acc.getAccountType() + " (Monthly Fee: $" + checkingAccount.getMonthlyFee() + ")";
    } else if (acc instanceof SavingsAccount savingsAccount) {
      return acc.getAccountType() + " (Min Balance: $" + savingsAccount.getMinimumBalance() + ")";
    }
    return acc.getAccountType();
  }

  public double getTotalBalance() {
    return accounts.values().stream().mapToDouble(Account::getBalance).sum();
  }

  /** Saves all accounts to file. */
  public void saveAccounts() {
    try {
      persistenceService.saveAccounts(accounts);
    } catch (IOException e) {
      System.err.println("Error saving accounts: " + e.getMessage());
    }
  }

  /** Returns the accounts HashMap for persistence operations. */
  public HashMap<String, Account> getAccounts() {
    return accounts;
  }
}
