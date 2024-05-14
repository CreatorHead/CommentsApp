package com.intuit.comments.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;

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
    
    /**
     * Handles ResourceNotFoundException thrown by any controller method in the application.
     * This method ensures that a proper HTTP response is sent back to the client with a 404 status code
     * and the exception's message as the response body.
     * 
     * @param ex the ResourceNotFoundException thrown by a controller method
     * @return ResponseEntity containing the exception message and HTTP status 404 (Not Found)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found...");
    }
    
    
    /**
     * Handles MethodArgumentTypeMismatchException, which occurs when the method argument is of an incorrect type.
     *
     * @param ex the MethodArgumentTypeMismatchException thrown when an argument of incorrect type is passed
     * @return ResponseEntity with an error message and HTTP status BAD_REQUEST
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>("Invalid post ID format", HttpStatus.BAD_REQUEST);
    }
    
    
    /**
     * Handles EntityNotFoundException, which occurs when the requested entity is not found.
     *
     * @param ex the EntityNotFoundException thrown when an entity is not found
     * @return ResponseEntity with the exception message and HTTP status NOT_FOUND
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}