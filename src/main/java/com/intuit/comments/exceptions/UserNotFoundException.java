package com.intuit.comments.exceptions;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8930579856308274056L;

	public UserNotFoundException(String message) {
		super(message);
	}

}
