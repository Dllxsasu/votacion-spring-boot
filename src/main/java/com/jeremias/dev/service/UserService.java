package com.jeremias.dev.service;

import org.springframework.stereotype.Service;

import com.jeremias.dev.exception.ResourceNotFoundException;
import com.jeremias.dev.models.User;
import com.jeremias.dev.payload.response.UserProfile;
import com.jeremias.dev.payload.response.UserSummary;
import com.jeremias.dev.repository.PollRepository;
import com.jeremias.dev.repository.UserRepository;
import com.jeremias.dev.repository.VoteRepository;
import com.jeremias.dev.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
    private final PollRepository pollRepository;
    private final VoteRepository voteRepository;
    
	public boolean isAvalaibleUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	public boolean isAvalaibleEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	public UserProfile getUserProfile(String username) {
		
		 User user = userRepository.findByUsername(username)
	                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

	        long pollCount = pollRepository.countByCreatedBy(user.getId());
	        long voteCount = voteRepository.countByUserId(user.getId());

	       return new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), pollCount, voteCount);
	}
	
	public	UserSummary getCurrentUser(UserPrincipal currentUser) {
		 return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());	  
	}

}
