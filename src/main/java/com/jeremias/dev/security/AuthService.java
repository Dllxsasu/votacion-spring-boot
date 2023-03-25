package com.jeremias.dev.security;

import java.net.URI;
import java.util.Collections;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jeremias.dev.exception.AppException;
import com.jeremias.dev.models.Role;
import com.jeremias.dev.models.User;
import com.jeremias.dev.payload.LoginRequest;
import com.jeremias.dev.payload.response.ApiResponse;
import com.jeremias.dev.payload.response.JwtAuthenticationResponse;
import com.jeremias.dev.payload.response.SignUpRequest;
import com.jeremias.dev.repository.RoleRepository;
import com.jeremias.dev.repository.UserRepository;
import com.jeremias.dev.utils.enums.RoleName;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class AuthService {
	
    private final AuthenticationManager authenticationManager;	
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;  
    private final JwtTokenProvider tokenProvider;		
    
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
		
	}
	
	
	public ResponseEntity<?> registerUser(SignUpRequest signUpRequest){
		 if(userRepository.existsByUsername(signUpRequest.getUsername())) {
	            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
	                    HttpStatus.BAD_REQUEST);
	        }

	        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
	            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
	                    HttpStatus.BAD_REQUEST);
	        }

	        // Creating user's account
	        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
	                signUpRequest.getEmail(), signUpRequest.getPassword());

	        user.setPassword(passwordEncoder.encode(user.getPassword()));

	        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
	                .orElseThrow(() -> new AppException("User Role not set."));

	        user.setRoles(Collections.singleton(userRole));

	        User result = userRepository.save(user);

	        URI location = ServletUriComponentsBuilder
	                .fromCurrentContextPath().path("/api/users/{username}")
	                .buildAndExpand(result.getUsername()).toUri();

	        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
	}
}
