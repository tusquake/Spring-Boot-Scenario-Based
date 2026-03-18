# Scenario 64: Command Injection Prevention

Securing your server against attackers who try to execute arbitrary OS commands via your Java code.

## Concept
Command injection occurs when an application executes a system command (like `ping`, `ls`, or `dir`) using unsanitized user input. An attacker can use shell metacharacters (`;`, `&&`, `|`) to Chain their own malicious commands.

## Implementation Details
We demonstrated the difference between an unsafe "String concat" approach and the secure "List-based" approach.

### 1. The Vulnerable Way (Concatenation):
```java
String command = "cmd.exe /c ping -n 1 " + host;
Process process = Runtime.getRuntime().exec(command);
```
**Risk**: If host is `127.0.0.1 && echo HACKED`, the server executes BOTH.

### 2. The Secure Way (ProcessBuilder + Validation):
- **Validation**: Strict Regex check (`^[a-zA-Z0-9.-]+$`).
- **ProcessBuilder**: Passing parameters as a `List<String>`.

```java
List<String> command = List.of("ping", "-n", "1", host);
ProcessBuilder pb = new ProcessBuilder(command);
```

## Verification Results
1. **Attack Attempt (Vulnerable)**: Host set to `127.0.0.1 && echo INJECTED`.
   - **Result**: Command succeeds and the output contains "INJECTED".
2. **Attack Attempt (Secure)**: Same input.
   - **Result**: Denied by Regex validator.
3. **Escaped Attack**: Even if bypass validation, `ProcessBuilder` treats the entire input string as a single argument to ping, preventing it from being executed as a command.

## Interview Theory: Best Practices
- **Whitelist Validation**: Only allow specific characters (Alphanumeric). Never try to "filter out" bad characters (Blacklist), as attackers always find new ones (like `%0a` for newline).
- **Avoid Shell Interaction**: Use `ProcessBuilder` with a list. This skips the OS shell (cmd.exe or /bin/sh) and sends arguments directly to the binary.
- **Library Alternatives**: Instead of calling `ping` from the OS, use `java.net.InetAddress.isReachable()`. Always prefer a Java library over a system command.
