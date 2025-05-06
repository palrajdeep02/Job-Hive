package com.jobhive.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetails.put("message", message != null ? message : "An unexpected error occurred");
        errorDetails.put("error", exception != null ? exception.toString() : "Unknown error");

        return ResponseEntity
                .status(status != null ? Integer.parseInt(status.toString()) : HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(errorDetails);
    }
} 
