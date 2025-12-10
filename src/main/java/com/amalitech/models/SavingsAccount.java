package com.amalitech.models;

import com.amalitech.exceptions.InsufficientFundsException;

/** Represents a savings account with an interest rate and a minimum balance requirement. */
public class SavingsAccount extends Account {

  private final double interestRate;
  private final double minimumBalance;

  private static final double INTEREST_RATE = 3.5;
  private static final double MINIMUM_BALANCE = 500.0;
  private static final String ACCOUNT_TYPE = "Savings";

  public SavingsAccount(Customer customer, double initialDeposit) {
    super(customer);
    this.deposit(initialDeposit);
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
    return this.getBalance() * this.interestRate;
  }

  /**
   * Withdraws the specified amount from the account, ensuring the minimum balance is maintained.
   *
   * @param amount the amount to withdraw
   * @return the new balance after withdrawal, or -1 if withdrawal would breach minimum balance
   */
  @java.lang.Override
  public double withdraw(double amount) {

    if (this.getBalance() - amount >= this.minimumBalance) {
      return super.withdraw(amount);
    }
    return -1;
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
  public String getAccountType() {
    return ACCOUNT_TYPE;
  }

  /**
   * Validates that a withdrawal amount does not breach the minimum balance requirement.
   *
   * @param amount the amount to withdraw
   * @throws InsufficientFundsException if the withdrawal would result in a balance below the
   *     minimum
   */
  @Override
  protected void validateWithdrawal(double amount) throws InsufficientFundsException {
    if (this.getBalance() - amount < this.minimumBalance) {
      throw new InsufficientFundsException(
          "Transaction Failed: Insufficient funds to maintain minimum balance. Current Balance: "
              + this.getBalance()
              + ", Minimum Required Balance: "
              + this.minimumBalance);
    }
  }
}
