package com.jobportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jobportal", "com.jobhive"})
@EnableJpaRepositories(basePackages = {"com.jobportal.repository", "com.jobhive.repository"})
@EntityScan(basePackages = {"com.jobportal.entity", "com.jobhive.entity"})
public class JobPortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobPortalApplication.class, args);
    }
} 