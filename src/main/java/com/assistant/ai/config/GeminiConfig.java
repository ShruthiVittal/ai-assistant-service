package com.assistant.ai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for Google Gemini AI client.
 * 
 * <p>This configuration class sets up all beans required for Google Gemini integration:
 * <ul>
 *   <li>ObjectMapper for JSON processing</li>
 *   <li>WebClient for HTTP communication with Gemini API</li>
 * </ul>
 * </p>
 * 
 * <p>Configuration properties (from application.properties):
 * <ul>
 *   <li>gemini.api-key - Google Gemini API key (required)</li>
 *   <li>gemini.model - Default model name (default: gemini-2.5-flash)</li>
 *   <li>gemini.base-url - Gemini API base URL (default: https://generativelanguage.googleapis.com/v1beta)</li>
 * </ul>
 * </p>
 * 
 * @author AI Assistant Service
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class GeminiConfig {

    /** Google Gemini API key - required for authentication */
    @Value("${gemini.api-key}")
    private String geminiApiKey;

    /** Default Gemini model to use when not specified in request */
    @Value("${gemini.model:gemini-2.5-flash}")
    private String defaultModel;

    /** Base URL for Google Gemini API */
    @Value("${gemini.base-url:https://generativelanguage.googleapis.com/v1beta}")
    private String geminiBaseUrl;

    /**
     * Creates and configures ObjectMapper for JSON processing.
     *
     * @return configured ObjectMapper instance
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper;
    }

    /**
     * Creates and configures WebClient for Google Gemini API calls.
     *
     * @return configured WebClient instance
     */
    @Bean
    public WebClient geminiWebClient() {
        return WebClient.builder()
                .baseUrl(geminiBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public String getGeminiApiKey() {
        return geminiApiKey;
    }

    public String getDefaultModel() {
        return defaultModel;
    }

    public String getGeminiBaseUrl() {
        return geminiBaseUrl;
    }
}
