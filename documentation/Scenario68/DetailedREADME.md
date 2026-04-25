# Secure File Uploads — Complete Interview Reference

## Table of Contents
1. [Introduction to File Upload Security](#1-introduction)
2. [What is Path Traversal? (.. /)](#2-path-traversal)
3. [Filename Sanitization Strategies](#3-sanitization)
4. [Extension Whitelisting vs Blacklisting](#4-extension-whitelisting)
5. [The Classic Interview Trap: The Magic Bytes Check](#5-the-classic-interview-trap-magic-bytes)
6. [Validating Content-Type Headers](#6-content-type)
7. [Restricting File Size and Counts](#7-file-size)
8. [Virus Scanning Integration](#8-virus-scanning)
9. [Storing Files: Disk vs Database vs Cloud](#9-storing-files)
10. [Preventing Script Execution (No-Execute)](#10-preventing-execution)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction
File uploading is one of the most dangerous features an application can have. If not secured, it can lead to Remote Code Execution (RCE), Data Exfiltration, or a full server takeover.

---

## 2. What is Path Traversal?
Path traversal (or directory traversal) is an attack where the attacker provides a filename like `../../../../etc/passwd`. If the server blindly resolves this path, it might save the upload over a sensitive system file or allow the attacker to read files outside the intended upload directory.

---

## 3. Filename Sanitization
Never trust the `originalFilename`.
- **Strategy A**: Use `StringUtils.cleanPath()` to remove `..` and `./`.
- **Strategy B**: Generate a completely new, random filename (UUID) and store the original name in the database. This is the **most secure** approach.

---

## 4. Extension Whitelisting
Always use a **whitelist** of allowed extensions (e.g., `jpg`, `png`, `pdf`). Never use a blacklist (e.g., "don't allow `.exe` or `.sh`") because an attacker will always find a new extension you forgot (like `.php5` or `.phtml`).

---

## 5. The Classic Interview Trap: Magic Bytes
**The Question**: *"Is checking the file extension enough to verify it's an image?"*
**The Answer**: **NO**. An attacker can rename a malicious script to `virus.jpg`. The server will see the `.jpg` extension and accept it. To be truly secure, you must check the **Magic Bytes** (file signatures) of the file's content to verify it's actually an image.

---

## 6. Content-Type Validation
The `Content-Type` header (e.g., `image/jpeg`) is provided by the client and is easily spoofed. You should check it, but never rely on it as your primary security measure.

---

## 7. File Size Restricting
To prevent Denial of Service (DoS) attacks, always set a maximum file size in your configuration:
`spring.servlet.multipart.max-file-size=5MB`

---

## 8. Virus Scanning
For production apps, uploaded files should be scanned by an antivirus (like ClamAV) before they are made accessible to other users.

---

## 9. Storing Files
- **Filesystem**: Fast, but hard to scale across multiple servers.
- **Database (BLOB)**: Easy to backup, but makes the database huge and slow.
- **Cloud Storage (S3/GCS)**: The industry standard. Scalable, secure, and offloads the storage burden from your servers.

---

## 10. Preventing Execution
The directory where you store uploaded files should be configured to **NOT** execute any code. In Nginx or Apache, disable PHP/Python/Bash execution for the `/uploads` path.

---

## 11. Common Mistakes
1. Trusting the client's filename.
2. Saving files in a directory that is served directly by the web server with execution permissions.
3. Not checking for "Zip Slips" (malicious zip files that contain path traversal entries).

---

## 12. Quick-Fire Interview Q&A
**Q: How do you handle duplicate filenames?**  
A: Append a timestamp or UUID to the filename, or store files in subdirectories based on the upload date.  
**Q: What is a "Zip Bomb"?**  
A: A small zip file that expands to an enormous size (terabytes), designed to crash the server by filling up the disk.
