# Scenario 100: JSON Columns in JPA (Hibernate 6) 🏆

## Overview
How do you store flexible, structured data in a relational database without creating dozens of columns? 

In modern applications, we often need to store "extra" metadata—like social links, product specifications, or feature flags—that doesn't fit a rigid schema. Historically, this required complex `AttributeConverter` logic or separate bridge tables.

**Scenario 100** demonstrates the modern **Hibernate 6** approach using native JSON mapping.

---

## 🚀 Key Features

### 1. The `@JdbcTypeCode(SqlTypes.JSON)` Annotation
This is the "Magic Sauce" of Hibernate 6. It tells the persistence provider: *"This Java Object should be treated as a JSON blob in the database."*

### 2. Zero-Boilerplate Serialization
Unlike the old days of using Jackson manual mappers or `AttributeConverter<MyObj, String>`, Hibernate now handles the conversion to and from JSON **automatically**.

---

## 🏗️ Implementation Details

### 1. The Metadata POJO (`Scenario100Metadata.java`)
A standard Java class that contains:
- `List<String> tags`
- `Map<String, String> attributes`
- `Double weight`
- `String origin`

### 2. The Entity Mapping
```java
@JdbcTypeCode(SqlTypes.JSON)
@Column(name = "item_metadata", columnDefinition = "json")
Scenario100Metadata metadata;
```
Note the `columnDefinition = "json"`. This works perfectly with H2, PostgreSQL, and MySQL.

---

## 📊 Database Visualization

This is how the **`scenario100_items`** table actually looks inside your H2, PostgreSQL, or MySQL database:

| ID | NAME | ITEM_METADATA (JSON) |
| :--- | :--- | :--- |
| **1** | TitaniumProcessor | `{"origin": "GLOBAL-WAREHOUSE", "weight": 0.25, "tags": ["NEW", "PREMIUM", "SCENARIO-100"], "attributes": {"Material": "Titanium", "Warranty": "10Y"}}` |
| **2** | QuantumProcessor | `{"origin": "GLOBAL-WAREHOUSE", "weight": 0.05, "tags": ["NEW", "PREMIUM", "SCENARIO-100"], "attributes": {"Material": "Silicon", "Cooling": "Nitrogen"}}` |

---

## 🆚 Native JSON vs. Base64/CLOB

A common question is: *"Can we just store this as a Base64-encoded string in a CLOB column?"* 

While possible, native JSON is modern and preferred:

| Feature | Base64 / CLOB | Native JSON (Hibernate 6) |
| :--- | :--- | :--- |
| **Readability** | ❌ Unreadable gibberish in DB console. | ✅ Human-readable and clear. |
| **DB-Level Search** | ❌ Cannot query inside the blob. | ✅ Can use `->>` and `@>` operators in SQL. |
| **Indexing** | ❌ Impossible to index sub-fields. | ✅ Supported (e.g., GIN indexes in Postgres). |
| **Storage Size** | 📉 33% overhead from encoding. | 📈 Efficient, compressed binary format. |
| **Boilerplate** | 🛠️ Needs custom `AttributeConverter`. | ✨ Zero-code with `@JdbcTypeCode`. |

---

## 🧪 How to Test

### 1. Create an Inventory Item
Run the following command to save an item with rich JSON metadata:
```bash
curl -X POST "http://localhost:8080/debug-application/api/scenario100/create?name=TitaniumProcessor&weight=0.25"
```

### 2. Retrieve the Item
Fetch the item to see how Hibernate deserialized the JSON back into a structured Java object:
```bash
curl -X GET "http://localhost:8080/debug-application/api/scenario100/find/1"
```

### 3. Verification
Check the response body. You should see the `metadata` object fully populated with the tags and attributes we set in the Controller.

---

## Interview Tip 💡
**Q**: *"How do you store a JSON blob in a JPA entity?"*  
**A**: *"In Spring Boot 3 / Hibernate 6, we use the `@JdbcTypeCode(SqlTypes.JSON)` annotation on the field. This allows Hibernate to handle the serialization to the database's native JSON type automatically, without needing a custom AttributeConverter."*

**Q**: *"When would you use a JSON column instead of a separate table?"*  
**A**: *"If the data is highly variable (different fields for different records) and we don't need to perform complex SQL joins or indexing on every specific sub-field, a JSON column provides much better performance and flexibility than a join-heavy normalized schema."*
