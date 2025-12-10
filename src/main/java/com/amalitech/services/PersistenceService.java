package com.amalitech.services;

import com.amalitech.models.Account;
import com.amalitech.models.Customer;
import com.amalitech.models.Transaction;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Interface for persisting and loading bank data. This abstraction allows for different storage
 * implementations (File, Database, etc.) without affecting the business logic services.
 */
public interface PersistenceService {

  /**
   * Loads accounts from storage.
   *
   * @param customers Map of existing customers to link to accounts
   * @return HashMap of account number to Account object
   * @throws IOException if storage operations fail
   */
  HashMap<String, Account> loadAccounts(HashMap<String, Customer> customers) throws IOException;

  /**
   * Saves accounts to storage.
   *
   * @param accounts HashMap of accounts to save
   * @throws IOException if storage operations fail
   */
  void saveAccounts(HashMap<String, Account> accounts) throws IOException;

  /**
   * Loads customers from storage.
   *
   * @return HashMap of customer ID to Customer object
   * @throws IOException if storage operations fail
   */
  HashMap<String, Customer> loadCustomers() throws IOException;

  /**
   * Saves customers to storage.
   *
   * @param customers HashMap of customers to save
   * @throws IOException if storage operations fail
   */
  void saveCustomers(HashMap<String, Customer> customers) throws IOException;

  /**
   * Loads transactions from storage.
   *
   * @return List of Transaction objects
   * @throws IOException if storage operations fail
   */
  List<Transaction> loadTransactions() throws IOException;

  /**
   * Saves transactions to storage.
   *
   * @param transactions List of transactions to save
   * @throws IOException if storage operations fail
   */
  void saveTransactions(List<Transaction> transactions) throws IOException;
}
