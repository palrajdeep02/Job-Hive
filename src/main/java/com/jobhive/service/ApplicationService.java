package com.jobhive.service;

import com.jobhive.entity.Application;
import com.jobhive.entity.Job;
import com.jobhive.entity.User;
import com.jobhive.repository.ApplicationRepository;
import com.jobhive.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    @Transactional
    public Application createApplication(User user, Long jobId, String coverLetter, String resumePath) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (applicationRepository.existsByUserAndJob(user, job)) {
            throw new RuntimeException("You have already applied for this job");
        }

        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setCoverLetter(coverLetter);
        application.setResumePath(resumePath);

        return applicationRepository.save(application);
    }

    public List<Application> getApplicationsByUser(User user) {
        return applicationRepository.findByUser(user);
    }

    public List<Application> getApplicationsByJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return applicationRepository.findByJob(job);
    }

    public boolean existsByUserAndJob(User user, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return applicationRepository.existsByUserAndJob(user, job);
    }

    @Transactional
    public Application updateApplicationStatus(Long applicationId, String status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(status);
        return applicationRepository.save(application);
    }
} 
