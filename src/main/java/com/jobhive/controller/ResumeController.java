package com.jobhive.controller;

import com.jobhive.entity.Resume;
import com.jobhive.entity.User;
import com.jobhive.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {
    @Autowired
    private ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<Resume> uploadResume(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user) throws IOException {
        return ResponseEntity.ok(resumeService.uploadResume(file, user));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Resume>> getMyResumes(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(resumeService.getResumesByUser(user));
    }

    @GetMapping("/latest")
    public ResponseEntity<Resume> getLatestResume(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(resumeService.getLatestResume(user));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long id) throws IOException {
        Resume resume = resumeService.getLatestResume(null); // TODO: Add proper user check
        Path filePath = Paths.get(resume.getFilePath());
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resume.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) throws IOException {
        resumeService.deleteResume(id);
        return ResponseEntity.ok().build();
    }
} 
