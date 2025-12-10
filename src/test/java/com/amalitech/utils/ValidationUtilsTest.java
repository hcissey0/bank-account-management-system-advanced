package com.amalitech.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"test@example.com", "user.name@domain.co", "user+tag@domain.org"})
    void testValidEmails(String email) {
        assertTrue(ValidationUtils.isValidEmail(email), "Email should be valid: " + email);
    }

    @ParameterizedTest
    @ValueSource(strings = {"plainaddress", "@missingusername.com", "username@.com", "username@domain"})
    void testInvalidEmails(String email) {
        assertFalse(ValidationUtils.isValidEmail(email), "Email should be invalid: " + email);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ACC001", "ACC123", "ACC999"})
    void testValidAccountNumbers(String accNum) {
        assertTrue(ValidationUtils.isValidAccountNumber(accNum), "Account number should be valid: " + accNum);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ACC1", "AC123", "123456", "acc001", "ACC0011"})
    void testInvalidAccountNumbers(String accNum) {
        assertFalse(ValidationUtils.isValidAccountNumber(accNum), "Account number should be invalid: " + accNum);
    }

    @ParameterizedTest
    @ValueSource(strings = {"CUS001", "CUS123", "CUS999"})
    void testValidCustomerIds(String cusId) {
        assertTrue(ValidationUtils.isValidCustomerId(cusId), "Customer ID should be valid: " + cusId);
    }

    @ParameterizedTest
    @ValueSource(strings = {"CUS1", "CU123", "123456", "cus001", "CUS0011"})
    void testInvalidCustomerIds(String cusId) {
        assertFalse(ValidationUtils.isValidCustomerId(cusId), "Customer ID should be invalid: " + cusId);
    }

    @ParameterizedTest
    @ValueSource(strings = {"John Doe", "Alice", "Bob Smith Jr"})
    void testValidNames(String name) {
        assertTrue(ValidationUtils.isValidName(name), "Name should be valid: " + name);
    }

    @ParameterizedTest
    @ValueSource(strings = {"John123", "Alice!", "", "   "})
    void testInvalidNames(String name) {
        assertFalse(ValidationUtils.isValidName(name), "Name should be invalid: " + name);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567890", "0987654321"})
    void testValidPhoneNumbers(String phone) {
        assertTrue(ValidationUtils.isValidPhoneNumber(phone), "Phone number should be valid: " + phone);
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "12345678901", "abcdefghij", "123-456-7890"})
    void testInvalidPhoneNumbers(String phone) {
        assertFalse(ValidationUtils.isValidPhoneNumber(phone), "Phone number should be invalid: " + phone);
    }
    
    @Test
    void testValidAge() {
        assertTrue(ValidationUtils.isValidAge(18));
        assertTrue(ValidationUtils.isValidAge(50));
        assertTrue(ValidationUtils.isValidAge(120));
    }

    @Test
    void testInvalidAge() {
        assertFalse(ValidationUtils.isValidAge(17));
        assertFalse(ValidationUtils.isValidAge(121));
        assertFalse(ValidationUtils.isValidAge(-5));
    }
}
