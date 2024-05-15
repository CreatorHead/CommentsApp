package com.intuit.comments.exceptions;

public class PostNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8486778608548741518L;

	public PostNotFoundException(String message) {
		super(message);
	}
}
