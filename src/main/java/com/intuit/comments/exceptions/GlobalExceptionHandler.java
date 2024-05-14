package com.intuit.comments.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
	

	/**
     * Handles validation exceptions thrown when method arguments fail validation.
     * This method extracts validation error messages and returns them in the response.
     *
     * @param ex the MethodArgumentNotValidException containing validation errors
     * @return a ResponseEntity with BAD_REQUEST status and a map of field errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}