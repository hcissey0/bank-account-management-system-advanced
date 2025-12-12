# Advanced Search & Filtering Features

## Overview
The Advanced Search module empowers users to query the transaction history with high precision. Unlike standard history views, this feature allows for combining multiple criteria to find specific financial records.

## Search Capabilities
Users can build complex queries by chaining multiple filters. The system interactively asks for each criteria:

### 1. Filter by Account Number
Isolate transactions for a specific account.
- **Input**: Account Number (e.g., `ACC001`)
- **Usage**: Useful for auditing a single customer's activity within the global transaction log.

### 2. Filter by Transaction Type
Narrow down results to specific operation types.
- **Options**:
  - `DEPOSIT`
  - `WITHDRAWAL`
  - `TRANSFER_IN`
  - `TRANSFER_OUT`
- **Usage**: "Show me all withdrawals" or "Show me all incoming transfers".

### 3. Filter by Amount Range
Find transactions based on their monetary value.
- **Min Amount**: Floor value (inclusive).
- **Max Amount**: Ceiling value (inclusive).
- **Logic**: Users can specify just a minimum (e.g., "> $500"), just a maximum (e.g., "< $100"), or both (e.g., "$100 - $500").

### 4. Filter by Date Range
Find recent transactions based on time.
- **Input**: Number of days (N).
- **Logic**: Returns transactions from the last N days.
- **Example**: Entering `7` filters for transactions from the last week.

## Technical Implementation

### Java Streams & Predicates
This feature showcases the power of functional programming in Java 21.

- **Dynamic Predicate Construction**:
  The search logic starts with a base predicate (`t -> true`) and dynamically chains additional conditions using `Predicate.and()`.
  ```java
  Predicate<Transaction> predicate = t -> true;
  
  if (filterByType) {
      predicate = predicate.and(t -> t.getType() == selectedType);
  }
  if (filterByAmount) {
      predicate = predicate.and(t -> t.getAmount() >= minAmount);
  }
  ```

- **Stream Processing**:
  The final composite predicate is passed to the `TransactionManager`, which uses the Stream API to filter and sort the data efficiently.
  ```java
  return transactions.stream()
      .filter(predicate)
      .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
      .collect(Collectors.toList());
  ```

## Usage Example
**Scenario**: Find large withdrawals made in the last week.
1. Select **Search Transactions** from the menu.
2. Filter by Account? **No** (Search all accounts).
3. Filter by Type? **Yes** -> Select **WITHDRAWAL**.
4. Filter by Amount? **Yes** -> Min: **1000**, Max: **0** (No max).
5. Filter by Date? **Yes** -> Enter Days: **7**.

**Result**: A table showing all withdrawals over $1,000.00 from the last 7 days.
