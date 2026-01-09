package com.assistant.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for AI chat endpoint.
 * 
 * <p>This class represents the request payload for the chat API endpoint.
 * It contains the user's message and optional model configuration.</p>
 * 
 * <p>Validation:
 * <ul>
 *   <li>Message is required and must be between 1-10000 characters</li>
 *   <li>Model name is optional (defaults to gemini-2.5-flash) and max 50 characters</li>
 *   <li>Driver field is reserved for future use</li>
 * </ul>
 * </p>
 * 
 * @author AI Assistant Service
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    /** The message/prompt to send to the AI model. Required field. */
    @NotBlank(message = "Message cannot be blank")
    @Size(min = 1, max = 10000, message = "Message must be between 1 and 10000 characters")
    private String message;

    /** 
     * The AI model to use (e.g., "gemini-2.5-flash", "gemini-1.5-pro"). 
     * Optional - defaults to configured default model.
     */
    @Size(max = 50, message = "Model name must not exceed 50 characters")
    private String model;
}

