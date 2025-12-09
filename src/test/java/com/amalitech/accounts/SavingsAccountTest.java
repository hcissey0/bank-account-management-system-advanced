package com.amalitech.accounts;

import com.amalitech.customers.Customer;
import com.amalitech.customers.RegularCustomer;
import com.amalitech.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest {

    private SavingsAccount savingsAccount;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new RegularCustomer("Bob Smith", 40, "1122334455", "789 Oak St");
        savingsAccount = new SavingsAccount(customer, 1000.0);
    }

    @Test
    void testConstructor() {
        assertEquals(1000.0, savingsAccount.getBalance());
        assertEquals(3.5, savingsAccount.getInterestRate());
        assertEquals(500.0, savingsAccount.getMinimumBalance());
        assertEquals("Savings", savingsAccount.getAccountType());
    }

    @Test
    void testCalculateInterest() {
        // 1000 * 3.5 = 3500.
        assertEquals(3500.0, savingsAccount.calculateInterest());
    }

    @Test
    void testWithdrawSuccess() {
        // Min balance 500. Balance 1000. Can withdraw 500.
        savingsAccount.withdraw(200.0);
        assertEquals(800.0, savingsAccount.getBalance());
    }

    @Test
    void testWithdrawFailure() {
        // Min balance 500. Balance 1000. Try withdraw 600. Remaining 400 < 500.
        double result = savingsAccount.withdraw(600.0);
        assertEquals(-1, result);
        assertEquals(1000.0, savingsAccount.getBalance());
    }

    @Test
    void testValidateWithdrawalSuccess() {
        assertDoesNotThrow(() -> savingsAccount.processTransaction(200.0, "Withdrawal"));
    }

    @Test
    void testValidateWithdrawalFailure() {
        assertThrows(InsufficientFundsException.class, () -> savingsAccount.processTransaction(600.0, "Withdrawal"));
    }
}
