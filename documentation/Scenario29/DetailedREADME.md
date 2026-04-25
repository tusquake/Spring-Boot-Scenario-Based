# Streaming File Upload, Download & SSE — Complete Interview Reference

## Table of Contents
1. [Introduction to File Streaming](#1-introduction)
2. [MultipartFile vs InputStream](#2-multipartfile-vs-inputstream)
3. [Configuring File Size Limits](#3-size-limits)
4. [Streaming Large Downloads (FileSystemResource)](#4-streaming-downloads)
5. [The Classic Interview Trap: OutOfMemory during Uploads](#5-the-classic-interview-trap-oom)
6. [Real-time Progress Tracking with SSE](#6-progress-tracking)
7. [Handling Timeouts in SSE (SseEmitter)](#7-sse-timeouts)
8. [Security: Sanitizing Filenames](#8-security)
9. [Multipart Request Configuration](#9-configuration)
10. [Performance: Buffer Sizes and OS Copy](#10-performance)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Introduction to File Streaming
When dealing with large files (hundreds of MBs or GBs), you cannot load the entire file into the JVM's memory (Heap). Streaming allows you to process the file in small chunks, keeping the memory footprint low and constant.

---

## 2. MultipartFile vs InputStream
- **MultipartFile**: Spring's high-level abstraction. It often buffers the file to disk temporarily.
- **InputStream**: The raw stream from the HTTP request. Accessing this directly allows for the most efficient processing.

---

## 3. Configuring File Size Limits
Spring Boot has default limits (usually 1MB per file, 10MB per request). You must increase these for large uploads:
```properties
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
```

---

## 4. Streaming Large Downloads
Instead of returning a `byte[]`, return a `FileSystemResource` or use `StreamingResponseBody`. Spring will then stream the file directly from the disk to the network socket in small chunks.

---

## 5. The Classic Interview Trap: OutOfMemory during Uploads
**The Trap**: You use `file.getBytes()` to process an upload. 
**The Problem**: If the file is 1GB and your heap is 512MB, the app will crash with `java.lang.OutOfMemoryError: Java heap space`.
**The Fix**: Use `file.getInputStream()` and read into a small buffer (e.g., 8KB) in a loop.

---

## 6. Real-time Progress Tracking with SSE
**Server-Sent Events (SSE)** is a standard allowing servers to push data to web pages over HTTP. It is perfect for showing an "Upload Progress Bar" to the user as the server receives chunks of the file.

---

## 7. Handling Timeouts in SSE
`SseEmitter` has a timeout. If the upload takes longer than the timeout, the connection will close. You must handle the `onTimeout` and `onCompletion` callbacks to clean up resources.

---

## 8. Security: Sanitizing Filenames
**CRITICAL**: Never trust the filename provided by the client (`file.getOriginalFilename()`). An attacker could send a name like `../../etc/passwd` to try and overwrite system files. Always sanitize or rename the file.

---

## 9. Multipart Request Configuration
Spring uses `StandardServletMultipartResolver` by default. You can configure the `location` where temporary files are stored during upload.

---

## 10. Performance: Buffer Sizes and OS Copy
For maximum performance, use `Files.copy(inputStream, targetLocation)` which can sometimes use OS-level optimizations (like `sendfile`) to move data between streams.

---

## 11. Common Mistakes
1. Loading the whole file into a `byte[]` array.
2. Not sanitizing filenames (Path Traversal vulnerability).
3. Not configuring a long enough timeout for SSE progress tracking.

---

## 12. Quick-Fire Interview Q&A
**Q: What is the difference between SSE and WebSockets?**  
A: SSE is one-way (Server -> Client) and uses standard HTTP. WebSockets are two-way and use a custom protocol.  
**Q: How do you handle a file upload that is larger than the available RAM?**  
A: By using streaming (processing small chunks of the `InputStream` at a time).
