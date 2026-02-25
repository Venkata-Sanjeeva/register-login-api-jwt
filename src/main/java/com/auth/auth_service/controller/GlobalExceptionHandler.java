package com.auth.auth_service.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth.auth_service.exception.EmailAlreadyExistsException;
import com.auth.auth_service.exception.InvalidLoginCredentialsException;
import com.auth.auth_service.exception.UserNotFoundException;
import com.auth.auth_service.response.ErrorResponse;
import com.auth.auth_service.response.GlobalResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles 404 Not Found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GlobalResponse<ErrorResponse>> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            ex.getMessage(),
            request.getRequestURI()
        );
        
        GlobalResponse<ErrorResponse> response = new GlobalResponse<ErrorResponse>(HttpStatus.NOT_FOUND.value(), "User Not Found", error);
        
        return new ResponseEntity<GlobalResponse<ErrorResponse>>(response, HttpStatus.NOT_FOUND);
    }

    // Handles 409 Conflict (e.g., Email already exists)
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<GlobalResponse<ErrorResponse>> handleDuplicateEmail(EmailAlreadyExistsException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            ex.getMessage(),
            request.getRequestURI()
        );
        
        GlobalResponse<ErrorResponse> response = new GlobalResponse<ErrorResponse>(HttpStatus.CONFLICT.value(), "Registration Error", error);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(InvalidLoginCredentialsException.class)
    public ResponseEntity<GlobalResponse<Void>> handleInvalidLogin(InvalidLoginCredentialsException ex, HttpServletRequest request) {
        
        GlobalResponse<Void> response = GlobalResponse.<Void>builder()
                .status(HttpStatus.UNAUTHORIZED.value()) // 401 Unauthorized
                .message("Login Failed")
                .data(null) 
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<String, String>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));

        GlobalResponse<Map<String, String>> response = GlobalResponse.<Map<String, String>>builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .message("Validation Failed")
            .data(errors) // Here, 'data' is actually useful!
            .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    // Generic fallback for any other unexpected errors (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalResponse<ErrorResponse>> handleGlobalException(Exception ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            "An unexpected error occurred",
            request.getRequestURI()
        );
        
        GlobalResponse<ErrorResponse> response = new GlobalResponse<ErrorResponse>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", error);
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}