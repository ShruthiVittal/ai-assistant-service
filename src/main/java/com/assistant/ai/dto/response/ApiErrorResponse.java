package com.assistant.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response DTO for API error handling.
 * 
 * <p>This class provides a consistent structure for all error responses across the API.
 * It follows REST API best practices for error reporting.</p>
 * 
 * @author AI Assistant Service
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    /** Timestamp when the error occurred */
    private LocalDateTime timestamp;

    /** HTTP status code */
    private int status;

    /** Error type/category */
    private String error;

    /** Human-readable error message */
    private String message;

    /** Request path where the error occurred */
    private String path;

    /** Additional error details (e.g., validation errors) */
    private List<String> details;
}

