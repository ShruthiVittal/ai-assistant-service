package com.assistant.ai.client;

import com.assistant.ai.client.dto.GeminiRequest;
import com.assistant.ai.client.dto.GeminiResponse;
import com.assistant.ai.config.GeminiConfig;
import com.assistant.ai.dto.request.ChatRequest;
import com.assistant.ai.exception.GeminiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;

/**
 * Client service for interacting with Google Gemini AI.
 * 
 * <p>This class handles all communication with the Google Gemini API using Spring WebClient.
 * It transforms chat requests into the format expected by Gemini's REST API and extracts
 * the response text from the API response structure.</p>
 * 
 * <p>Key responsibilities:
 * <ul>
 *   <li>Building Gemini API request payloads</li>
 *   <li>Making HTTP POST requests to Gemini API endpoints</li>
 *   <li>Handling API errors and timeouts</li>
 *   <li>Extracting response text from Gemini's response structure</li>
 * </ul>
 * </p>
 * 
 * @author AI Assistant Service
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final WebClient geminiWebClient;
    private final GeminiConfig config;

    /** Default timeout for API calls: 30 seconds */
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

    /**
     * Sends a chat request to Google Gemini AI.
     * 
     * <p>This method:
     * <ol>
     *   <li>Extracts or defaults the model name</li>
     *   <li>Builds the request payload in Gemini's expected format</li>
     *   <li>Makes an HTTP POST request to the Gemini API</li>
     *   <li>Extracts and returns the response text</li>
     * </ol>
     * </p>
     *
     * @param chatRequest the chat request containing message and optional model configuration
     * @return Mono containing the response text from Gemini
     * @throws GeminiException if the API call fails or returns an error
     */
    public Mono<String> chat(ChatRequest chatRequest) {
        log.info("Sending chat request to Google Gemini with message length: {}", 
                chatRequest.getMessage() != null ? chatRequest.getMessage().length() : 0);

        // Get model name from request or use default from configuration
        String modelName = chatRequest.getModel() != null && !chatRequest.getModel().trim().isEmpty()
                ? chatRequest.getModel().trim()
                : config.getDefaultModel();

        // Build request body in Gemini API format: {"contents": [{"parts": [{"text": "message"}]}]}
        GeminiRequest requestBody = buildRequestBody(chatRequest.getMessage());
        
        // Build endpoint: /models/{model}:generateContent?key={apiKey}
        String endpoint = String.format("/models/%s:generateContent", modelName);

        return geminiWebClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(endpoint)
                        .queryParam("key", config.getGeminiApiKey())
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .timeout(DEFAULT_TIMEOUT)
                .map(this::extractResponseText)
                .doOnSuccess(response -> log.debug("Successfully received response from Google Gemini"))
                .doOnError(error -> log.error("Error calling Google Gemini API: {}", error.getMessage()))
                .onErrorMap(WebClientResponseException.class, ex -> {
                    log.error("Google Gemini API error - Status: {}, Body: {}", 
                            ex.getStatusCode(), ex.getResponseBodyAsString());
                    return new GeminiException(
                            "Failed to communicate with Google Gemini API: " + ex.getMessage(),
                            ex.getStatusCode().value()
                    );
                })
                .onErrorMap(ex -> {
                    if (ex instanceof GeminiException) {
                        return ex;
                    }
                    log.error("Unexpected error calling Google Gemini API: {}", ex.getMessage());
                    return new GeminiException("Unexpected error calling Google Gemini API: " + ex.getMessage(), 500);
                });
    }

    /**
     * Builds the request body for Google Gemini API.
     * 
     * <p>Transforms a simple message string into Gemini's required request structure:
     * <pre>
     * {
     *   "contents": [{
     *     "parts": [{"text": "message"}]
     *   }]
     * }
     * </pre>
     * </p>
     *
     * @param message the message/prompt to send to Gemini
     * @return GeminiRequest object with properly structured content
     */
    private GeminiRequest buildRequestBody(String message) {
        GeminiRequest.Part part = GeminiRequest.Part.builder()
                .text(message)
                .build();

        GeminiRequest.Content content = GeminiRequest.Content.builder()
                .parts(Collections.singletonList(part))
                .build();

        return GeminiRequest.builder()
                .contents(Collections.singletonList(content))
                .build();
    }

    /**
     * Extracts the response text from Google Gemini API response.
     * 
     * <p>Gemini API returns responses in this structure:
     * <pre>
     * {
     *   "candidates": [{
     *     "content": {
     *       "parts": [{"text": "response text"}]
     *     }
     *   }]
     * }
     * </pre>
     * This method navigates through this structure to extract the text.
     * </p>
     *
     * @param response the response from Google Gemini API
     * @return extracted text response
     * @throws GeminiException if the response structure is invalid or empty
     */
    private String extractResponseText(GeminiResponse response) {
        // Validate response structure
        if (response == null || response.getCandidates() == null || response.getCandidates().isEmpty()) {
            throw new GeminiException("Empty response from Google Gemini API", 500);
        }

        // Get first candidate (Gemini typically returns one)
        GeminiResponse.Candidate candidate = response.getCandidates().get(0);
        if (candidate.getContent() == null || candidate.getContent().getParts() == null 
                || candidate.getContent().getParts().isEmpty()) {
            throw new GeminiException("No content in response from Google Gemini API", 500);
        }

        // Extract text from first part
        return candidate.getContent().getParts().get(0).getText();
    }
}
