# Reporting Features

## Overview
The Reporting module provides insights into the financial health of the bank and individual accounts. It generates formatted outputs for both administrative and customer use.

## Available Reports

### 1. Bank Statement
A detailed statement for a specific customer account.
- **Scope**: Single Account.
- **Content**:
  - Account Details (Owner, Type, Current Balance).
  - Transaction History (Chronological order).
  - **Summary Section**:
    - Total Deposits
    - Total Withdrawals
    - Total Transfers In/Out
    - Net Change

### 2. Bank Summary
A high-level dashboard for bank administrators.
- **Scope**: Entire System.
- **Metrics**:
  - **Accounts**: Total count, breakdown by type (Savings vs Checking), total liquidity.
  - **Customers**: Total count, breakdown by tier (Regular vs Premium).
  - **Transactions**: Total volume, breakdown by type.

## Formatting
- **Tables**: Data is presented in aligned ASCII tables for readability in the console.
- **Currency**: All monetary values are formatted with currency symbols and two decimal places (e.g., `$1,234.56`).
- **Dates**: Timestamps are formatted for human readability.
