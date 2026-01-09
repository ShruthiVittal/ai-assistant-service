package com.assistant.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for AI Assistant Service.
 * 
 * <p>This is the entry point for the Spring Boot application that provides
 * REST API endpoints for interacting with Google Gemini AI.</p>
 * 
 * <p>The application provides:
 * <ul>
 *   <li>REST API for AI chat functionality</li>
 *   <li>Integration with Google Gemini AI</li>
 *   <li>Health check endpoints</li>
 *   <li>Comprehensive error handling</li>
 * </ul>
 * </p>
 * 
 * @author AI Assistant Service
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class AiAssistantServiceApplication {

	/**
	 * Main method to start the Spring Boot application.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(AiAssistantServiceApplication.class, args);
	}
}
