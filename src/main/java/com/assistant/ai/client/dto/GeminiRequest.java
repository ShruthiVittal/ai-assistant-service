package com.assistant.ai.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for Google Gemini API request.
 * 
 * <p>This class represents the request structure expected by Google Gemini API.
 * The structure follows Gemini's API specification:
 * <pre>
 * {
 *   "contents": [{
 *     "parts": [{"text": "message"}]
 *   }]
 * }
 * </pre>
 * </p>
 * 
 * @author AI Assistant Service
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeminiRequest {

    /** List of content items containing the message parts */
    @JsonProperty("contents")
    private List<Content> contents;

    /**
     * Inner class representing a content item in the Gemini request.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        /** List of parts containing the actual message content */
        @JsonProperty("parts")
        private List<Part> parts;
    }

    /**
     * Inner class representing a part of the content (text message).
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Part {
        /** The text content of the message */
        @JsonProperty("text")
        private String text;
    }
}

