package com.auth.auth_service.exception;

public class EmailAlreadyExistsException extends RuntimeException {
	public EmailAlreadyExistsException(String msg) {
		super(msg);
	}
}
