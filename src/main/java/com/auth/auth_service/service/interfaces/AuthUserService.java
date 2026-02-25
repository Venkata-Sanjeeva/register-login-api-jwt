package com.auth.auth_service.service.interfaces;

import com.auth.auth_service.exception.EmailAlreadyExistsException;
import com.auth.auth_service.exception.InvalidLoginCredentialsException;
import com.auth.auth_service.request.LoginRequest;
import com.auth.auth_service.request.RegisterRequest;
import com.auth.auth_service.response.LoginResponse;
import com.auth.auth_service.response.RegisterResponse;

public interface AuthUserService {
	RegisterResponse register(RegisterRequest request, String role) throws EmailAlreadyExistsException;
	
	LoginResponse login(LoginRequest request) throws InvalidLoginCredentialsException;
}
