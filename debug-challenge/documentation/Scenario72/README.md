# Scenario 72: Insecure Deserialization Protection

## Overview
**Insecure Deserialization** occurs when untrusted data is used to abuse the logic of an application, inflict a Denial of Service (DoS) attack, or even execute arbitrary code (RCE) upon being deserialized.

Java's native serialization (`java.io.ObjectInputStream.readObject()`) is particularly dangerous because it doesn't just restore data; it can trigger code execution through "Gadget Chains" (sequences of existing class methods like `readObject()`, `toString()`, or `equals()` that are called automatically during deserialization).

## The Vulnerability
If an application deserializes a user-provided byte stream without validation, an attacker can send a serialized stream representing a malicious object graph.

```java
// VULNERABLE CODE
ObjectInputStream ois = new ObjectInputStream(inputStream);
Object obj = ois.readObject(); // <--- RCE happens here
```

## The Defense: Look-ahead Deserialization
The best defense is to **avoid Java serialization** entirely (use JSON, Protobuf, etc.). If you MUST use it, implement **Look-ahead Deserialization**.

In this scenario, we use `SafeObjectInputStream` which overrides `resolveClass()` to check an allowlist of permitted classes *before* they are instantiated.

```java
@Override
protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
    if (!allowedClasses.contains(desc.getName())) {
        throw new SecurityException("Unauthorized Class!");
    }
    return super.resolveClass(desc);
}
```

## Interview Tips 💡
- **What is a Gadget Chain?** It's a chain of methods starting from the sink (like `readObject`) to a dangerous operation (like `Runtime.exec()`), using classes already present in the classpath (e.g., CommonsCollections, Spring, etc.).
- **Why is Jackson default typing dangerous?** Because it allows the JSON to specify the class to instantiate, similar to Java serialization's `resolveClass` behavior.
- **How does JEP 290 help?** Java 9+ introduced a global serialization filter (`jdk.serialFilter`) that provides built-in protection.

## Testing the Scenario
1. **Generate a Payload**: `GET /api/scenario72/generate-payload?username=Tushar`
2. **Test Vulnerable**: `POST /api/scenario72/vulnerable/deserialize` with the Base64 payload. It should work.
3. **Test Secure**: `POST /api/scenario72/secure/deserialize` with the Base64 payload. It should also work because `UserProfile` is on the allowlist.
4. **Test Blocked**: If you send a payload for an unauthorized class (e.g., a serialized `java.util.HashMap` if not in allowlist), the secure endpoint will block it.
