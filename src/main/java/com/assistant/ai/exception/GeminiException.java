package com.assistant.ai.exception;

/**
 * Custom exception for Google Gemini API related errors.
 * 
 * <p>This exception is thrown when:
 * <ul>
 *   <li>API calls to Gemini fail</li>
 *   <li>Invalid responses are received</li>
 *   <li>Network or timeout errors occur</li>
 * </ul>
 * </p>
 * 
 * <p>The exception includes the HTTP status code from the API response,
 * which is used by the global exception handler to return appropriate error responses.</p>
 * 
 * @author AI Assistant Service
 * @version 1.0
 * @since 1.0
 */
public class GeminiException extends RuntimeException {

    /** HTTP status code from the API error response */
    private final int statusCode;

    public GeminiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

