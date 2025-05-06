package com.jobhive.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.jobhive.entity.Company;
import com.jobhive.entity.User;
import com.jobhive.entity.UserRole;
import com.jobhive.repository.CompanyRepository;
import com.jobhive.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if users already exist
        if (userRepository.count() == 0) {
            // Create admin user
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@jobportal.com");
            admin.setPassword("admin123");
            admin.setRole(UserRole.ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);

            // Create employer user
            User employer = new User();
            employer.setName("Tech Company");
            employer.setEmail("employer@jobportal.com");
            employer.setPassword("employer123");
            employer.setRole(UserRole.EMPLOYER);
            employer.setEnabled(true);
            userRepository.save(employer);

            // Create job seeker user
            User jobSeeker = new User();
            jobSeeker.setName("John Doe");
            jobSeeker.setEmail("jobseeker@jobportal.com");
            jobSeeker.setPassword("jobseeker123");
            jobSeeker.setRole(UserRole.JOB_SEEKER);
            jobSeeker.setEnabled(true);
            userRepository.save(jobSeeker);

            // Create another employer
            User employer2 = new User();
            employer2.setName("Marketing Solutions");
            employer2.setEmail("marketing@jobportal.com");
            employer2.setPassword("marketing123");
            employer2.setRole(UserRole.EMPLOYER);
            employer2.setEnabled(true);
            userRepository.save(employer2);
        }
        
        // Check if companies already exist
        if (companyRepository.count() == 0) {
            // Create sample companies
            userRepository.findByEmail("employer@jobportal.com").ifPresent(employer -> {
                // Create Tech Corp
                Company techCorp = new Company();
                techCorp.setName("Tech Corp");
                techCorp.setDescription("A leading technology company specializing in software development");
                techCorp.setIndustry("Technology");
                techCorp.setLocation("New York, NY");
                techCorp.setWebsite("https://techcorp.example.com");
                techCorp.setContactEmail("contact@techcorp.example.com");
                techCorp.setContactPhone("555-123-4567");
                techCorp.setLogoUrl("https://example.com/logos/techcorp.png");
                techCorp.setUser(employer);
                techCorp.setSize("MEDIUM");
                techCorp.setActive(true);
                companyRepository.save(techCorp);
                
                // Create Digital Innovations
                Company digitalInnovations = new Company();
                digitalInnovations.setName("Digital Innovations");
                digitalInnovations.setDescription("Pioneering digital transformation for enterprises");
                digitalInnovations.setIndustry("Digital");
                digitalInnovations.setLocation("Boston, MA");
                digitalInnovations.setWebsite("https://digitalinnovations.example.com");
                digitalInnovations.setContactEmail("hello@digitalinnovations.example.com");
                digitalInnovations.setContactPhone("555-456-7890");
                digitalInnovations.setLogoUrl("https://example.com/logos/digitalinnovations.png");
                digitalInnovations.setUser(employer);
                digitalInnovations.setSize("LARGE");
                digitalInnovations.setActive(true);
                companyRepository.save(digitalInnovations);
            });
            
            userRepository.findByEmail("marketing@jobportal.com").ifPresent(employer2 -> {
                // Create Software Solutions
                Company softwareSolutions = new Company();
                softwareSolutions.setName("Software Solutions");
                softwareSolutions.setDescription("Providing innovative software solutions for businesses");
                softwareSolutions.setIndustry("Software");
                softwareSolutions.setLocation("San Francisco, CA");
                softwareSolutions.setWebsite("https://softwaresol.example.com");
                softwareSolutions.setContactEmail("info@softwaresol.example.com");
                softwareSolutions.setContactPhone("555-987-6543");
                softwareSolutions.setLogoUrl("https://example.com/logos/softwaresol.png");
                softwareSolutions.setUser(employer2);
                softwareSolutions.setSize("SMALL");
                softwareSolutions.setActive(true);
                companyRepository.save(softwareSolutions);
                
                // Create Cloud Systems
                Company cloudSystems = new Company();
                cloudSystems.setName("Cloud Systems");
                cloudSystems.setDescription("Cloud infrastructure and services provider");
                cloudSystems.setIndustry("Cloud Computing");
                cloudSystems.setLocation("Seattle, WA");
                cloudSystems.setWebsite("https://cloudsystems.example.com");
                cloudSystems.setContactEmail("support@cloudsystems.example.com");
                cloudSystems.setContactPhone("555-789-0123");
                cloudSystems.setLogoUrl("https://example.com/logos/cloudsystems.png");
                cloudSystems.setUser(employer2);
                cloudSystems.setSize("ENTERPRISE");
                cloudSystems.setActive(true);
                companyRepository.save(cloudSystems);
            });
        }
    }
} 
