package com.jobhive.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobhive.entity.User;
import com.jobhive.entity.UserRole;
import com.jobhive.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userData) {
        log.info("Registering new user: {}", userData.get("email"));
        try {
            User user = new User();
            user.setName(userData.get("name"));
            user.setEmail(userData.get("email"));
            user.setPassword(userData.get("password"));
            user.setRole(UserRole.valueOf(userData.get("role")));
            
            String token = authService.register(user);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", user.getRole().name());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid role specified for user: {}", userData.get("email"), e);
            return ResponseEntity.badRequest().body("Invalid role specified");
        } catch (RuntimeException e) {
            log.error("Registration failed for user: {}", userData.get("email"), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        log.info("Login attempt for user: {}", credentials.get("email"));
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            Map<String, String> result = authService.login(email, password);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            log.error("Login failed for user: {}", credentials.get("email"), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String timestamp = LocalDateTime.now().format(formatter);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null) {
            String username = auth.getName();
            String sessionId = request.getSession(false) != null ? request.getSession().getId() : "no session";
            String ipAddress = request.getRemoteAddr();
            
            log.info("Logout initiated - Timestamp: {}, User: {}, Session ID: {}, IP: {}", 
                timestamp, username, sessionId, ipAddress);
            
            // Perform logout
            new SecurityContextLogoutHandler().logout(request, response, auth);
            
            // Clear security context
            SecurityContextHolder.clearContext();
            
            // Invalidate session
            HttpSession session = request.getSession(false);
            if (session != null) {
                // Remove all session attributes
                java.util.Enumeration<String> attributeNames = session.getAttributeNames();
                while (attributeNames.hasMoreElements()) {
                    String attributeName = attributeNames.nextElement();
                    session.removeAttribute(attributeName);
                }
                
                // Invalidate the session
                session.invalidate();
                log.info("Session invalidated - Timestamp: {}, User: {}, Session ID: {}", 
                    timestamp, username, sessionId);
            }
            
            log.info("Logout successful - Timestamp: {}, User: {}, IP: {}", 
                timestamp, username, ipAddress);
            
            return ResponseEntity.ok().build();
        }
        
        log.warn("Logout attempted with no active session - Timestamp: {}, IP: {}", 
            timestamp, request.getRemoteAddr());
        return ResponseEntity.ok().build();
    }
} 
