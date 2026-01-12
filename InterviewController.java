package com.interview.scheduler.controller;

import com.interview.scheduler.dto.ConflictResponse;
import com.interview.scheduler.dto.InterviewRequest;
import com.interview.scheduler.dto.InterviewResponse;
import com.interview.scheduler.model.InterviewStatus;
import com.interview.scheduler.service.InterviewService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@CrossOrigin(origins = "*")
public class InterviewController {
    
    private static final Logger log = LoggerFactory.getLogger(InterviewController.class);
    
    private final InterviewService interviewService;
    
    @Autowired
    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }
    
    @PostMapping
    public ResponseEntity<InterviewResponse> scheduleInterview(@Valid @RequestBody InterviewRequest request) {
        log.info("Received request to schedule interview for candidate: {}", request.getCandidateEmail());
        InterviewResponse response = interviewService.scheduleInterview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/check-conflicts")
    public ResponseEntity<ConflictResponse> checkConflicts(@Valid @RequestBody InterviewRequest request) {
        log.info("Received request to check conflicts for candidate: {}", request.getCandidateEmail());
        ConflictResponse response = interviewService.checkConflicts(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<InterviewResponse>> getAllInterviews() {
        log.info("Received request to fetch all interviews");
        List<InterviewResponse> interviews = interviewService.getAllInterviews();
        return ResponseEntity.ok(interviews);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InterviewResponse> getInterviewById(@PathVariable Long id) {
        log.info("Received request to fetch interview with ID: {}", id);
        InterviewResponse response = interviewService.getInterviewById(id);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<InterviewResponse> updateInterviewStatus(@PathVariable Long id, @RequestParam InterviewStatus status) {
        log.info("Received request to update interview {} status to {}", id, status);
        InterviewResponse response = interviewService.updateInterviewStatus(id, status);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<InterviewResponse> cancelInterview(@PathVariable Long id) {
        log.info("Received request to cancel interview with ID: {}", id);
        InterviewResponse response = interviewService.cancelInterview(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<InterviewResponse>> getUpcomingInterviews() {
        log.info("Received request to fetch upcoming interviews");
        List<InterviewResponse> interviews = interviewService.getUpcomingInterviews();
        return ResponseEntity.ok(interviews);
    }
    
    @GetMapping("/interviewer/{email}")
    public ResponseEntity<List<InterviewResponse>> getInterviewsByInterviewer(@PathVariable String email) {
        log.info("Received request to fetch interviews for interviewer: {}", email);
        List<InterviewResponse> interviews = interviewService.getInterviewsByInterviewer(email);
        return ResponseEntity.ok(interviews);
    }
    
    @GetMapping("/candidate/{email}")
    public ResponseEntity<List<InterviewResponse>> getInterviewsByCandidate(@PathVariable String email) {
        log.info("Received request to fetch interviews for candidate: {}", email);
        List<InterviewResponse> interviews = interviewService.getInterviewsByCandidate(email);
        return ResponseEntity.ok(interviews);
    }
}