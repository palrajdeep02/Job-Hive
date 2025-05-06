package com.jobhive.service;

import com.jobhive.entity.Resume;
import com.jobhive.entity.User;
import com.jobhive.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ResumeService {
    @Autowired
    private ResumeRepository resumeRepository;

    private final Path uploadDir = Paths.get("uploads/resumes");

    public ResumeService() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public Resume uploadResume(MultipartFile file, User user) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        Resume resume = new Resume();
        resume.setFileName(file.getOriginalFilename());
        resume.setFileType(file.getContentType());
        resume.setFilePath(filePath.toString());
        resume.setUploadedDate(LocalDateTime.now());
        resume.setUser(user);

        return resumeRepository.save(resume);
    }

    public List<Resume> getResumesByUser(User user) {
        return resumeRepository.findByUser(user);
    }

    public Resume getLatestResume(User user) {
        return resumeRepository.findFirstByUserOrderByUploadedDateDesc(user);
    }

    public void deleteResume(Long resumeId) throws IOException {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        
        Files.deleteIfExists(Paths.get(resume.getFilePath()));
        resumeRepository.delete(resume);
    }
} 
