package com.jobhive.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class SessionValidationInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(SessionValidationInterceptor.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String requestURI = request.getRequestURI();
        String sessionId = request.getSession(false) != null ? request.getSession().getId() : "no session";
        String timestamp = LocalDateTime.now().format(formatter);
        
        // Log detailed session information
        logger.info("Session Validation - Timestamp: {}, URI: {}, Authenticated: {}, Principal: {}, Session ID: {}, Remote IP: {}", 
            timestamp,
            requestURI,
            authentication != null && authentication.isAuthenticated(),
            authentication != null ? authentication.getPrincipal() : "null",
            sessionId,
            request.getRemoteAddr()
        );
        
        // Check if the request is for protected resources
        if (requestURI.startsWith("/admin/") || requestURI.startsWith("/api/admin/")) {
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.warn("Unauthorized access attempt to admin resource - Timestamp: {}, URI: {}, IP: {}", 
                    timestamp, requestURI, request.getRemoteAddr());
                response.sendRedirect("/login.html?session=expired");
                return false;
            }
            
            if (!authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
                logger.warn("Invalid role access attempt to admin resource - Timestamp: {}, URI: {}, IP: {}, User: {}", 
                    timestamp, requestURI, request.getRemoteAddr(), authentication.getPrincipal());
                response.sendRedirect("/login.html?error=unauthorized");
                return false;
            }
        }
        else if (requestURI.startsWith("/jobseeker/") || requestURI.startsWith("/api/jobseeker/")) {
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.warn("Unauthorized access attempt to jobseeker resource - Timestamp: {}, URI: {}, IP: {}", 
                    timestamp, requestURI, request.getRemoteAddr());
                response.sendRedirect("/login.html?session=expired");
                return false;
            }
            
            if (!authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("JOB_SEEKER"))) {
                logger.warn("Invalid role access attempt to jobseeker resource - Timestamp: {}, URI: {}, IP: {}, User: {}", 
                    timestamp, requestURI, request.getRemoteAddr(), authentication.getPrincipal());
                response.sendRedirect("/login.html?error=unauthorized");
                return false;
            }
        }
        else if (requestURI.startsWith("/employer/") || requestURI.startsWith("/api/employer/")) {
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.warn("Unauthorized access attempt to employer resource - Timestamp: {}, URI: {}, IP: {}", 
                    timestamp, requestURI, request.getRemoteAddr());
                response.sendRedirect("/login.html?session=expired");
                return false;
            }
            
            if (!authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("EMPLOYER"))) {
                logger.warn("Invalid role access attempt to employer resource - Timestamp: {}, URI: {}, IP: {}, User: {}", 
                    timestamp, requestURI, request.getRemoteAddr(), authentication.getPrincipal());
                response.sendRedirect("/login.html?error=unauthorized");
                return false;
            }
        }
        
        // Validate session for all authenticated requests
        if (authentication != null && authentication.isAuthenticated()) {
            if (request.getSession(false) == null) {
                logger.warn("Session expired for authenticated user - Timestamp: {}, User: {}, IP: {}", 
                    timestamp, authentication.getPrincipal(), request.getRemoteAddr());
                response.sendRedirect("/login.html?session=expired");
                return false;
            }
            
            // Check if the session is still valid
            if (request.getSession(false).getAttribute("SPRING_SECURITY_CONTEXT") == null) {
                logger.warn("Security context not found in session - Timestamp: {}, User: {}, IP: {}", 
                    timestamp, authentication.getPrincipal(), request.getRemoteAddr());
                response.sendRedirect("/login.html?session=expired");
                return false;
            }
        }
        
        return true;
    }
} 
