package com.jobhive.controller;

import com.jobhive.entity.Application;
import com.jobhive.entity.User;
import com.jobhive.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<?> createApplication(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, String> request) {
        try {
            String jobIdStr = request.get("jobId");
            if (jobIdStr == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Job ID is required"));
            }

            Long jobId = Long.parseLong(jobIdStr);
            Application application = applicationService.createApplication(user, jobId, "", "");
            return ResponseEntity.ok(application);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid job ID format"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my-applications")
    public ResponseEntity<List<Application>> getMyApplications(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(applicationService.getApplicationsByUser(user));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsByJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsByJob(jobId));
    }

    @GetMapping("/check/{jobId}")
    public ResponseEntity<Boolean> hasApplied(@PathVariable Long jobId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(applicationService.existsByUserAndJob(user, jobId));
    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<Void> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam String status) {
        applicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.ok().build();
    }
} 
