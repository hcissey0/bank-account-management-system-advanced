# Testing Strategy

## Overview
The project employs a comprehensive testing strategy using JUnit 5 to ensure code quality and reliability. The test suite covers unit tests for models and services, as well as integration scenarios.

## Test Structure
Tests are located in `src/test/java` and mirror the package structure of the main application.

### 1. Unit Tests
Focused on testing individual classes in isolation.
- **Models**: Verify that entities behave correctly (e.g., `SavingsAccountTest` checks interest calculations).
- **Services**: Verify business logic (e.g., `AccountManagerTest` checks add/remove functionality).
- **Utils**: Verify helper functions (e.g., `ValidationUtilsTest` checks regex patterns).

### 2. Integration Tests
Verify that components work together correctly.
- **Persistence**: `FilePersistenceServiceTest` verifies that data can be saved to disk and reloaded accurately (Round-trip testing).
- **Data Isolation**: Tests use a separate data directory (`src/test/resources/data`) to avoid corrupting production data.

## Key Test Scenarios

### Account Operations
- Creating accounts with valid/invalid inputs.
- Deposit and Withdrawal logic.
- Interest calculation for Savings accounts.
- Overdraft protection for Checking accounts.

### Transaction Processing
- Successful transfers between accounts.
- Failed transfers due to insufficient funds.
- Verification of transaction history recording.

### Concurrency
- Simulating multiple threads accessing the same account to verify thread safety and `synchronized` blocks.

## Running Tests
Tests can be executed using Maven or the built-in `TestLauncher`.

```bash
# Run all tests via Maven
mvn test

# Run specific test class
mvn -Dtest=AccountManagerTest test
```
