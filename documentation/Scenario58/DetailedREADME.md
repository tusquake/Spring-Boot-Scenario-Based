# Password Encoding & Security — Complete Interview Reference

## Table of Contents
1. [Introduction to Password Hashing](#1-introduction)
2. [Why Not MD5 or SHA-256?](#2-why-not-md5)
3. [The Role of Salt and Pepper](#3-salt-and-pepper)
4. [Spring's DelegatingPasswordEncoder](#4-delegating-encoder)
5. [The Classic Interview Trap: Re-hashing Passwords](#5-the-classic-interview-trap-rehashing)
6. [BCrypt vs Argon2](#6-bcrypt-vs-argon2)
7. [Adaptive Hashing (Work Factors)](#7-adaptive-hashing)
8. [Verifying Passwords (matches method)](#8-verifying-passwords)
9. [Migration of Legacy Passwords](#9-legacy-migration)
10. [Hardware-Resistant Hashing (Argon2id)](#10-hardware-resistant)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction to Password Hashing
You should NEVER store passwords in plain text. Password hashing is a one-way mathematical function that transforms a password into a "fingerprint" (hash). It is impossible to reverse the hash to get the original password.

---

## 2. Why Not MD5 or SHA-256?
MD5 and SHA-256 are **too fast**. A modern GPU can try billions of MD5 hashes per second, making them vulnerable to "Brute Force" or "Rainbow Table" attacks.

---

## 3. The Role of Salt and Pepper
- **Salt**: A random string added to the password *before* hashing. It ensures that two users with the same password have different hashes.
- **Pepper**: A secret key stored in the application code (not the DB) that is added to the password. Even if the DB is stolen, the attacker cannot crack the hashes without the pepper.

---

## 4. DelegatingPasswordEncoder
Spring Security's modern way of handling passwords. It stores the hash with a prefix like `{bcrypt}` or `{argon2}`. This allows the application to support multiple algorithms simultaneously and upgrade them over time.

---

## 5. The Classic Interview Trap: Re-hashing
**The Trap**: You implement a "Login" service. You manually hash the incoming password and compare the string with the DB. 
`if (myHasher.hash(input).equals(dbHash)) ...`
**The Problem**: BCrypt and Argon2 generate a **NEW random salt** every time they hash. The same password will NEVER produce the same hash string twice.
**The Fix**: Use the `passwordEncoder.matches(rawPassword, encodedPassword)` method.

---

## 6. BCrypt vs Argon2
- **BCrypt**: The industry standard for over 20 years. CPU-intensive.
- **Argon2**: The winner of the Password Hashing Competition (2015). Both CPU and Memory-intensive, making it even harder to crack with specialized hardware like ASICs.

---

## 7. Adaptive Hashing
Algorithms like BCrypt have a "Work Factor" (or Cost). As computers get faster, you can increase the work factor to make hashing slower, keeping it secure against future hardware.

---

## 8. Verifying Passwords
The `matches` method extracts the salt from the encoded password string, applies it to the raw input password, and then compares the resulting hash.

---

## 9. Migration of Legacy Passwords
With `DelegatingPasswordEncoder`, you can check if a user is using an old algorithm (like MD5) during login. If they are, you can re-hash their password with BCrypt and update the DB transparently.

---

## 10. Hardware-Resistant Hashing
Argon2id is the recommended variant of Argon2. It provides the best protection against both side-channel attacks and GPU/ASIC brute forcing.

---

## 11. Common Mistakes
1. Using `MD5` or `SHA-1` in 2024.
2. Forgetting to clear the password from memory (using `char[]` instead of `String` for raw passwords is a best practice).
3. Not using a unique salt per user.

---

## 12. Quick-Fire Interview Q&A
**Q: Is hashing the same as encryption?**  
A: No. Encryption is two-way (can be decrypted); Hashing is one-way.  
**Q: What does the {noop} prefix mean in Spring?**  
A: It tells Spring to treat the password as plain text (No Operation). Use only for testing!
