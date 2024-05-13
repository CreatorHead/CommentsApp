package com.intuit.comments.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Centralized exception handling for the application, intercepting uncaught
 * exceptions across all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles generic exceptions by logging and returning a standardized error
	 * response.
	 *
	 * @param ex the exception to handle
	 * @return a ResponseEntity with INTERNAL_SERVER_ERROR status and a message to
	 *         retry later
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGlobalException(Exception ex) {
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("An error occurred. Please try again later.");
	}
}