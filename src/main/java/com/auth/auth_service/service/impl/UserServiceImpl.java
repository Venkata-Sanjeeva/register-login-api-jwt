package com.auth.auth_service.service.impl;

import org.springframework.stereotype.Service;

import com.auth.auth_service.entity.User;
import com.auth.auth_service.enums.Roles;
import com.auth.auth_service.exception.EmailAlreadyExistsException;
import com.auth.auth_service.exception.InvalidLoginCredentialsException;
import com.auth.auth_service.exception.UserNotFoundException;
import com.auth.auth_service.repository.UserRepository;
import com.auth.auth_service.service.interfaces.UserService;
import com.auth.auth_service.util.IdentifierGenerator;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public boolean existsByEmail(String email) {
		return userRepo.existsByEmail(email);
	}
	
	@Override
	public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with " + email + " not found!"));
    }

	@Override
    public User registerUser(String name, String email, String password, String role) {

        if (existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email + " already exists in DB");
        }

        User user = new User();
        
        String roleUC = role.toUpperCase();

        user.setUserUID(IdentifierGenerator.generate(roleUC));
        user.setName(name);
        user.setEmail(email);

        user.setPassword(passwordEncoder.encode(password));

        if(roleUC.equals("USER")) {
        	user.setRole(Roles.USER.toString());
        } else {
        	user.setRole(Roles.ADMIN.toString());
        }

        return userRepo.save(user);
    }
    
    @Override
    public boolean verifyUser(String userEmail, String userPassword) {
        User user = userRepo.findByEmail(userEmail).orElseThrow(() -> new InvalidLoginCredentialsException("Invalid email or password"));
        return passwordEncoder.matches(userPassword, user.getPassword());
    }

}
