# JPA Attribute Converters & Encryption — Complete Interview Reference

## Table of Contents
1. [What is an Attribute Converter?](#1-what-is-an-attribute-converter)
2. [Why Use Converters?](#2-why-use-converters)
3. [Implementing AttributeConverter<X, Y>](#3-implementing-converter)
4. [Encryption at Rest (The Main Use Case)](#4-encryption-at-rest)
5. [The Classic Interview Trap: Querying Encrypted Data](#5-the-classic-interview-trap-querying)
6. [Auto-Applying Converters (@Converter(autoApply = true))](#6-auto-applying)
7. [Converters vs User Types](#7-converters-vs-user-types)
8. [Handling Null Values in Converters](#8-handling-nulls)
9. [Performance Impact of Encryption](#9-performance-impact)
10. [Key Management for Encryption](#10-key-management)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is an Attribute Converter?
A JPA feature that allows you to transform an entity attribute's state to a different database representation and back again.

---

## 2. Why Use Converters?
- **Encryption**: Encrypt data before it hits the DB.
- **Custom Types**: Store a Java `List<String>` as a comma-separated string in a single DB column.
- **Legacy Mapping**: Mapping a complex Java object to a JSON string in the DB.

---

## 3. Implementing AttributeConverter<X, Y>
You must implement two methods:
- `convertToDatabaseColumn`: Java -> DB
- `convertToEntityAttribute`: DB -> Java

---

## 4. Encryption at Rest (The Main Use Case)
Converters are perfect for transparently encrypting PII (Personally Identifiable Information) like Social Security Numbers or Credit Card details. The application sees plain text, but the DB sees ciphertext.

---

## 5. The Classic Interview Trap: Querying Encrypted Data
**The Trap**: If you encrypt a `lastName` column using a converter, you **cannot** perform a `WHERE lastName LIKE 'S%'` query in SQL. The database only sees encrypted "gibberish," so pattern matching and sorting won't work.

---

## 6. Auto-Applying Converters (@Converter(autoApply = true))
Setting `autoApply = true` ensures that every instance of the specified Java type (e.g., `MySecretType`) is automatically processed by the converter across the entire application.

---

## 7. Converters vs User Types
Attribute Converters are standard JPA. "User Types" are Hibernate-specific and offer more power (like mapping one object to multiple columns) but lock you into the Hibernate provider.

---

## 8. Handling Null Values in Converters
JPA does NOT call the converter if the attribute value is `null`. However, it's best practice to handle `null` checks inside your converter methods to avoid `NullPointerException` during manual calls.

---

## 9. Performance Impact of Encryption
Encryption is CPU-intensive. Applying it to every column will significantly increase the latency of `SAVE` and `FIND` operations. Only encrypt truly sensitive data.

---

## 10. Key Management for Encryption
The converter needs an encryption key. Never hardcode this key. Instead, inject it from an environment variable or a Secret Manager (AWS KMS, HashiCorp Vault).

---

## 11. Common Mistakes
1. Hardcoding the encryption key in the converter class.
2. Forgetting that JPA doesn't support encryption for ID columns or Join columns.
3. Trying to use `LIKE` or `ORDER BY` on encrypted columns.

---

## 12. Quick-Fire Interview Q&A
**Q: Can a converter map one field to two columns?**  
A: No. Attribute Converters are strictly 1:1. For 1:N mapping, use `@Embeddable`.  
**Q: Is the converter called for Native Queries?**  
A: No. Native queries bypass the JPA mapping layer.
