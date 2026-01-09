package com.assistant.ai.controller;

import com.assistant.ai.dto.request.ChatRequest;
import com.assistant.ai.dto.response.ChatResponse;
import com.assistant.ai.service.AiAssistantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * REST Controller for AI Assistant API endpoints.
 * 
 * <p>This controller provides RESTful endpoints for interacting with Google Gemini AI.
 * It handles HTTP requests, validates input, and returns AI-generated responses.</p>
 * 
 * <p>Endpoints:
 * <ul>
 *   <li>POST /api/v1/chat - Send a message to AI and get response</li>
 *   <li>GET /api/v1/health - Health check endpoint</li>
 * </ul>
 * </p>
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AiAssistantController {

    private final AiAssistantService aiAssistantService;

    /**
     * Chat endpoint for AI interactions.
     * 
     * <p>This endpoint accepts a chat request with a message and optional model specification,
     * processes it through the AI service, and returns the AI-generated response.</p>
     * 
     * <p>Request validation:
     * <ul>
     *   <li>Message is required (1-10000 characters)</li>
     *   <li>Model is optional (defaults to gemini-2.5-flash)</li>
     *   <li>Invalid models are automatically replaced with default</li>
     * </ul>
     * </p>
     *
     * @param chatRequest the validated chat request containing message and optional model
     * @return Mono containing ResponseEntity with ChatResponse including AI response
     * @throws org.springframework.web.bind.MethodArgumentNotValidException if validation fails
     */
    @PostMapping(
            value = "/chat",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<ChatResponse>> chat(@Valid @RequestBody ChatRequest chatRequest) {
        log.info("Received chat request - Model: {}, Message length: {}", 
                chatRequest.getModel(), 
                chatRequest.getMessage() != null ? chatRequest.getMessage().length() : 0);

        return aiAssistantService.chat(chatRequest)
                .map(ResponseEntity.status(HttpStatus.OK)::body)
                .doOnError(error -> log.error("Error in chat endpoint: {}", error.getMessage()));
    }

    /**
     * Health check endpoint.
     *
     * @return ResponseEntity with health status
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("AI Assistant Service is running");
    }
}

