package com.auth.auth_service.service.impl;

import org.springframework.stereotype.Service;

import com.auth.auth_service.entity.User;
import com.auth.auth_service.enums.Roles;
import com.auth.auth_service.exception.InvalidLoginCredentialsException;
import com.auth.auth_service.request.LoginRequest;
import com.auth.auth_service.request.RegisterRequest;
import com.auth.auth_service.response.LoginResponse;
import com.auth.auth_service.response.RegisterResponse;
import com.auth.auth_service.service.interfaces.AuthUserService;
import com.auth.auth_service.util.JWTUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {
	
    private final UserServiceImpl userService;
    private final JWTUtils jwtUtil;
	
    @Override
	public RegisterResponse register(RegisterRequest request, String role) {

		User savedUser = userService.registerUser(request.getName(), request.getEmail(), request.getPassword(), role);

        String token = jwtUtil.generateTokenUsingEmail(savedUser.getEmail());

        return RegisterResponse.builder()
        		.email(request.getEmail())
        		.token(token)
        		.build();
    }

    @Override
	public LoginResponse login(LoginRequest request) {
	    
	    if (!userService.verifyUser(request.getEmail(), request.getPassword())) {
	        throw new InvalidLoginCredentialsException("Invalid email or password");
	    }
	    
	    User user = userService.getUserByEmail(request.getEmail());

	    String token = jwtUtil.generateTokenUsingEmailAndRole(user.getEmail(), Roles.valueOf(user.getRole()));

	    return LoginResponse.builder()
	    		.userUID(user.getUserUID())
	            .email(user.getEmail())
	            .token(token)
	            .role(user.getRole().toString())
	            .build();
	}
}