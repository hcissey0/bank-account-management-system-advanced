package com.amalitech.models;

import com.amalitech.constants.AccountType;
import com.amalitech.exceptions.InsufficientFundsException;
import com.amalitech.exceptions.InvalidAmountException;
import com.amalitech.utils.ValidationUtils;

/** Represents a savings account with an interest rate and a minimum balance requirement. */
public class SavingsAccount extends Account {

  private final double interestRate;
  private final double minimumBalance;

  private static final double INTEREST_RATE = 3.5;
  private static final double MINIMUM_BALANCE = 500.0;
  private static final AccountType ACCOUNT_TYPE = AccountType.SAVINGS;

  public SavingsAccount(Customer customer, double initialDeposit) {
    super(customer);
    try {
      this.deposit(initialDeposit);
    } catch (com.amalitech.exceptions.InvalidAmountException e) {
      throw new IllegalArgumentException("Initial deposit must be positive", e);
    }
    this.interestRate = INTEREST_RATE;
    this.minimumBalance = MINIMUM_BALANCE;
  }

  /** Constructor for loading existing account (for persistence). */
  public SavingsAccount(String existingAccountNumber, Customer customer, double balance) {
    super(existingAccountNumber, customer, balance);
    this.interestRate = INTEREST_RATE;
    this.minimumBalance = MINIMUM_BALANCE;
  }

  public double getInterestRate() {
    return interestRate;
  }

  public double getMinimumBalance() {
    return minimumBalance;
  }

  /** Calculates the potential interest based on current balance and interest rate. */
  public double calculateInterest() {
    return this.getBalance() * (this.interestRate / 100);
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
    System.out.println("Interest Rate: " + this.interestRate);
    System.out.println("Minimum Balance: " + this.minimumBalance);
    System.out.println("Interest: " + this.calculateInterest());
    System.out.println("+--------------------------+");
  }

  @java.lang.Override
  public AccountType getAccountType() {
    return ACCOUNT_TYPE;
  }

  /**
   * Withdraws amount from the account if it does not breach the minimum balance requirement.
   *
   * @param amount the amount to withdraw
   * @return the new balance
   * @throws Exception if the withdrawal would result in a balance below the minimum
   */
  @Override
  public synchronized double withdraw(double amount)
      throws InvalidAmountException, InsufficientFundsException {
    ValidationUtils.validateSavingsWithdrawal(amount, this.getBalance(), this.minimumBalance);
    double newBalance = this.getBalance() - amount;
    this.setBalance(newBalance);
    return newBalance;
  }
}
