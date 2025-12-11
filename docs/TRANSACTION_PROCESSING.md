# Transaction Processing Features

## Overview
The Transaction Processing module handles all financial movements within the system. It ensures data integrity, enforces business rules, and maintains a complete audit trail of all activities.

## Transaction Types

### 1. Deposit
Adding funds to an account.
- **Validation**: Amount must be positive.
- **Effect**: Increases account balance.
- **Record**: Logged as `DEPOSIT`.

### 2. Withdrawal
Removing funds from an account.
- **Validation**:
  - Amount must be positive.
  - **Savings**: Cannot breach minimum balance ($500).
  - **Checking**: Cannot exceed overdraft limit ($1,000).
- **Effect**: Decreases account balance.
- **Record**: Logged as `WITHDRAWAL`.

### 3. Transfer
Moving funds between two accounts.
- **Process**:
  1. Withdraw from Source Account.
  2. Deposit to Destination Account.
- **Validation**: Source account must have sufficient funds.
- **Record**:
  - Source: Logged as `TRANSFER_OUT`.
  - Destination: Logged as `TRANSFER_IN`.

## Key Operations

### Process Transaction
The central interface for performing financial operations.
1. **Identify Account**: User enters Account Number.
2. **Select Type**: Deposit, Withdrawal, or Transfer.
3. **Enter Amount**: System validates the amount against business rules.
4. **Confirmation**: Transaction is executed and saved.

### Transaction History
View the chronological list of operations for a specific account.
- **Details Shown**:
  - Transaction ID (e.g., `TXN001`)
  - Type (Deposit/Withdrawal/Transfer)
  - Amount
  - Date/Time

## Audit & Security
- **Immutability**: Once created, transactions cannot be altered.
- **IDs**: Every transaction gets a unique ID.
- **Timestamps**: All actions are time-stamped for audit purposes.
