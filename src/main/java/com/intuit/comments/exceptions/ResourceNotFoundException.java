package com.intuit.comments.exceptions;

public class ResourceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -3499068068032355886L;

	public ResourceNotFoundException(String message) {
		super(message);
	}
}