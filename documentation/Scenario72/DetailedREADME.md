# Insecure Deserialization — Complete Interview Reference

## Table of Contents
1. [What is Deserialization?](#1-what-is-deserialization)
2. [The Danger: How the Attack Works](#2-how-the-attack-works)
3. [Gadget Chains: From Deserialization to RCE](#3-gadget-chains)
4. [Java Serialization vs JSON/XML](#4-java-serialization-vs-json)
5. [The Classic Interview Trap: readObject() Exploitation](#5-the-classic-interview-trap-readobject)
6. [Look-Ahead Deserialization (The Defense)](#6-look-ahead-deserialization)
7. [Using ObjectInputFilter (Java 9+)](#7-objectinputfilter)
8. [Whitelist vs Blacklist in Deserialization](#8-whitelist-vs-blacklist)
9. [Safe Alternatives (Protobuf, JSON)](#9-safe-alternatives)
10. [Impact of Insecure Deserialization](#10-impact)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Deserialization?
Serialization is the process of converting an object into a byte stream (for storage or transmission). **Deserialization** is the reverse process: turning that byte stream back into a live object in the JVM memory.

---

## 2. How the Attack Works
Insecure deserialization occurs when an application deserializes untrusted data provided by a user. An attacker can craft a malicious byte stream that, when deserialized, executes arbitrary code or performs unauthorized actions.

---

## 3. Gadget Chains
A "Gadget" is a class that exists in the application's classpath (e.g., in a library like Commons-Collections) and has a method that can be used maliciously. A **Gadget Chain** is a sequence of gadgets that, when triggered by the deserialization process, leads to **Remote Code Execution (RCE)**.

---

## 4. Java Serialization vs JSON
- **Java Serialization**: Extremely powerful (can reconstruct complex logic) but inherently dangerous because it allows the stream to specify the class to be created.
- **JSON/XML**: Safer because they typically only store data fields, not the full object structure and logic.

---

## 5. The Classic Interview Trap: readObject()
**The Trap**: You think your code is safe because you don't call any malicious methods after deserialization.
**The Problem**: The attack happens **DURING** the call to `ois.readObject()`. Many classes have custom `readObject()` methods that execute logic automatically upon being recreated. An attacker targets these methods.

---

## 6. Look-Ahead Deserialization
This defense technique involves inspecting the class type in the byte stream **before** the object is actually created. If the class is not on your "Allow-list", you abort the process.

---

## 7. ObjectInputFilter
Introduced in Java 9 (and backported to 8), `ObjectInputFilter` is the official way to secure Java deserialization. It allows you to define a filter that checks class names, array sizes, and graph depth.

---

## 8. Whitelist vs Blacklist
- **Blacklist**: Trying to block known bad classes (e.g., `org.apache.commons.collections.functors.*`). This is doomed to fail as new gadgets are discovered every day.
- **Whitelist**: Only allowing specific, trusted DTO classes. This is the **only secure approach**.

---

## 9. Safe Alternatives
Whenever possible, avoid Java's native serialization (`Serializable`). Use data-only formats like:
- **JSON** (Jackson/Gson)
- **Protocol Buffers** (Protobuf)
- **Avro**

---

## 10. Impact
The impact is usually **CRITICAL**. Insecure deserialization is often the root cause of the most devastating data breaches because it grants attackers full control over the server.

---

## 11. Common Mistakes
1. Deserializing data from cookies or hidden form fields.
2. Assuming that using a `SerialVersionUID` provides security (it's only for version compatibility).
3. Not realizing that many third-party libraries (like old versions of JMX or RMI) use native serialization under the hood.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I secure native Java deserialization?**  
A: Yes, by using `ObjectInputFilter` with a strict whitelist of allowed classes.  
**Q: What is the most famous library associated with deserialization attacks?**  
A: Apache Commons-Collections (specifically the `InvokerTransformer` gadget).
