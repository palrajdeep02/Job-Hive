package com.jobhive.controller;

import com.jobhive.entity.Job;
import com.jobhive.entity.User;
import com.jobhive.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job, @AuthenticationPrincipal User user) {
        Job createdJob = jobService.createJob(job, user);
        return ResponseEntity.ok(createdJob);
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/my-jobs")
    public ResponseEntity<List<Job>> getMyJobs(@AuthenticationPrincipal User user) {
        List<Job> jobs = jobService.getJobsByUser(user);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job job, @AuthenticationPrincipal User user) {
        Job updatedJob = jobService.updateJob(id, job, user);
        return ResponseEntity.ok(updatedJob);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id, @AuthenticationPrincipal User user) {
        jobService.deleteJob(id, user);
        return ResponseEntity.ok().build();
    }
} 