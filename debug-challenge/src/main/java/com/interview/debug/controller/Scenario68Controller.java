package com.interview.debug.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/scenario68")
public class Scenario68Controller {

    private final String UPLOAD_DIR = "uploads/";

    public Scenario68Controller() throws IOException {
        Files.createDirectories(Paths.get(UPLOAD_DIR));
    }

    /**
     * VULNERABLE: Uses original filename directly.
     * Risk: Path Traversal (e.g., filename="../../etc/passwd")
     */
    @PostMapping("/upload/vulnerable")
    public String vulnerableUpload(@RequestParam("file") MultipartFile file) throws IOException {
        Path targetPath = Paths.get(UPLOAD_DIR).resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return "VULNERABLE: File uploaded to " + targetPath;
    }

    /**
     * SECURE: Sanitizes filename, checks extensions, and validates content.
     */
    @PostMapping("/upload/secure")
    public ResponseEntity<String> secureUpload(@RequestParam("file") MultipartFile file) throws IOException {
        // 1. Sanitize Filename (Prevent Path Traversal)
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.contains("..")) {
            return ResponseEntity.badRequest().body("THREAT DETECTED: Path traversal attempt!");
        }

        // 2. Extension Whitelisting
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "pdf");
        String extension = StringUtils.getFilenameExtension(fileName);
        if (extension == null || !allowedExtensions.contains(extension.toLowerCase())) {
            return ResponseEntity.badRequest().body("ERROR: Invalid file extension. Only images and PDFs allowed.");
        }

        // 3. (Optional but Recommended) Content-Type Check
        String contentType = file.getContentType();
        if (contentType != null && !contentType.startsWith("image/") && !contentType.equals("application/pdf")) {
            return ResponseEntity.badRequest().body("ERROR: Malicious content-type detected.");
        }

        // 4. Save to target location
        Path targetPath = Paths.get(UPLOAD_DIR).resolve(fileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return ResponseEntity.ok("SECURE: File " + fileName + " uploaded successfully.");
    }
}
