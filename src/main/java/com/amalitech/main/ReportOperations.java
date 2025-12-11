package com.amalitech.main;

import com.amalitech.exceptions.AccountNotFoundException;
import com.amalitech.exceptions.InvalidInputException;
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
    String accountNumber;
    while (true) {
      accountNumber = inputReader.readString("\nEnter Account number: ");
      try {
        ValidationUtils.validateAccountNumber(accountNumber);
        break;
      } catch (InvalidInputException e) {
        System.out.println(e.getMessage());
      }
    }

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

  public static void displayBankSummary(
      AccountManager accountManager,
      CustomerManager customerManager,
      TransactionManager transactionManager,
      InputReader inputReader) {
    System.out.println("\n+----------------+\n| BANK SUMMARY   |\n+----------------+");

    System.out.println("\n--- Accounts ---");
    System.out.printf("Total Accounts: %d\n", accountManager.getAccountCount());
    System.out.printf("  - Savings: %d\n", accountManager.getSavingsAccountCount());
    System.out.printf("  - Checking: %d\n", accountManager.getCheckingAccountCount());
    System.out.printf("Total Bank Balance: $%.2f\n", accountManager.getTotalBalance());

    System.out.println("\n--- Customers ---");
    System.out.printf("Total Customers: %d\n", customerManager.getCustomerCount());
    System.out.printf("  - Regular: %d\n", customerManager.getRegularCustomerCount());
    System.out.printf("  - Premium: %d\n", customerManager.getPremiumCustomerCount());

    System.out.println("\n--- Transactions ---");
    System.out.printf("Total Transactions: %d\n", transactionManager.getTransactionCount());
    System.out.printf("  - Deposits: %d\n", transactionManager.getDepositCount());
    System.out.printf("  - Withdrawals: %d\n", transactionManager.getWithdrawalCount());
    System.out.printf("  - Transfers In: %d\n", transactionManager.getTransferInCount());
    System.out.printf("  - Transfers Out: %d\n", transactionManager.getTransferOutCount());

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
                            t.getTransactionId(),
                            t.getType(),
                            formatAmount(t.getType(), t.getAmount()),
                            t.getTimestamp()
                          })
                  .toArray(String[][]::new));
    }
  }

  private static String formatAmount(String type, double amount) {
    boolean isCredit = type.equalsIgnoreCase("DEPOSIT") || type.equalsIgnoreCase("TRANSFER_IN");
    String prefix = isCredit ? "+$" : "-$";
    return String.format("%s%.2f", prefix, amount);
  }

  private static void displaySummary(
      TransactionManager transactionManager, String accountNumber, double balance) {
    double totalDeposits = transactionManager.getTotalDeposits(accountNumber);
    double totalWithdrawals = transactionManager.getTotalWithdrawals(accountNumber);
    double totalTransfersIn = transactionManager.getTotalTransfersIn(accountNumber);
    double totalTransfersOut = transactionManager.getTotalTransfersOut(accountNumber);

    double netChange = (totalDeposits + totalTransfersIn) - (totalWithdrawals + totalTransfersOut);

    System.out.printf(
        "\n"
            + "--- Summary ---\n"
            + "Total Deposits: $%.2f\n"
            + "Total Withdrawals: $%.2f\n"
            + "Total Transfers In: $%.2f\n"
            + "Total Transfers Out: $%.2f\n"
            + "Net Change: $%.2f\n"
            + "Closing Balance: $%.2f\n",
        totalDeposits, totalWithdrawals, totalTransfersIn, totalTransfersOut, netChange, balance);
  }
}
