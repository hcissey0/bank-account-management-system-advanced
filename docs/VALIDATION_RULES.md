# Validation Rules & Security

## Overview
The application enforces strict data validation rules to ensure data integrity and prevent invalid states. All user inputs are validated against specific patterns before being processed by the system.

## Validation Strategy
Validation is centralized in the `ValidationUtils` class and uses Regular Expressions (Regex) for pattern matching.

## Input Rules

### 1. Account Numbers
- **Format**: `ACC` followed by 3 digits.
- **Pattern**: `^ACC\d{3}$`
- **Example**: `ACC001`, `ACC999`
- **Invalid**: `ACC1`, `acc001`, `123`

### 2. Customer IDs
- **Format**: `CUS` followed by 3 digits.
- **Pattern**: `^CUS\d{3}$`
- **Example**: `CUS001`, `CUS050`
- **Invalid**: `CUS1`, `cus001`

### 3. Personal Information
- **Name**:
  - Must contain only letters and spaces.
  - Cannot be empty or whitespace only.
- **Email**:
  - Must follow standard email format: `user@domain.com`.
  - **Pattern**: `^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*\.[A-Za-z]{2,}$`
- **Phone Number**:
  - Must be exactly 10 digits.
  - **Pattern**: `^\d{10}$`
- **Age**:
  - Must be between 18 and 120 years old.

## Business Logic Validation

### Financial Rules
- **Deposits**: Amount must be > 0.
- **Withdrawals**:
  - Amount must be > 0.
  - **Savings**: Balance cannot fall below $500.00.
  - **Checking**: Balance cannot fall below -$1,000.00 (Overdraft).

### Exception Handling
The system uses custom exceptions to handle validation failures gracefully:
- `InvalidInputException`: For format errors (Regex mismatch).
- `InvalidAmountException`: For negative amounts.
- `InsufficientFundsException`: For withdrawal limits.
- `OverdraftLimitExceededException`: Specific to Checking accounts.
