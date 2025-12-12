package com.amalitech.utils;

import com.amalitech.exceptions.InsufficientFundsException;
import com.amalitech.exceptions.InvalidAmountException;
import com.amalitech.exceptions.InvalidInputException;
import com.amalitech.exceptions.OverdraftLimitExceededException;

import java.util.regex.Pattern;

/** Utility class for validating inputs using Regex patterns. */
public class ValidationUtils {

  // Regex Patterns
  private static final String EMAIL_REGEX =
      "^[A-Za-z0-9+_.-]+@(?:[A-Za-z0-9-]+\\.)++[A-Za-z]{2,}$";
  private static final String ACCOUNT_NUMBER_REGEX = "^ACC\\d{3}$";
  private static final String CUSTOMER_ID_REGEX = "^CUS\\d{3}$";
  private static final String NAME_REGEX = "^[A-Za-z\\s]+$";
  private static final String PHONE_REGEX = "^\\d{10}$"; // Simple 10-digit phone validation

  private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
  private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile(ACCOUNT_NUMBER_REGEX);
  private static final Pattern CUSTOMER_ID_PATTERN = Pattern.compile(CUSTOMER_ID_REGEX);
  private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);
  private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

  private ValidationUtils() {
    // Private constructor to prevent instantiation
  }

  /**
   * Validates if the provided email is in a correct format.
   *
   * @param email The email string to validate.
   * @throws InvalidInputException if invalid.
   */
  public static void validateEmail(String email) throws InvalidInputException {
    if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
      throw new InvalidInputException("Invalid email format. Please enter a valid email address.");
    }
  }

  /**
   * Validates if the provided account number matches the format ACCxxx.
   *
   * @param accountNumber The account number string.
   * @throws InvalidInputException if invalid.
   */
  public static void validateAccountNumber(String accountNumber) throws InvalidInputException {
    if (accountNumber == null || !ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).matches()) {
      throw new InvalidInputException(
          "Invalid Account Number format. Expected format: ACCxxx (e.g., ACC001)");
    }
  }

  /**
   * Validates if the provided customer ID matches the format CUSxxx.
   *
   * @param customerId The customer ID string.
   * @throws InvalidInputException if invalid.
   */
  public static void validateCustomerId(String customerId) throws InvalidInputException {
    if (customerId == null || !CUSTOMER_ID_PATTERN.matcher(customerId).matches()) {
      throw new InvalidInputException(
          "Invalid Customer ID format. Expected format: CUSxxx (e.g., CUS001)");
    }
  }

  /**
   * Validates if the name contains only letters and spaces.
   *
   * @param name The name string.
   * @throws InvalidInputException if invalid.
   */
  public static void validateName(String name) throws InvalidInputException {
    if (name == null || name.trim().isEmpty() || !NAME_PATTERN.matcher(name).matches()) {
      throw new InvalidInputException("Invalid name. Please use only letters and spaces.");
    }
  }

  /**
   * Validates if the phone number is a 10-digit number.
   *
   * @param phone The phone number string.
   * @throws InvalidInputException if invalid.
   */
  public static void validatePhoneNumber(String phone) throws InvalidInputException {
    if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
      throw new InvalidInputException("Invalid phone number. Must be 10 digits.");
    }
  }

  /**
   * Validates if the age is within a reasonable range (18-120).
   *
   * @param age The age to validate.
   * @throws InvalidInputException if invalid.
   */
  public static void validateAge(int age) throws InvalidInputException {
    if (age < 18 || age > 120) {
      throw new InvalidInputException("Invalid age. Must be between 18 and 120.");
    }
  }

  /**
   * Validates if the amount is positive.
   *
   * @param amount The amount to validate.
   * @throws InvalidAmountException if invalid.
   */
  public static void validateDeposit(double amount) throws InvalidAmountException {
    if (amount <= 0) {
      throw new InvalidAmountException("Invalid deposit amount. Must be positive.");
    }
  }

  /**
   * Validates if the amount is positive and does not exceed the balance.
   *
   * @param amount The amount to validate.
   * @param balance The current balance.
   * @throws InvalidAmountException if invalid.
   * @throws InsufficientFundsException if insufficient funds.
   */
  public static void validateSavingsWithdrawal(double amount, double balance, double minimumBalance)
      throws InvalidAmountException, InsufficientFundsException {
    if (amount <= 0) {
      throw new InvalidAmountException("Invalid withdrawal amount. Must be positive.");
    }
    if (amount > balance - minimumBalance) {
      throw new InsufficientFundsException("Insufficient funds for this withdrawal. Minimum balance must be maintained.");
    }
  }

  /**
   * Validates if the amount is positive and does not exceed overdraft limit.
   *
   * @param amount The amount to validate.
   * @param balance The current balance.
   * @param overdraftLimit The overdraft limit.
   * @throws InvalidAmountException if invalid.
   * @throws OverdraftLimitExceededException if overdraft limit exceeded.
   */
  public static void validateCheckingWithdrawal(
      double amount, double balance, double overdraftLimit)
      throws InvalidAmountException, OverdraftLimitExceededException {
    if (amount <= 0) {
      throw new InvalidAmountException("Invalid withdrawal amount. Must be positive.");
    }
    if (amount - balance > overdraftLimit) {
      throw new OverdraftLimitExceededException("Withdrawal exceeds overdraft limit.");
    }
  }
}
