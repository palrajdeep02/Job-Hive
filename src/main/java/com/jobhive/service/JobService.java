package com.jobhive.service;

import com.jobhive.entity.Company;
import com.jobhive.entity.Job;
import com.jobhive.entity.User;
import com.jobhive.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    private static final Logger logger = LoggerFactory.getLogger(JobService.class);

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyService companyService;

    @Transactional
    public Job createJob(Job job, User user) {
        try {
            logger.info("Creating job for user: {}", user.getEmail());
            
            // Create a minimal company if it doesn't exist
            Company company = companyService.getCompanyByUser(user);
            if (company == null) {
                company = new Company();
                company.setName(user.getName() + "'s Company");
                company.setUser(user);
                company = companyService.createCompany(company);
            }
            
            // Set the company based on the user
            job.setCompany(company);
            // Set the posting date
            job.setPostedDate(LocalDateTime.now());
            // Set the job as active
            job.setActive(true);

            Job savedJob = jobRepository.save(job);
            logger.info("Successfully created job with ID: {}", savedJob.getId());
            return savedJob;
        } catch (Exception e) {
            logger.error("Error creating job: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Job> getAllJobs() {
        try {
            logger.info("Fetching all jobs");
            List<Job> jobs = jobRepository.findAll();
            logger.info("Successfully fetched {} jobs", jobs.size());
            return jobs;
        } catch (Exception e) {
            logger.error("Error fetching all jobs: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Job> getJobsByUser(User user) {
        try {
            logger.info("Fetching jobs for user: {}", user.getEmail());
            Company company = companyService.getCompanyByUser(user);
            if (company == null) {
                return List.of(); // Return empty list if no company exists
            }
            List<Job> jobs = jobRepository.findByCompany(company);
            logger.info("Successfully fetched {} jobs for company: {}", jobs.size(), company.getName());
            return jobs;
        } catch (Exception e) {
            logger.error("Error fetching jobs for user: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Optional<Job> getJobById(Long id) {
        try {
            logger.info("Fetching job with ID: {}", id);
            return jobRepository.findById(id);
        } catch (Exception e) {
            logger.error("Error fetching job with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public Job updateJob(Long id, Job jobDetails, User user) {
        try {
            logger.info("Updating job with ID: {}", id);
            
            Company company = companyService.getCompanyByUser(user);
            if (company == null) {
                throw new RuntimeException("Company not found for user");
            }

            Job existingJob = getJobById(id)
                    .orElseThrow(() -> {
                        logger.error("Job not found with ID: {}", id);
                        return new RuntimeException("Job not found");
                    });
            
            // Check if the user owns this job
            if (!existingJob.getCompany().getId().equals(company.getId())) {
                logger.error("Unauthorized update attempt by user: {} for job: {}", user.getEmail(), id);
                throw new RuntimeException("Unauthorized to update this job");
            }

            logger.debug("Updating job details for job: {}", existingJob.getTitle());
            existingJob.setTitle(jobDetails.getTitle());
            existingJob.setDescription(jobDetails.getDescription());
            existingJob.setLocation(jobDetails.getLocation());
            existingJob.setType(jobDetails.getType());
            existingJob.setExperienceLevel(jobDetails.getExperienceLevel());
            existingJob.setSalary(jobDetails.getSalary());
            existingJob.setActive(jobDetails.isActive());

            Job updatedJob = jobRepository.save(existingJob);
            logger.info("Successfully updated job: {}", updatedJob.getTitle());
            return updatedJob;
        } catch (Exception e) {
            logger.error("Error updating job with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void deleteJob(Long id, User user) {
        try {
            logger.info("Deleting job with ID: {}", id);
            Company company = companyService.getCompanyByUser(user);
            if (company == null) {
                throw new RuntimeException("Company not found for user");
            }

            Job job = getJobById(id)
                    .orElseThrow(() -> {
                        logger.error("Job not found with ID: {}", id);
                        return new RuntimeException("Job not found");
                    });
            
            // Check if the user owns this job
            if (!job.getCompany().getId().equals(company.getId())) {
                logger.error("Unauthorized delete attempt by user: {} for job: {}", user.getEmail(), id);
                throw new RuntimeException("Unauthorized to delete this job");
            }

            jobRepository.delete(job);
            logger.info("Successfully deleted job with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting job with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
} 