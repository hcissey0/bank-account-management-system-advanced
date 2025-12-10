package com.amalitech.utils;

import java.util.regex.Pattern;

/**
 * Utility class for validating inputs using Regex patterns.
 */
public class ValidationUtils {

    // Regex Patterns
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$";
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
     * @return true if valid, false otherwise.
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates if the provided account number matches the format ACCxxx.
     *
     * @param accountNumber The account number string.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).matches();
    }

    /**
     * Validates if the provided customer ID matches the format CUSxxx.
     *
     * @param customerId The customer ID string.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidCustomerId(String customerId) {
        return customerId != null && CUSTOMER_ID_PATTERN.matcher(customerId).matches();
    }

    /**
     * Validates if the name contains only letters and spaces.
     *
     * @param name The name string.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && NAME_PATTERN.matcher(name).matches();
    }

    /**
     * Validates if the phone number is a 10-digit number.
     *
     * @param phone The phone number string.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Validates if the age is within a reasonable range (18-120).
     * 
     * @param age The age to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidAge(int age) {
        return age >= 18 && age <= 120;
    }
}
