package com.amalitech.main;

import com.amalitech.exceptions.AccountNotFoundException;
import com.amalitech.models.*;
import com.amalitech.services.*;
import com.amalitech.utils.*;

/** Handles report generation and bank statements. */
public class ReportOperations {

  public static void generateBankStatement(
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

  public static void displayBankSummary(AccountManager accountManager, InputReader inputReader) {
    System.out.println("\n+----------------+\n| BANK SUMMARY   |\n+----------------+");
    System.out.printf("\nTotal Accounts: %d\n", accountManager.getAccountCount());
    System.out.printf("Total Bank Balance: $%.2f\n", accountManager.getTotalBalance());
    System.out.println("\nPress Enter to continue...");
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
}
