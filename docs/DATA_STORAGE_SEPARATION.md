# Data Storage Separation

## Overview
The application now uses separate data directories for production and testing to prevent data loss and test interference.

## Directory Structure

```
src/
├── main/
│   └── resources/
│       └── data/              # Production data (persists between runs)
│           ├── accounts.txt
│           ├── customers.txt
│           └── transactions.txt
└── test/
    └── resources/
        └── data/              # Test data (cleared after each test)
            ├── accounts.txt
            ├── customers.txt
            └── transactions.txt
```

## Benefits

### 1. **Data Persistence**
- Production data in `src/main/resources/data/` is preserved across application runs
- Running tests no longer clears your production data

### 2. **Test Isolation**
- Tests use `src/test/resources/data/` directory
- Each test starts with a clean state
- Test data is automatically cleaned up after each test

### 3. **No Data Conflicts**
- Tests and main application never interfere with each other
- You can run tests without losing your saved data

## How It Works

### FilePersistenceService
The service now accepts an optional data directory path:

```java
// Production (default)
FilePersistenceService service = new FilePersistenceService();
// Uses: src/main/resources/data/

// Testing (custom path)
FilePersistenceService service = new FilePersistenceService("src/test/resources/data/");
// Uses: src/test/resources/data/
```

### Main Application
Uses the default constructor, which points to production data:
```java
FilePersistenceService persistenceService = new FilePersistenceService();
// Data saved to src/main/resources/data/
```

### Tests
All test classes use the test directory:
```java
FilePersistenceService persistenceService = new FilePersistenceService("src/test/resources/data/");
// Data saved to src/test/resources/data/
```

## Git Configuration

Test data files are ignored by Git (added to `.gitignore`):
```gitignore
### Test Data Files ###
src/test/resources/data/*.txt
```

This ensures test-generated files don't clutter the repository while production data can be tracked if needed.

## Modified Files

1. **FilePersistenceService.java**
   - Changed from static constants to instance variables
   - Added constructor with configurable data directory
   - Default constructor maintains backward compatibility

2. **Test Classes**
   - AccountManagerTest.java
   - CustomerManagerTest.java
   - FilePersistenceServiceTest.java
   - All now use `new FilePersistenceService("src/test/resources/data/")`

3. **.gitignore**
   - Added exclusion for test data files

## Usage

### Running the Application
```bash
mvn exec:java -Dexec.mainClass="com.amalitech.main.Main"
# Data persists in src/main/resources/data/
```

### Running Tests
```bash
mvn test
# Uses src/test/resources/data/ (cleared automatically)
```

### Result
You can now:
- Run the application and save data
- Run tests multiple times
- Return to the application with your data intact ✅
