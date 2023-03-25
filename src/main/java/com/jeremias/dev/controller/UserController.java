package com.jeremias.dev.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeremias.dev.exception.ResourceNotFoundException;
import com.jeremias.dev.models.User;
import com.jeremias.dev.payload.response.PagedResponse;
import com.jeremias.dev.payload.response.PollResponse;
import com.jeremias.dev.payload.response.UserIdentityAvailability;
import com.jeremias.dev.payload.response.UserProfile;
import com.jeremias.dev.payload.response.UserSummary;
import com.jeremias.dev.repository.PollRepository;
import com.jeremias.dev.repository.UserRepository;
import com.jeremias.dev.repository.VoteRepository;
import com.jeremias.dev.security.CurrentUser;
import com.jeremias.dev.security.UserPrincipal;
import com.jeremias.dev.service.PollService;
import com.jeremias.dev.service.UserService;
import com.jeremias.dev.utils.AppConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
	

    private final PollService pollService;
    private final UserService userService;
    
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
       return userService.getCurrentUser(currentUser);
    }

    @GetMapping("/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {

        return new UserIdentityAvailability(!userService.isAvalaibleUsername(username));
    }

    @GetMapping("/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
  
        return new UserIdentityAvailability(!userService.isAvalaibleEmail(email));
    }

    @GetMapping("/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        
    	return userService.getUserProfile(username);
    	
    }

    @GetMapping("/users/{username}/polls")
    public PagedResponse<PollResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
                                                         @CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getPollsCreatedBy(username, currentUser, page, size);
    }


    @GetMapping("/users/{username}/votes")
    public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
                                                       @CurrentUser UserPrincipal currentUser,
                                                       @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                       @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return pollService.getPollsVotedBy(username, currentUser, page, size);
    }
    
    
}
