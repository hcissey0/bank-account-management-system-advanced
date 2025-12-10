package com.amalitech.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.amalitech.exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ValidationUtilsTest {

  @ParameterizedTest
  @ValueSource(strings = {"test@example.com", "user.name@domain.co", "user+tag@domain.org"})
  void testValidEmails(String email) {
    assertDoesNotThrow(
        () -> ValidationUtils.validateEmail(email), "Email should be valid: " + email);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {"plainaddress", "@missingusername.com", "username@.com", "username@domain"})
  void testInvalidEmails(String email) {
    assertThrows(
        InvalidInputException.class,
        () -> ValidationUtils.validateEmail(email),
        "Email should be invalid: " + email);
  }

  @ParameterizedTest
  @ValueSource(strings = {"ACC001", "ACC123", "ACC999"})
  void testValidAccountNumbers(String accNum) {
    assertDoesNotThrow(
        () -> ValidationUtils.validateAccountNumber(accNum),
        "Account number should be valid: " + accNum);
  }

  @ParameterizedTest
  @ValueSource(strings = {"ACC1", "AC123", "123456", "acc001", "ACC0011"})
  void testInvalidAccountNumbers(String accNum) {
    assertThrows(
        InvalidInputException.class,
        () -> ValidationUtils.validateAccountNumber(accNum),
        "Account number should be invalid: " + accNum);
  }

  @ParameterizedTest
  @ValueSource(strings = {"CUS001", "CUS123", "CUS999"})
  void testValidCustomerIds(String cusId) {
    assertDoesNotThrow(
        () -> ValidationUtils.validateCustomerId(cusId), "Customer ID should be valid: " + cusId);
  }

  @ParameterizedTest
  @ValueSource(strings = {"CUS1", "CU123", "123456", "cus001", "CUS0011"})
  void testInvalidCustomerIds(String cusId) {
    assertThrows(
        InvalidInputException.class,
        () -> ValidationUtils.validateCustomerId(cusId),
        "Customer ID should be invalid: " + cusId);
  }

  @ParameterizedTest
  @ValueSource(strings = {"John Doe", "Alice", "Bob Smith Jr"})
  void testValidNames(String name) {
    assertDoesNotThrow(() -> ValidationUtils.validateName(name), "Name should be valid: " + name);
  }

  @ParameterizedTest
  @ValueSource(strings = {"John123", "Alice!", "", "   "})
  void testInvalidNames(String name) {
    assertThrows(
        InvalidInputException.class,
        () -> ValidationUtils.validateName(name),
        "Name should be invalid: " + name);
  }

  @ParameterizedTest
  @ValueSource(strings = {"1234567890", "0987654321"})
  void testValidPhoneNumbers(String phone) {
    assertDoesNotThrow(
        () -> ValidationUtils.validatePhoneNumber(phone), "Phone number should be valid: " + phone);
  }

  @ParameterizedTest
  @ValueSource(strings = {"12345", "12345678901", "abcdefghij", "123-456-7890"})
  void testInvalidPhoneNumbers(String phone) {
    assertThrows(
        InvalidInputException.class,
        () -> ValidationUtils.validatePhoneNumber(phone),
        "Phone number should be invalid: " + phone);
  }

  @Test
  void testValidAge() {
    assertDoesNotThrow(() -> ValidationUtils.validateAge(18));
    assertDoesNotThrow(() -> ValidationUtils.validateAge(50));
    assertDoesNotThrow(() -> ValidationUtils.validateAge(120));
  }

  @Test
  void testInvalidAge() {
    assertThrows(InvalidInputException.class, () -> ValidationUtils.validateAge(17));
    assertThrows(InvalidInputException.class, () -> ValidationUtils.validateAge(121));
    assertThrows(InvalidInputException.class, () -> ValidationUtils.validateAge(-5));
  }
}
