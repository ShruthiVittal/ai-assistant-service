package com.assistant.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for AI chat endpoint.
 * 
 * <p>This class represents the standardized response structure returned by the chat API.
 * It includes the AI-generated response along with metadata about the request.</p>
 * 
 * @author AI Assistant Service
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    /** The AI-generated response text */
    private String response;

    /** The model that was used to generate the response */
    private String model;

    /** Timestamp when the response was generated */
    private LocalDateTime timestamp;

    /** Indicates if the request was successful */
    private boolean success;
}

