package com.amalitech.models;

import com.amalitech.constants.AccountType;
import com.amalitech.constants.TransactionType;
import com.amalitech.exceptions.InvalidAmountException;
import com.amalitech.utils.ValidationUtils;

/**
 * Abstract base class for bank accounts, implementing {@link Transactable} for transactions.
 * Manages account number, customer, status, and balance.
 */
public abstract class Account implements Transactable {
  private static final String DEFAULT_STATUS = "Active";
  private static int accountCounter = 0;
  private final String accountNumber;
  private final Customer customer;
  private final String status;
  private double balance;

  Account(Customer customer) {
    this.accountNumber = generateAccountNumber();
    this.balance = 0;
    this.customer = customer;
    this.status = DEFAULT_STATUS;
  }

  private static String generateAccountNumber() {
    return "ACC" + String.format("%03d", ++accountCounter);
  }

  /** Sets the account counter (used for persistence restoration). */
  public static void setAccountCounter(int counter) {
    accountCounter = counter;
  }

  /** Constructor for loading existing account with preserved number (for persistence). */
  protected Account(String existingAccountNumber, Customer customer, double balance) {
    this.accountNumber = existingAccountNumber;
    this.customer = customer;
    this.balance = balance;
    this.status = DEFAULT_STATUS;
  }

  // getters

  public static int getAccountCounter() {
    return accountCounter;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public Customer getCustomer() {
    return customer;
  }

  public String getStatus() {
    return status;
  }

  public double getBalance() {
    return balance;
  }

  // setters

  public void setBalance(double balance) {
    this.balance = balance;
  }

  // methods

  public synchronized double deposit(double amount) throws InvalidAmountException {
    ValidationUtils.validateDeposit(amount);
    this.balance += amount;
    return this.balance;
  }

  public abstract double withdraw(double amount) throws Exception;

  /**
   * Processes a deposit or withdrawal transaction after validation.
   *
   * @param amount the transaction amount (must be positive)
   * @param type the transaction type
   * @throws Exception if validation fails or type is invalid
   */
  @Override
  public double processTransaction(double amount, TransactionType type) throws Exception {
    if (type == TransactionType.DEPOSIT) return this.deposit(amount);
    else if (type == TransactionType.WITHDRAWAL) return this.withdraw(amount);
    throw new IllegalArgumentException("Invalid transaction type: " + type);
  }

  public abstract void displayAccountDetails();

  public abstract AccountType getAccountType();
}
