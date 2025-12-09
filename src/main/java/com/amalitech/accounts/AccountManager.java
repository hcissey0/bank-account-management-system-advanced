package com.amalitech.accounts;

import com.amalitech.exceptions.AccountNotFoundException;
import com.amalitech.utils.ConsoleTablePrinter;
import com.amalitech.utils.InputReader;
import com.amalitech.utils.TablePrinter;

/** Manages a collection of bank accounts with fixed capacity. */
public class AccountManager {
  private static final int MAX_ACCOUNTS = 50;

  private final Account[] accounts;
  private int accountCount;
  private final TablePrinter printer;

  public AccountManager() {
    this.accounts = new Account[MAX_ACCOUNTS];
    this.accountCount = 0;
    this.printer = new ConsoleTablePrinter();
  }

  public int getAccountCount() {
    return accountCount;
  }

  public void addAccount(Account account) {
    if (accountCount < MAX_ACCOUNTS) this.accounts[this.accountCount++] = account;
    else System.out.println("Account limit reached.");
  }

  public Account findAccount(String accountNumber) throws AccountNotFoundException {
    for (int i = 0; i < this.accountCount; i++) {
      if (this.accounts[i].getAccountNumber().equals(accountNumber)) {
        return this.accounts[i];
      }
    }
    throw new AccountNotFoundException("Account with number " + accountNumber + " not found.");
  }

  /**
   * Displays a tabular view of all accounts and summary statistics.
   *
   * @param inputReader used to pause execution after display
   */
  public void viewAllAccounts(InputReader inputReader) {
    String[] headers = {"ACCOUNT NUMBER", "CUSTOMER NAME", "TYPE", "BALANCE", "STATUS"};

    if (accountCount == 0) {
      System.out.println("No accounts available.");
      inputReader.waitForEnter();
      return;
    }

    String[][] data = buildTableData();

    printer.printTable(headers, data);

    System.out.println();
    System.out.println("Total Accounts: " + this.accountCount);
    System.out.println("Total Bank Balance: $" + getTotalBalance());

    inputReader.waitForEnter();
  }

  /** Constructs a 2D array of formatted account data for tabular display. */
  private String[][] buildTableData() {
    return java.util.stream.IntStream.range(0, this.accountCount)
        .mapToObj(i -> this.accounts[i])
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
    double totalBalance = 0;
    for (int i = 0; i < this.accountCount; i++) totalBalance += this.accounts[i].getBalance();

    return totalBalance;
  }
}
