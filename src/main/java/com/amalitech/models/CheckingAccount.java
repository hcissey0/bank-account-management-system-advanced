package com.amalitech.models;

import com.amalitech.exceptions.InvalidAmountException;
import com.amalitech.exceptions.OverdraftLimitExceededException;
import com.amalitech.utils.ValidationUtils;

/** Represents a checking account with overdraft protection and monthly fees. */
public class CheckingAccount extends Account {

  private final double overdraftLimit;
  private final double monthlyFee;

  private static final String ACCOUNT_TYPE = "Checking";
  private static final double OVERDRAFT_LIMIT = 1_000.0;
  private static final double MONTHLY_FEE = 10.0;

  public CheckingAccount(Customer customer, double initialDeposit) {
    super(customer);
    try {
      this.deposit(initialDeposit);
    } catch (com.amalitech.exceptions.InvalidAmountException e) {
      throw new IllegalArgumentException("Initial deposit must be positive", e);
    }
    this.overdraftLimit = OVERDRAFT_LIMIT;
    this.monthlyFee = MONTHLY_FEE;
  }

  /** Constructor for loading existing account (for persistence). */
  public CheckingAccount(String existingAccountNumber, Customer customer, double balance) {
    super(existingAccountNumber, customer, balance);
    this.overdraftLimit = OVERDRAFT_LIMIT;
    this.monthlyFee = MONTHLY_FEE;
  }

  public double getMonthlyFee() {
    return monthlyFee;
  }

  public double getOverdraftLimit() {
    return overdraftLimit;
  }

  /** Deducts the monthly fee from the account balance if sufficient funds exist. */
  public void applyMonthlyFee() {
    if (getBalance() > this.monthlyFee) {
      this.setBalance(this.getBalance() - this.monthlyFee);
    }
  }

  @java.lang.Override
  public void displayAccountDetails() {
    System.out.println("+-----------------+");
    System.out.println("| Account Details |");
    System.out.println("+-----------------+");
    System.out.println("Account Number: " + this.getAccountNumber());
    System.out.println("Customer: " + this.getCustomer().getName());
    System.out.println("Account Type: " + this.getAccountType());
    System.out.println("Current Balance: " + this.getBalance());
    System.out.println("Overdraft: " + getOverdraftLimit());
    System.out.println("Monthly fee: " + getMonthlyFee());
    System.out.println("+--------------------------+");
  }

  @java.lang.Override
  public String getAccountType() {
    return ACCOUNT_TYPE;
  }

  /**
   * Withdraws amount from the account if it does not exceed the overdraft limit.
   *
   * @param amount the amount to withdraw
   * @return the new balance
   * @throws OverdraftLimitExceededException if the withdrawal would exceed the overdraft limit
   */
  @Override
  public double withdraw(double amount)
      throws InvalidAmountException, OverdraftLimitExceededException {
    ValidationUtils.validateCheckingWithdrawal(amount, this.getBalance(), this.overdraftLimit);
    double newBalance = this.getBalance() - amount;
    this.setBalance(newBalance);
    return newBalance;
  }
}
