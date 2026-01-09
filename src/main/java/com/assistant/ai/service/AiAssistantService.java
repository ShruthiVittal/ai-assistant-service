package com.assistant.ai.service;

import com.assistant.ai.client.GeminiClient;
import com.assistant.ai.dto.request.ChatRequest;
import com.assistant.ai.dto.response.ChatResponse;
import com.assistant.ai.model.AiModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Service layer for AI Assistant functionality.
 * 
 * <p>This service contains the core business logic for processing chat requests.
 * It handles model validation, request transformation, and response building.</p>
 * 
 * <p>Key responsibilities:
 * <ul>
 *   <li>Validating and normalizing AI model names</li>
 *   <li>Transforming requests for the Gemini client</li>
 *   <li>Building standardized response DTOs</li>
 *   <li>Error handling and logging</li>
 * </ul>
 * </p>
 * 
 * @author AI Assistant Service
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiAssistantService {

    private final GeminiClient geminiClient;
    
    @Value("${gemini.model:gemini-2.5-flash}")
    private String defaultModel;

    /**
     * Processes a chat request and returns AI response.
     * 
     * <p>This method orchestrates the chat flow:
     * <ol>
     *   <li>Validates and normalizes the model name</li>
     *   <li>Creates a validated request with proper model</li>
     *   <li>Calls the Gemini client to get AI response</li>
     *   <li>Builds and returns a standardized response</li>
     * </ol>
     * </p>
     *
     * @param chatRequest the chat request containing message and optional model
     * @return Mono containing ChatResponse with AI-generated response
     */
    public Mono<ChatResponse> chat(ChatRequest chatRequest) {
        log.info("Processing chat request with message length: {}", 
                chatRequest.getMessage() != null ? chatRequest.getMessage().length() : 0);

        // Validate and normalize model name - ensures it's a valid Gemini model
        String modelName = validateAndNormalizeModel(chatRequest.getModel());
        
        // Create a new request with the validated model (replaces invalid models with default)
        ChatRequest validatedRequest = ChatRequest.builder()
                .message(chatRequest.getMessage())
                .model(modelName)
                .build();

        return geminiClient.chat(validatedRequest)
                .map(response -> buildChatResponse(response, modelName))
                .doOnSuccess(response -> log.info("Successfully processed chat request with model: {}", modelName))
                .doOnError(error -> log.error("Error processing chat request: {}", error.getMessage()));
    }

    /**
     * Validates and normalizes the model name.
     * 
     * <p>Validation logic:
     * <ol>
     *   <li>If null/empty, returns default model</li>
     *   <li>If matches known enum model, returns normalized name</li>
     *   <li>If starts with "gemini-", allows it (for future models)</li>
     *   <li>Otherwise, logs warning and returns default</li>
     * </ol>
     * </p>
     *
     * @param requestedModel the model name from request (can be null)
     * @return validated Gemini model name (never null)
     */
    private String validateAndNormalizeModel(String requestedModel) {
        // Handle null or empty model - use default
        if (requestedModel == null || requestedModel.trim().isEmpty()) {
            log.debug("No model specified, using default: {}", defaultModel);
            return defaultModel;
        }

        String trimmedModel = requestedModel.trim();
        
        // Check if it's a known Gemini model from enum (case-insensitive)
        AiModel model = AiModel.fromString(trimmedModel);
        if (model != null) {
            log.debug("Using requested Gemini model: {}", model.getModelName());
            return model.getModelName();
        }
        
        // Allow any model starting with "gemini-" (for future/new models not yet in enum)
        if (trimmedModel.startsWith("gemini-")) {
            log.debug("Using requested Gemini model (not in enum): {}", trimmedModel);
            return trimmedModel;
        }

        // Invalid model - log warning and fallback to default
        log.warn("Invalid model '{}' requested. Must be a Gemini model (e.g., gemini-2.5-flash). Using default: {}", 
                trimmedModel, defaultModel);
        return defaultModel;
    }

    /**
     * Builds a ChatResponse from the AI response string.
     *
     * @param aiResponse the response from Google Gemini
     * @param modelName the model used
     * @return ChatResponse object
     */
    private ChatResponse buildChatResponse(String aiResponse, String modelName) {
        return ChatResponse.builder()
                .response(aiResponse)
                .model(modelName)
                .timestamp(LocalDateTime.now())
                .success(true)
                .build();
    }
}

