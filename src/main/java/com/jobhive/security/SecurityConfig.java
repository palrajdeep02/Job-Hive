package com.jobhive.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html", "/login.html", "/register.html", 
                               "/jobs.html", "/job-details.html", "/static/**", "/assets/**",
                               "/uploads/**", "/error", "/api/auth/**").permitAll()
                .requestMatchers("/admin/**", "/api/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/employer/**", "/api/employer/**").hasAuthority("EMPLOYER")
                .requestMatchers("/jobseeker/**", "/api/jobseeker/**").hasAuthority("JOB_SEEKER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login.html")
                .loginProcessingUrl("/api/auth/login")
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/login.html?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    if (authentication != null) {
                        log.info("User {} logged out successfully from IP {}", 
                            authentication.getName(), 
                            request.getRemoteAddr());
                    }
                    response.sendRedirect("/login.html?logout=success");
                })
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "X-Auth-Token")
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/login.html?expired=true")
                .and()
                .sessionFixation().migrateSession()
                .invalidSessionUrl("/login.html?invalid=true")
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            String redirectUrl;
            
            switch (role) {
                case "ADMIN":
                    redirectUrl = "/admin/dashboard.html";
                    break;
                case "EMPLOYER":
                    redirectUrl = "/employer/dashboard.html";
                    break;
                case "JOB_SEEKER":
                    redirectUrl = "/jobseeker/dashboard.html";
                    break;
                default:
                    redirectUrl = "/login.html?error=true";
            }
            
            response.sendRedirect(redirectUrl);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
} 
