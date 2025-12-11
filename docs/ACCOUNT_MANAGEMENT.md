# Account Management Features

## Overview
The Account Management module allows bank administrators to create and manage customer bank accounts. The system supports multiple account types with specific business rules and validation logic.

## Account Types

### 1. Savings Account
Designed for customers who want to earn interest on their deposits.
- **Interest Rate**: 3.5%
- **Minimum Balance**: $500.00
- **Features**:
  - Interest calculation capability
  - Strict minimum balance enforcement during withdrawals

### 2. Checking Account
Designed for daily transactional use with overdraft protection.
- **Monthly Fee**: $10.00
- **Overdraft Limit**: $1,000.00
- **Features**:
  - Allows withdrawals below zero up to the overdraft limit
  - Monthly fee deduction capability

## Key Operations

### Create Account
Allows creating a new account for an existing customer.
- **Process**:
  1. Select Customer (new or existing)
  2. Choose Account Type (Savings/Checking)
  3. Enter Initial Deposit
     - **Savings**: Must be at least $500.00
     - **Premium Customers**: Must be at least $10,000.00 regardless of account type
  4. System generates a unique Account Number (e.g., `ACC001`)

### Find Account
Search for an account using its unique Account Number.
- **Input**: Account Number (Format: `ACC` followed by 3 digits)
- **Output**: Displays full account details including balance, owner, and type.

### View All Accounts
Displays a tabular list of all accounts in the system.
- **Columns**: Account Number, Customer Name, Type, Balance.

### Account Summary
Provides high-level statistics about the bank's portfolio.
- **Metrics**:
  - Total number of accounts
  - Total liquidity (sum of all balances)
