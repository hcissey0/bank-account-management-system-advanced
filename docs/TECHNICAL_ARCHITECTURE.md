# Technical Architecture

## Overview
This document outlines the technical structure and design decisions of the Bank Account Management System. The application follows a layered architecture with a focus on separation of concerns, modularity, and modern Java practices.

## Project Structure

### 1. Models (`com.amalitech.models`)
- **Entities**: `Account`, `Customer`, `Transaction`.
- **Inheritance**:
  - `Account` (Abstract) -> `SavingsAccount`, `CheckingAccount`.
  - `Customer` (Abstract) -> `RegularCustomer`, `PremiumCustomer`.
- **Design**: Rich domain models that encapsulate their own state and basic validation logic.

### 2. Services (`com.amalitech.services`)
- **Managers**: `AccountManager`, `CustomerManager`, `TransactionManager`.
  - Responsible for business logic and orchestration.
  - Maintain in-memory collections of entities.
- **Persistence**: `FilePersistenceService`.
  - Handles low-level I/O operations.
  - Decoupled from business logic.

### 3. Utilities (`com.amalitech.utils`)
- **Helpers**: `ValidationUtils`, `InputReader`, `ConsoleTablePrinter`.
- **Role**: Provide shared functionality across the application without holding state.

### 4. Main (`com.amalitech.main`)
- **Entry Point**: `Main.java`.
- **UI Logic**: `MenuHandler`, `MenuDisplay`, `*Operations` classes.
- **Role**: Handles user interaction and delegates tasks to Services.

## Key Technical Decisions

### Collections Framework
The system has migrated from fixed-size arrays to the Java Collections Framework for flexibility and performance.
- **`HashMap<String, Account>`**: Used for storing accounts. Provides O(1) access time by Account Number.
- **`ArrayList<Transaction>`**: Used for storing transaction history. Allows dynamic growth.
- **Streams API**: Used extensively for filtering, sorting, and aggregating data (e.g., generating reports).

### Enums for Constants
String literals have been replaced with Java Enums to ensure type safety and reduce errors.
- `AccountType`: `SAVINGS`, `CHECKING`
- `CustomerType`: `REGULAR`, `PREMIUM`
- `TransactionType`: `DEPOSIT`, `WITHDRAWAL`, `TRANSFER_IN`, `TRANSFER_OUT`

### Exception Handling
A custom exception hierarchy is used to manage business errors.
- `BankException` (Base)
  - `AccountNotFoundException`
  - `InsufficientFundsException`
  - `InvalidInputException`
