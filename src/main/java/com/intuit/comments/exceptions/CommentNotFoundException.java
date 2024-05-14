package com.intuit.comments.exceptions;

public class CommentNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 8311101410078130319L;

	public CommentNotFoundException(String message) {
		super(message);
	}

}
