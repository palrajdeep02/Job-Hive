package com.jobhive.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobhive.entity.Application;
import com.jobhive.entity.ApplicationStatus;
import com.jobhive.entity.Company;
import com.jobhive.entity.Job;
import com.jobhive.entity.User;
import com.jobhive.entity.UserRole;
import com.jobhive.repository.ApplicationRepository;
import com.jobhive.repository.CompanyRepository;
import com.jobhive.repository.JobRepository;
import com.jobhive.repository.UserRepository;
import com.jobhive.service.ApplicationService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ApplicationService applicationService;

    // User Management
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch users: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, String> userData) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            if (userData.containsKey("name")) {
                user.setName(userData.get("name"));
            }
            if (userData.containsKey("email")) {
                user.setEmail(userData.get("email"));
            }
            if (userData.containsKey("role")) {
                user.setRole(UserRole.valueOf(userData.get("role")));
            }
            if (userData.containsKey("enabled")) {
                user.setEnabled(Boolean.parseBoolean(userData.get("enabled")));
            }
            
            userRepository.save(user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update user: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            userRepository.delete(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete user: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Job Management
    @GetMapping("/jobs")
    public ResponseEntity<?> getAllJobs() {
        try {
            List<Job> jobs = jobRepository.findAll().stream()
                    .filter(job -> job.getCompany() != null)
                    .toList();
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch jobs: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PutMapping("/jobs/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody Job job) {
        try {
            Job existingJob = jobRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Job not found"));
            
            existingJob.setTitle(job.getTitle());
            existingJob.setDescription(job.getDescription());
            existingJob.setLocation(job.getLocation());
            existingJob.setType(job.getType());
            existingJob.setExperienceLevel(job.getExperienceLevel());
            existingJob.setActive(job.isActive());
            
            jobRepository.save(existingJob);
            return ResponseEntity.ok(existingJob);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update job: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        try {
            Job job = jobRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Job not found"));
            jobRepository.delete(job);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete job: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Application Management
    @GetMapping("/applications")
    public ResponseEntity<?> getAllApplications() {
        try {
            List<Application> applications = applicationRepository.findAll();
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch applications: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PutMapping("/applications/{id}/status")
    public ResponseEntity<Application> updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            Application updatedApplication = applicationService.updateApplicationStatus(id, status);
            return ResponseEntity.ok(updatedApplication);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update application status: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Company Management
    @GetMapping("/companies")
    public ResponseEntity<?> getAllCompanies() {
        try {
            List<Company> companies = companyRepository.findAll();
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch companies: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    @GetMapping("/companies/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        try {
            Company company = companyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            return ResponseEntity.ok(company);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch company: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/companies")
    public ResponseEntity<?> createCompany(@RequestBody Company company) {
        try {
            // Set default values if not provided
            if (company.getDescription() == null) {
                company.setDescription("No description provided");
            }
            if (company.getLocation() == null) {
                company.setLocation("Not specified");
            }
            if (company.getWebsite() == null) {
                company.setWebsite("Not specified");
            }
            if (company.getContactEmail() == null) {
                company.setContactEmail("Not specified");
            }
            if (company.getContactPhone() == null) {
                company.setContactPhone("Not specified");
            }
            
            Company savedCompany = companyRepository.save(company);
            return ResponseEntity.ok(savedCompany);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create company: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PutMapping("/companies/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody Map<String, Object> companyData) {
        try {
            Company company = companyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            
            if (companyData.containsKey("name")) {
                company.setName((String) companyData.get("name"));
            }
            if (companyData.containsKey("industry")) {
                company.setIndustry((String) companyData.get("industry"));
            }
            if (companyData.containsKey("size")) {
                company.setSize((String) companyData.get("size"));
            }
            if (companyData.containsKey("active")) {
                company.setActive((Boolean) companyData.get("active"));
            }
            if (companyData.containsKey("description")) {
                company.setDescription((String) companyData.get("description"));
            }
            if (companyData.containsKey("location")) {
                company.setLocation((String) companyData.get("location"));
            }
            if (companyData.containsKey("website")) {
                company.setWebsite((String) companyData.get("website"));
            }
            if (companyData.containsKey("contactEmail")) {
                company.setContactEmail((String) companyData.get("contactEmail"));
            }
            if (companyData.containsKey("contactPhone")) {
                company.setContactPhone((String) companyData.get("contactPhone"));
            }
            if (companyData.containsKey("logoUrl")) {
                company.setLogoUrl((String) companyData.get("logoUrl"));
            }
            
            companyRepository.save(company);
            return ResponseEntity.ok(company);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update company: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        try {
            Company company = companyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            companyRepository.delete(company);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete company: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
} 
