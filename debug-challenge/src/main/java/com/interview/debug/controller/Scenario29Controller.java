package com.interview.debug.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/scenario29")
public class Scenario29Controller {

    private static final Logger logger = LoggerFactory.getLogger(Scenario29Controller.class);
    private static final String UPLOAD_DIR = "uploads";
    
    // Registry to keep track of clients listening for specific upload progress
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public Scenario29Controller() throws IOException {
        // Ensure upload directory exists
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    /**
     * SSE Endpoint: Clients connect here to listen for progress on a specific upload ID.
     */
    @GetMapping("/status/{uploadId}")
    public SseEmitter streamStatus(@PathVariable String uploadId) {
        SseEmitter emitter = new SseEmitter(600000L); // 10 minute timeout
        emitters.put(uploadId, emitter);

        emitter.onCompletion(() -> emitters.remove(uploadId));
        emitter.onTimeout(() -> emitters.remove(uploadId));
        emitter.onError((e) -> emitters.remove(uploadId));

        logger.info("New progress listener connected for uploadId: {}", uploadId);
        return emitter;
    }

    /**
     * Optimized Upload: Uses InputStream directly and progress tracking via SSE.
     */
    @PostMapping("/upload")
    public Map<String, Object> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "uploadId", required = false) String uploadId) throws IOException, InterruptedException {
        
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name is missing");
        }

        Path targetLocation = Paths.get(UPLOAD_DIR).resolve(fileName);
        logger.info("Optimized upload starting for: {} (uploadId: {})", fileName, uploadId);
        
        SseEmitter emitter = (uploadId != null) ? emitters.get(uploadId) : null;

        // Streaming upload: InputStream -> Filesystem with Progress Tracking
        try (var inputStream = file.getInputStream();
             var outputStream = Files.newOutputStream(targetLocation)) {
            
            byte[] buffer = new byte[8192]; // 8KB buffer
            long totalBytes = file.getSize();
            long bytesReadSoFar = 0;
            int bytesRead;
            int lastPercentage = -1;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                bytesReadSoFar += bytesRead;
                
                // Simulate slower network for visibility
                Thread.sleep(50); 

                int percentage = (int) ((bytesReadSoFar * 100) / totalBytes);
                
                if (percentage > lastPercentage) {
                    lastPercentage = percentage;
                    
                    // 1. Log to console
                    if (percentage % 10 == 0) {
                        logger.info("Upload progress for {}: {}%", fileName, percentage);
                    }

                    // 2. Push to SSE Emitter
                    if (emitter != null) {
                        try {
                            emitter.send(SseEmitter.event().name("progress").data(percentage));
                        } catch (Exception e) {
                            logger.warn("Failed to send SSE update for {}: {}", uploadId, e.getMessage());
                        }
                    }
                }
            }
        }

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("complete").data("Upload finished"));
                emitter.complete();
            } catch (Exception e) { /* already handled */ }
        }

        logger.info("Optimized upload complete: {}", targetLocation.toAbsolutePath());

        return Map.of(
            "status", "success",
            "uploadId", uploadId != null ? uploadId : "N/A",
            "fileName", fileName
        );
    }

    /**
     * Optimized Download: Returns a FileSystemResource.
     * Spring handles this by streaming the file content to the response body in small chunks.
     * The JVM heap usage remains constant regardless of the file size.
     */
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
        
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(filePath.toFile());
        
        logger.info("Optimized download starting for: {}", fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
