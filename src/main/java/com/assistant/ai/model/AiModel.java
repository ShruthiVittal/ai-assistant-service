package com.assistant.ai.model;

import lombok.Getter;

/**
 * Enumeration of supported Google Gemini AI models.
 * 
 * <p>This enum provides a list of known Gemini models that can be used with the API.
 * It helps with model validation and provides a centralized list of available models.</p>
 * 
 * <p>Supported models:
 * <ul>
 *   <li>gemini-2.5-flash - Latest fast model (recommended)</li>
 *   <li>gemini-2.0-flash - Previous generation flash model</li>
 *   <li>gemini-1.5-pro - Pro model with advanced capabilities</li>
 *   <li>gemini-1.5-flash - Fast model from 1.5 generation</li>
 * </ul>
 * </p>
 * 
 * @author AI Assistant Service
 * @version 1.0
 */
@Getter
public enum AiModel {

    /** Gemini 2.5 Flash - Latest fast and efficient model (recommended) */
    GEMINI_2_5_FLASH("gemini-2.5-flash"),
    
    /** Gemini 2.0 Flash - Previous generation flash model */
    GEMINI_2_0_FLASH("gemini-2.0-flash"),
    
    /** Gemini 1.5 Pro - Advanced capabilities model */
    GEMINI_1_5_PRO("gemini-1.5-pro"),
    
    /** Gemini 1.5 Flash - Fast model from 1.5 generation */
    GEMINI_1_5_FLASH("gemini-1.5-flash");

    /** The model name as used in Gemini API */
    private final String modelName;

    /**
     * Constructor for AiModel enum.
     *
     * @param modelName the model name as used in API calls
     */
    AiModel(String modelName) {
        this.modelName = modelName;
    }

    /**
     * Get model by name, case-insensitive.
     *
     * @param modelName the model name to search for
     * @return the matching AiModel or null if not found
     */
    public static AiModel fromString(String modelName) {
        if (modelName == null) {
            return null;
        }
        for (AiModel model : values()) {
            if (model.modelName.equalsIgnoreCase(modelName)) {
                return model;
            }
        }
        return null;
    }
}

