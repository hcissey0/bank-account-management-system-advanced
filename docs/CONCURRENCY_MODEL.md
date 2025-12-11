# Concurrency & Thread Safety

## Overview
The Bank Account Management System is designed to handle concurrent operations safely. It employs synchronization mechanisms to ensure data consistency when multiple threads access the same resources, simulating a real-world banking environment where multiple transactions might occur simultaneously.

## Thread Safety Mechanisms

### 1. Synchronized Methods
Critical state-modifying methods in the Account models are marked as `synchronized`. This ensures that only one thread can modify an account's balance at a time.

- **`Account.deposit(double amount)`**: Synchronized to prevent lost updates during concurrent deposits.
- **`SavingsAccount.withdraw(double amount)`**: Synchronized to ensure balance checks and updates are atomic.
- **`CheckingAccount.withdraw(double amount)`**: Synchronized to strictly enforce overdraft limits.

### 2. Deadlock Prevention in Transfers
Transfer operations involve locking two account objects simultaneously. To prevent deadlocks (where Thread A holds Account 1 and waits for Account 2, while Thread B holds Account 2 and waits for Account 1), the system uses **Lock Ordering**.

**Strategy**:
- Accounts are always locked in a consistent order based on their Account Number (lexicographical order).
- **Implementation**:
  ```java
  Object lock1 = fromAccountNumber.compareTo(toAccountNumber) < 0 ? fromAccount : toAccount;
  Object lock2 = fromAccountNumber.compareTo(toAccountNumber) < 0 ? toAccount : fromAccount;

  synchronized (lock1) {
      synchronized (lock2) {
          // Perform transfer
      }
  }
  ```

## Concurrency Simulation
The system includes utilities to simulate concurrent load, allowing developers to verify thread safety.
- **Scenario**: Multiple threads performing deposits and withdrawals on the same account simultaneously.
- **Verification**: The final balance is checked against the expected mathematical result to ensure no "lost updates" occurred.
