# ID Preservation Feature

## Overview
The system now fully preserves all auto-generated IDs (Customer IDs, Account Numbers, Transaction IDs) and timestamps across application restarts.

## How It Works

### 1. **New Constructors**
Each model class now has two constructors:
- **Standard Constructor**: Auto-generates new ID (e.g., `new RegularCustomer(name, age, contact, address)`)
- **Persistence Constructor**: Accepts existing ID (e.g., `new RegularCustomer("CUS050", name, age, contact, address)`)

### 2. **Counter Management**
When loading data from files, the system:
1. Parses all entities using persistence constructors to preserve their IDs
2. Extracts the maximum numeric value from all loaded IDs
3. Sets the static counter to this maximum value
4. Ensures new entities get IDs higher than any existing ones

### 3. **Example Flow**

#### Session 1:
```java
// Create customers - IDs auto-generated
Customer alice = new RegularCustomer("Alice", 28, "555-1234", "123 Main St");
// alice.getCustomerId() returns "CUS001"

Customer bob = new PremiumCustomer("Bob", 35, "555-5678", "456 Oak Ave");
// bob.getCustomerId() returns "CUS002"

// System saves to file and exits
```

#### Session 2 (After Restart):
```java
// System loads from file
// - Loads CUS001 (Alice) and CUS002 (Bob) with original IDs
// - Sets Customer.customerCounter = 2

// Create new customer - ID continues from max loaded
Customer charlie = new RegularCustomer("Charlie", 30, "555-9999", "789 Elm St");
// charlie.getCustomerId() returns "CUS003" (not CUS001!)
```

## Benefits

1. **Data Integrity**: Customer IDs, Account Numbers, and Transaction IDs remain stable across restarts
2. **Reference Consistency**: Foreign key relationships (e.g., Account → Customer) are preserved
3. **Audit Trail**: Transaction IDs and timestamps maintain historical accuracy
4. **No ID Collisions**: New entities never reuse IDs from loaded data

## Technical Details

### Modified Classes:
- `Customer.java` - Added `setCustomerCounter()` and protected constructor accepting ID
- `RegularCustomer.java` - Added persistence constructor
- `PremiumCustomer.java` - Added persistence constructor
- `Account.java` - Added `setAccountCounter()` and protected constructor accepting account number
- `SavingsAccount.java` - Added persistence constructor
- `CheckingAccount.java` - Added persistence constructor
- `Transaction.java` - Added `setTransactionCounter()` (already had persistence constructor)
- `FilePersistenceService.java` - Updated to use persistence constructors and restore counters

### Counter Update Logic:
```java
private void updateCustomerCounter(HashMap<String, Customer> customers) {
    int maxId = customers.keySet().stream()
        .map(id -> id.replace("CUS", ""))
        .filter(numStr -> numStr.matches("\\d+"))
        .mapToInt(Integer::parseInt)
        .max()
        .orElse(0);
    Customer.setCustomerCounter(maxId);
}
```

## Testing

The `testIdPreservationAcrossSessions()` test verifies:
- Customer IDs are preserved (CUS050, CUS075)
- Account Numbers are preserved (ACC100, ACC200)
- Transaction IDs are preserved (TXN999)
- Timestamps are preserved
- New entities get IDs higher than loaded maximums
- Counters are properly restored

**Test Result**: ✅ All 81 tests passing
