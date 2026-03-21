# Scenario 13: JPA Attribute Converters (Encryption)

Demonstrates how to transparently encrypt sensitive data before it hits the database.

## Concept
Security compliance often requires "Encryption at Rest." JPA Attribute Converters allow you to write logic that automatically encrypts a field when saving and decrypts it when reading, without changing your business logic.

## Implementation Details
We implemented a `SecretConverter` that uses AES encryption to wrap the `sensitiveData` field.

### Entity Snippet:
```java
@Convert(converter = AESEncryptionConverter.class)
private String sensitiveData;
```

## Verification Results
- **Save**: Call `/api/scenario13/save?username=Alice&data=MySecret123`.
- **Fetch**: Call `/api/scenario13/get/Alice`.
- **Result**:
  - `decryptedValueInsideJava`: "MySecret123" (Readable).
  - `rawEncryptedValueInDb`: "XyZ123...==" (Base64 Encrypted String).

## Interview Theory: Data Privacy
- **The Advantages**: The Java code always sees plain text, so searching and processing "inside Java" is easy.
- **The Limitations**: You cannot use SQL functions like `LIKE` or `SUM()` on encrypted columns because the database only sees scrambled text.
- **Key Management**: In a real app, never hardcode the AES key. Use a secrets manager like **AWS KMS** or **HashiCorp Vault**.
