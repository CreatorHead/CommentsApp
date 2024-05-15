package com.intuit.comments.exceptions;

public class CommentValidationException extends RuntimeException {

	private static final long serialVersionUID = -8782702548790983166L;

	public CommentValidationException(String message) {
		super(message);
	}

}
