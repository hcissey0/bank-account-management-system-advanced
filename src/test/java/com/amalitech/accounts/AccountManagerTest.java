package com.amalitech.accounts;


import com.amalitech.customers.Customer;
import com.amalitech.customers.RegularCustomer;
import com.amalitech.exceptions.AccountNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountManagerTest {

    private AccountManager accountManager;
    private Customer customer;

    @BeforeEach
    void setUp() {
        accountManager = new AccountManager();
        customer = new RegularCustomer("Alice", 28, "555-0101", "321 Pine St");
    }

    @Test
    void testAddAccount() {
        Account account = new CheckingAccount(customer, 500.0);
        accountManager.addAccount(account);
        assertEquals(1, accountManager.getAccountCount());
    }

    @Test
    void testFindAccountSuccess() throws AccountNotFoundException {
        Account account = new CheckingAccount(customer, 500.0);
        accountManager.addAccount(account);
        Account found = accountManager.findAccount(account.getAccountNumber());
        assertEquals(account, found);
    }

    @Test
    void testFindAccountFailure() {
        assertThrows(AccountNotFoundException.class, () -> accountManager.findAccount("NON_EXISTENT"));
    }

    @Test
    void testGetTotalBalance() {
        accountManager.addAccount(new CheckingAccount(customer, 100.0));
        accountManager.addAccount(new SavingsAccount(customer, 200.0));
        assertEquals(300.0, accountManager.getTotalBalance());
    }
}
