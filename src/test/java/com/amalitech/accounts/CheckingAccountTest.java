package com.amalitech.accounts;

import com.amalitech.customers.Customer;
import com.amalitech.customers.RegularCustomer;
import com.amalitech.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckingAccountTest {

    private CheckingAccount checkingAccount;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new RegularCustomer("Jane Doe", 25, "0987654321", "456 Elm St");
        checkingAccount = new CheckingAccount(customer, 100.0);
    }

    @Test
    void testConstructor() {
        assertEquals(100.0, checkingAccount.getBalance());
        assertEquals(1000.0, checkingAccount.getOverdraftLimit());
        assertEquals(10.0, checkingAccount.getMonthlyFee());
        assertEquals("Checking", checkingAccount.getAccountType());
    }

    @Test
    void testGetMonthlyFee() {
        assertEquals(10.0, checkingAccount.getMonthlyFee());
    }

    @Test
    void testGetOverdraftLimit() {
        assertEquals(1000.0, checkingAccount.getOverdraftLimit());
    }

    @Test
    void testApplyMonthlyFee() {
        checkingAccount.applyMonthlyFee();
        assertEquals(90.0, checkingAccount.getBalance());
    }

    @Test
    void testApplyMonthlyFeeInsufficientBalance() {
        // Fee is 10. Set balance to 5.
        checkingAccount.withdraw(95.0); // Balance becomes 5
        checkingAccount.applyMonthlyFee();
        assertEquals(5.0, checkingAccount.getBalance());
    }

    @Test
    void testWithdrawWithinBalance() {
        checkingAccount.withdraw(50.0);
        assertEquals(50.0, checkingAccount.getBalance());
    }

    @Test
    void testWithdrawWithinOverdraft() {
        // Balance 100. Overdraft 1000. Can withdraw up to 1100.
        // Withdraw 200. Balance should be -100.
        checkingAccount.withdraw(200.0);
        assertEquals(-100.0, checkingAccount.getBalance());
    }

    @Test
    void testWithdrawExceedingOverdraft() {
        // Balance 100. Overdraft 1000. Max withdraw 1100.
        // Try 1200.
        double result = checkingAccount.withdraw(1200.0);
        assertEquals(-1, result); // Returns -1 on failure
        assertEquals(100.0, checkingAccount.getBalance()); // Balance unchanged
    }

    @Test
    void testDisplayAccountDetails() {
        // Just ensure no exceptions are thrown during display
        assertDoesNotThrow(() -> checkingAccount.displayAccountDetails());
    }

    @Test
    void testGetAccountType() {
        assertEquals("Checking", checkingAccount.getAccountType());
    }

    @Test
    void testValidateWithdrawalSuccess() {
        assertDoesNotThrow(() -> checkingAccount.processTransaction(200.0, "Withdrawal"));
    }

    @Test
    void testValidateWithdrawalFailure() {
        assertThrows(InsufficientFundsException.class, () -> checkingAccount.processTransaction(1200.0, "Withdrawal"));
    }
}
