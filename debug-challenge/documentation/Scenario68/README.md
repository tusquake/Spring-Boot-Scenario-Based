# Scenario 68: File Upload Security

Allowing users to upload files is one of the most dangerous features an application can provide. If not handled correctly, it leads to full server compromise.

---

## 🛡️ The Big 4 Hazards

### 1. Path Traversal
**Attack**: An attacker sends a filename like `../../config/application.properties`.
**Impact**: The attacker overwrites critical system configurations.
**Fix**: Use `StringUtils.cleanPath()` and verify the path doesn't contain `..`.

### 2. Remote Code Execution (RCE)
**Attack**: Uploading a `.jsp` or `.php` file.
**Impact**: The attacker executes commands on your server by accessing the file via URL.
**Fix**: Strict **Extension Whitelisting** (only allow `jpg`, `pdf`, etc.) and store files **outside the public web root**.

### 3. Magic Number Spoofing
**Attack**: Renaming `virus.exe` to `image.jpg`.
**Fix**: Use libraries like **Apache Tika** to verify the actual file content (Magic Bytes) instead of relying on the extension.

### 4. Zip Bomb (DoS)
**Attack**: A tiny `.zip` file that expands into petabytes of data.
**Fix**: Check the uncompressed size before extraction or use **Multipart File Size Limits** in `application.properties`.

---

## 🚀 implementation in this Scenario
The [Scenario68Controller](file:///c:\Users\tushar.seth\Desktop\Scenario%20Based\debug-challenge\src\main\java\com\interview\debug\controller\Scenario68Controller.java) demonstrates:

1. **Vulnerable Endpoint**: Replaces existing files using raw input.
2. **Secure Endpoint**:
   - Cleans the path.
   - Blocks invalid extensions.
   - Validates MIME type.

### How to Test:

1. **Successful Upload**:
   `curl -X POST -F "file=@test.png" http://localhost:8080/api/scenario68/upload/secure`

2. **Attack (Path Traversal)**:
   Create a file named `.._.._test.png` (or similar) and try to upload it.
   
3. **Attack (Extension Block)**:
   `curl -X POST -F "file=@malicious.exe" http://localhost:8080/api/scenario68/upload/secure`
   - *Result*: Should return `400 Bad Request`.

---

## Interview Questions
- **Q: Where should you store uploaded files?**
  - **A**: Never in the project root or static resources. Use an external storage service like **AWS S3** or a dedicated NAS with restricted permissions.
- **Q: How can you prevent an attacker from finding their uploaded file?**
  - **A**: **Randomize the filename**. Instead of `myphoto.jpg`, store it as `uuid-2342-fd23.jpg` and keep the mapping in a database.
- **Q: What is the most effective way to stop malicious scripts from running?**
  - **A**: Serve uploaded files from a **different domain** (e.g., `files.example.com`). This prevents the script from accessing the main site's cookies (Same-Origin Policy).
