package com.assistant.ai.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * DTO for Google Gemini API response.
 * 
 * <p>This class represents the response structure returned by Google Gemini API.
 * The structure follows Gemini's API specification:
 * <pre>
 * {
 *   "candidates": [{
 *     "content": {
 *       "parts": [{"text": "response"}]
 *     }
 *   }]
 * }
 * </pre>
 * </p>
 * 
 * <p>Uses @JsonIgnoreProperties to ignore unknown fields for forward compatibility.</p>
 * 
 * @author AI Assistant Service
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiResponse {

    /** List of candidate responses from Gemini (typically one) */
    @JsonProperty("candidates")
    private List<Candidate> candidates;

    /**
     * Inner class representing a candidate response from Gemini.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Candidate {
        /** The content of this candidate response */
        @JsonProperty("content")
        private Content content;
    }

    /**
     * Inner class representing the content structure in the response.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        /** List of parts containing the response text */
        @JsonProperty("parts")
        private List<Part> parts;
    }

    /**
     * Inner class representing a part of the content (response text).
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Part {
        /** The text content of the response */
        @JsonProperty("text")
        private String text;
    }
}

