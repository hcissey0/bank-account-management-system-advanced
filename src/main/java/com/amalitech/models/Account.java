package com.amalitech.models;


import com.amalitech.exceptions.BankException;
import com.amalitech.exceptions.InvalidAmountException;

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

  public void deposit(double amount) {
    this.balance += amount;
  }

  public double withdraw(double amount) {
    this.balance -= amount;
    return this.balance;
  }

  /**
   * Processes a deposit or withdrawal transaction after validation.
   *
   * @param amount the transaction amount (must be positive)
   * @param type the transaction type ("Deposit" or "Withdrawal")
   * @throws BankException if validation fails or type is invalid
   */
  @Override
  public void processTransaction(double amount, String type) throws BankException {
    validateAmount(amount, type);

    if (type.equalsIgnoreCase("Deposit")) this.deposit(amount);
    else if (type.equalsIgnoreCase("Withdrawal")) this.withdraw(amount);
    else throw new BankException("Invalid transaction type: " + type);
  }

  /**
   * Validates the transaction amount based on type.
   *
   * @param amount the transaction amount
   * @param type the transaction type ("Deposit" or "Withdrawal")
   * @throws BankException if the amount is invalid for the transaction type
   */
  @Override
  public void validateAmount(double amount, String type) throws BankException {
    if (type.equalsIgnoreCase("Deposit")) validateDeposit(amount);

    if (type.equalsIgnoreCase("Withdrawal")) validateWithdrawal(amount);
  }

  public void validateDeposit(double amount) throws InvalidAmountException {
    if (amount <= 0) throw new InvalidAmountException("Amount must be positive");
  }

  public abstract void displayAccountDetails();

  public abstract String getAccountType();

  protected abstract void validateWithdrawal(double amount) throws BankException;
}
