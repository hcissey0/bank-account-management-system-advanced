package com.amalitech.accounts;

import com.amalitech.customers.Customer;
import com.amalitech.customers.RegularCustomer;
import com.amalitech.exceptions.BankException;
import com.amalitech.exceptions.InvalidAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private TestAccount account;
    private Customer customer;

    // Concrete implementation for testing abstract Account class
    private static class TestAccount extends Account {
        TestAccount(Customer customer) {
            super(customer);
        }

        @Override
        public void displayAccountDetails() {
            // Intentionally empty for testing purposes
        }

        @Override
        public String getAccountType() {
            return "Test";
        }

        @Override
        protected void validateWithdrawal(double amount) throws BankException {
            if (amount > getBalance()) {
                throw new InvalidAmountException("Insufficient funds");
            }
        }
    }

    @BeforeEach
    void setUp() {
        customer = new RegularCustomer("John Doe", 30, "1234567890", "123 Main St");
        account = new TestAccount(customer);
    }

    @Test
    void testAccountNumberGeneration() {
        String accountNumber = account.getAccountNumber();
        assertNotNull(accountNumber);
        assertTrue(accountNumber.startsWith("ACC"));
        System.out.println("Account Number Test Passed");
    }

    @Test
    void testDeposit() {
        account.deposit(100.0);
        assertEquals(100.0, account.getBalance());
    }

    @Test
    void testWithdraw() {
        account.deposit(100.0);
        account.withdraw(50.0);
        assertEquals(50.0, account.getBalance());
    }

    @Test
    void testProcessTransactionDeposit() throws BankException {
        account.processTransaction(100.0, "Deposit");
        assertEquals(100.0, account.getBalance());
    }

    @Test
    void testProcessTransactionWithdrawal() throws BankException {
        account.deposit(100.0);
        account.processTransaction(50.0, "Withdrawal");
        assertEquals(50.0, account.getBalance());
    }

    @Test
    void testProcessTransactionWithWrongType() {
        assertThrows(BankException.class, () -> account.processTransaction(100.0, "InvalidType"));
    }

    @Test
    void testProcessTransactionInvalidType() {
        assertThrows(BankException.class, () -> account.processTransaction(100.0, "Invalid"));
    }

    @Test
    void testValidateDepositNegativeAmount() {
        assertThrows(InvalidAmountException.class, () -> account.validateDeposit(-10.0));
    }

    @Test
    void testValidateWithdrawalInsufficientFunds() {
        assertThrows(InvalidAmountException.class, () -> account.validateWithdrawal(10.0));
    }

    @Test
    void testGetters() {
        assertNotNull(account.getAccountNumber());
        assertEquals(customer, account.getCustomer());
        assertEquals("Active", account.getStatus());
        assertEquals(0.0, account.getBalance());
    }

    @Test
    void testSetters() {
        account.setBalance(200.0);
        assertEquals(200.0, account.getBalance());
    }
}
