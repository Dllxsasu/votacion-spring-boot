package com.jeremias.dev.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jeremias.dev.models.Poll;
import com.jeremias.dev.payload.PollRequest;
import com.jeremias.dev.payload.VoteRequest;
import com.jeremias.dev.payload.response.ApiResponse;
import com.jeremias.dev.payload.response.PagedResponse;
import com.jeremias.dev.payload.response.PollResponse;
import com.jeremias.dev.security.CurrentUser;
import com.jeremias.dev.security.UserPrincipal;
import com.jeremias.dev.service.PollService;
import com.jeremias.dev.utils.AppConstants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/polls")
public class PollController {

	    
	    private final PollService pollService;

	    

	    @GetMapping
	    public PagedResponse<PollResponse> getPolls(@CurrentUser UserPrincipal currentUser,
	                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
	                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
	        return pollService.getAllPolls(currentUser, page, size);
	    }

	    @PostMapping
	    @PreAuthorize("hasRole('USER')")
	    public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest) {
	        Poll poll = pollService.createPoll(pollRequest);

	        URI location = ServletUriComponentsBuilder
	                .fromCurrentRequest().path("/{pollId}")
	                .buildAndExpand(poll.getId()).toUri();

	        return ResponseEntity.created(location)
	                .body(new ApiResponse(true, "Encuestra creada satisfactoriamente"));
	    }

	    @GetMapping("/{pollId}")
	    public PollResponse getPollById(@CurrentUser UserPrincipal currentUser,
	                                    @PathVariable Long pollId) {
	        return pollService.getPollById(pollId, currentUser);
	    }

	    @PostMapping("/{pollId}/votes")
	    @PreAuthorize("hasRole('USER')")
	    public PollResponse castVote(@CurrentUser UserPrincipal currentUser,
	                         @PathVariable Long pollId,
	                         @Valid @RequestBody VoteRequest voteRequest) {
	        return pollService.castVoteAndGetUpdatedPoll(pollId, voteRequest, currentUser);
	    }
}
