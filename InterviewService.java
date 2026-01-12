package com.interview.scheduler.service;

import com.interview.scheduler.dto.ConflictResponse;
import com.interview.scheduler.dto.InterviewRequest;
import com.interview.scheduler.dto.InterviewResponse;
import com.interview.scheduler.model.InterviewStatus;

import java.util.List;

public interface InterviewService {
    
    /**
     * Schedule a new interview with conflict detection
     */
    InterviewResponse scheduleInterview(InterviewRequest request);
    
    /**
     * Check for conflicts without scheduling
     */
    ConflictResponse checkConflicts(InterviewRequest request);
    
    /**
     * Get all interviews
     */
    List<InterviewResponse> getAllInterviews();
    
    /**
     * Get interview by ID
     */
    InterviewResponse getInterviewById(Long id);
    
    /**
     * Update interview status
     */
    InterviewResponse updateInterviewStatus(Long id, InterviewStatus status);
    
    /**
     * Cancel an interview
     */
    InterviewResponse cancelInterview(Long id);
    
    /**
     * Get upcoming interviews
     */
    List<InterviewResponse> getUpcomingInterviews();
    
    /**
     * Get interviews by interviewer email
     */
    List<InterviewResponse> getInterviewsByInterviewer(String interviewerEmail);
    
    /**
     * Get interviews by candidate email
     */
    List<InterviewResponse> getInterviewsByCandidate(String candidateEmail);
}