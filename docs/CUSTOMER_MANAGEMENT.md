# Customer Management Features

## Overview
The Customer Management module handles the registration and maintenance of bank customer profiles. It supports different customer tiers to offer tailored banking experiences.

## Customer Types

### 1. Regular Customer
Standard customers with access to basic banking services.
- **ID Prefix**: `CUS`
- **Requirements**: Standard personal information (Name, Age, Contact, Address).

### 2. Premium Customer
High-value customers with exclusive benefits and stricter entry requirements.
- **ID Prefix**: `CUS`
- **Benefits**:
  - Priority service (implied)
  - Access to exclusive account features
- **Requirements**:
  - Minimum initial deposit of $10,000.00 when opening accounts.

## Key Operations

### Add Customer
Register a new customer in the system.
- **Inputs**:
  - Name
  - Age
  - Contact Number
  - Address
  - Customer Type (Regular/Premium)
- **Validation**:
  - Name must not be empty.
  - Age must be realistic.
  - Contact details must follow standard formats.
- **Output**: Auto-generated Customer ID (e.g., `CUS001`).

### Find Customer
Locate a specific customer profile.
- **Search Key**: Customer ID.
- **Output**: Full profile details including linked accounts (if implemented).

### View All Customers
Lists all registered customers in a formatted table.
- **Columns**: ID, Name, Type, Contact Info.

## Integration
- **Account Creation**: A valid customer profile is required before creating any bank account.
- **Data Persistence**: Customer data is saved to `customers.txt` and loaded on startup.
