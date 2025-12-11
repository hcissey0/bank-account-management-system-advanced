# Data Persistence & Storage

## Overview
The application uses a file-based persistence system to ensure data is preserved between sessions. It handles the serialization and deserialization of objects to text files.

## Storage Mechanism

### File Formats
Data is stored in CSV-like text files in the `src/main/resources/data/` directory.
- **`customers.txt`**: Stores customer profiles.
- **`accounts.txt`**: Stores account details and balances.
- **`transactions.txt`**: Stores the complete transaction history.

### Lifecycle
1. **Startup (Load)**:
   - The system reads all files.
   - Objects are reconstructed and linked (e.g., Accounts are linked to their Owners).
   - ID counters are synchronized to prevent duplicates (See `ID_PRESERVATION_DEMO.md`).

2. **Runtime**:
   - Data is held in memory for fast access.
   - Modifications (new accounts, transactions) happen in memory first.

3. **Shutdown (Save)**:
   - On exit, the system serializes all in-memory objects.
   - Files are overwritten with the latest state.

## Configuration
- **Auto-Load**: Can be configured to load data automatically when the app starts.
- **Save-on-Exit**: Can be configured to save data automatically when the app closes.

## Advanced Features
- **Test Isolation**: Separate data directories are used for testing to prevent corruption of production data. (See `DATA_STORAGE_SEPARATION.md`).
- **ID Preservation**: The system intelligently tracks the last used IDs to ensure continuity across restarts.
