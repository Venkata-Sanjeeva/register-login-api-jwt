package com.auth.auth_service.service.interfaces;

import com.auth.auth_service.entity.User;
import com.auth.auth_service.exception.EmailAlreadyExistsException;
import com.auth.auth_service.exception.InvalidLoginCredentialsException;
import com.auth.auth_service.exception.UserNotFoundException;

public interface UserService {
	User getUserByEmail(String email) throws UserNotFoundException;
	
	User registerUser(String name, String email, String password, String role) throws EmailAlreadyExistsException;
	
	boolean existsByEmail(String email);
	
	boolean verifyUser(String email, String password) throws InvalidLoginCredentialsException;
}
