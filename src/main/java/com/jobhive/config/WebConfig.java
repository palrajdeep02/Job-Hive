package com.jobhive.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jobhive.security.SessionValidationInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Handle static resources
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        
        // Handle assets
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
        
        // Handle uploaded files
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");

        // Handle CSS files
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/assets/css/");
        
        // Handle JS files
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/assets/js/");
        
        // Handle images
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/assets/images/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Root and authentication pages
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/login").setViewName("forward:/login.html");
        registry.addViewController("/register").setViewName("forward:/register.html");

        // Job pages
        registry.addViewController("/jobs").setViewName("forward:/jobs.html");
        registry.addViewController("/job-details").setViewName("forward:/job-details.html");

        // Admin pages
        registry.addViewController("/admin/dashboard").setViewName("forward:/admin/dashboard.html");
        registry.addViewController("/admin/users").setViewName("forward:/admin/users.html");
        registry.addViewController("/admin/companies").setViewName("forward:/admin/companies.html");
        registry.addViewController("/admin/jobs").setViewName("forward:/admin/jobs.html");
        registry.addViewController("/admin/applications").setViewName("forward:/admin/applications.html");
        registry.addViewController("/admin/reports").setViewName("forward:/admin/reports.html");
        registry.addViewController("/admin/profile").setViewName("forward:/admin/profile.html");

        // Employer pages
        registry.addViewController("/employer/dashboard").setViewName("forward:/employer/dashboard.html");
        registry.addViewController("/employer/jobs").setViewName("forward:/employer/jobs.html");
        registry.addViewController("/employer/applications").setViewName("forward:/employer/applications.html");
        registry.addViewController("/employer/company").setViewName("forward:/employer/company.html");

        // Job seeker pages
        registry.addViewController("/jobseeker/dashboard").setViewName("forward:/jobseeker/dashboard.html");
        registry.addViewController("/jobseeker/applications").setViewName("forward:/jobseeker/applications.html");
        registry.addViewController("/jobseeker/resumes").setViewName("forward:/jobseeker/resumes.html");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionValidationInterceptor())
                .addPathPatterns(
                    "/admin/**", 
                    "/api/admin/**",
                    "/jobseeker/**", 
                    "/api/jobseeker/**",
                    "/employer/**",
                    "/api/employer/**"
                );
    }
} 
