package com.auth.auth_service.exception;

public class InvalidLoginCredentialsException extends RuntimeException {
	public InvalidLoginCredentialsException(String msg) {
		super(msg);
	}
}
