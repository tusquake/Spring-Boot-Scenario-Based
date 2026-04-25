# OS Command Injection & Process Security — Complete Interview Reference

## Table of Contents
1. [What is OS Command Injection?](#1-what-is-command-injection)
2. [How the Attack Works (Shell Metacharacters)](#2-how-it-works)
3. [Runtime.exec() vs ProcessBuilder](#3-exec-vs-processbuilder)
4. [The Role of the Shell (sh vs cmd)](#4-role-of-shell)
5. [The Classic Interview Trap: Validating with Regex Only](#5-the-classic-interview-trap-regex)
6. [Secure Implementation with ProcessBuilder](#6-secure-implementation)
7. [Input Sanitization & Allow-listing](#7-sanitization)
8. [The Principle of Least Privilege](#8-least-privilege)
9. [Restricting System Access (Java Security Manager)](#9-security-manager)
10. [Alternatives to System Calls (Native Libraries)](#10-alternatives)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is OS Command Injection?
OS command injection is a security vulnerability that allows an attacker to execute arbitrary operating system commands on the server that is running an application. This typically happens when an application passes unvalidated user input to a system shell.

---

## 2. How the Attack Works
Attackers use "shell metacharacters" like `&`, `|`, `;`, or ` ` (newline) to append their own commands to the one intended by the developer.
- **Normal Input**: `127.0.0.1` -> `ping 127.0.0.1`
- **Malicious Input**: `127.0.0.1 && rm -rf /` -> `ping 127.0.0.1 && rm -rf /`

---

## 3. Runtime.exec() vs ProcessBuilder
- **Runtime.exec(String)**: Highly dangerous. It parses the string into arguments, often using the system shell, which enables injection.
- **ProcessBuilder(List<String>)**: Much safer. It treats each element in the list as a literal argument and does not invoke a shell unless explicitly told to.

---

## 4. The Role of the Shell
Injections often happen because the developer calls `cmd.exe /c` (Windows) or `/bin/sh -c` (Linux). These shells are designed to interpret special characters. If you avoid the shell and call the executable directly (e.g., `ping.exe`), the risk is significantly reduced.

---

## 5. The Classic Interview Trap: Incomplete Validation
**The Trap**: You use a regex to strip out `;` and `&`.
**The Problem**: There are many other ways to inject commands (e.g., backticks `` ` ` ` ` ` `` in Linux, or using `\n` to start a new line).
**The Fix**: Use an **Allow-list** (e.g., only allow alphanumeric characters) rather than a Block-list.

---

## 6. Secure Implementation
Always use `ProcessBuilder` and pass arguments as separate strings.
```java
ProcessBuilder pb = new ProcessBuilder("ping", "-c", "1", host);
```
Even if `host` is `127.0.0.1 & rm -rf`, the OS will try to ping a literal host named `127.0.0.1 & rm -rf`, which will simply fail without executing the `rm` command.

---

## 7. Input Sanitization
Beyond using the right API, you should always validate that the input makes sense. If you expect an IP address, use an IP validator. If you expect a filename, ensure it doesn't contain path traversal characters (`..`).

---

## 8. Least Privilege
The application server should run as a low-privileged user (e.g., `www-data` or `nobody`). This ensures that even if an injection occurs, the attacker cannot access sensitive system files or administrative commands.

---

## 9. Java Security Manager
Although deprecated in newer versions of Java, the Security Manager was traditionally used to restrict which system commands a Java application could execute.

---

## 10. Alternatives to System Calls
Before calling an OS command, check if there is a Java library that does the same thing. For example, instead of calling `ping`, use `InetAddress.isReachable()`. Instead of `ls`, use `java.nio.file.Files`.

---

## 11. Common Mistakes
1. Using `Runtime.getRuntime().exec(userInput)`.
2. Blacklisting only a few characters like `;`.
3. Running the Java app as `root` or `Administrator`.

---

## 12. Quick-Fire Interview Q&A
**Q: Why is ProcessBuilder safer than Runtime.exec?**  
A: Because it passes arguments directly to the OS as a list, preventing the shell from interpreting special characters.  
**Q: What is the "blind" command injection?**  
A: An injection where the attacker cannot see the output of the command, so they use time-based techniques (like `sleep 10`) to confirm the injection worked.
