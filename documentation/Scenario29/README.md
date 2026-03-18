# Scenario 29: Optimized File Upload/Download & SSE

Demonstrates high-performance file handling with real-time progress tracking for the user.

## Concept
1. **Streaming Upload**: Using a buffer to move data from the request to the disk, ensuring we don't load a huge file into memory (RAM).
2. **SSE (Server-Sent Events)**: Pushing progress percentages (10%, 20%...) to the browser so the user doesn't see a "stuck" page.
3. **FileSystemResource**: Returning files in a way that Spring streams them directly to the client.

## Implementation Details
- **Upload**: Uses `InputStream` with an 8KB buffer and an `SseEmitter` for progress.
- **Download**: Returns `ResponseEntity<Resource>` using `FileSystemResource`.

### SSE Logic:
```java
SseEmitter emitter = emitters.get(uploadId);
int percentage = (int) ((bytesReadSoFar * 100) / totalBytes);
emitter.send(SseEmitter.event().name("progress").data(percentage));
```

## Verification Results
- **Test**: Open `/api/scenario29/status/UPLOAD_001` in one tab. Upload a file in another tab with `uploadId=UPLOAD_001`.
- **Result**: You will see the first tab automatically print percentages as the upload happens!

## Interview Theory: Large File Handling
- **Memory Management**: Never use `file.getBytes()` for large files; it will trigger `OutOfMemoryError`. Always use `InputStream`.
- **Multipart Config**: Mention `spring.servlet.multipart.max-file-size` to limit input size.
- **Cloud Storage**: In real apps, don't store files on the server disk; use **AWS S3** or **Azure Blob Storage** instead.
