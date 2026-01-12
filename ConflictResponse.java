package com.interview.scheduler.dto;

import java.util.List;

public class ConflictResponse {
    
    private boolean hasConflict;
    private String message;
    private List<InterviewResponse> conflictingInterviews;
    private List<String> suggestedSlots;
    
    // Constructors
    public ConflictResponse() {
    }
    
    public ConflictResponse(boolean hasConflict, String message, 
                           List<InterviewResponse> conflictingInterviews, 
                           List<String> suggestedSlots) {
        this.hasConflict = hasConflict;
        this.message = message;
        this.conflictingInterviews = conflictingInterviews;
        this.suggestedSlots = suggestedSlots;
    }
    
    // Getters and Setters
    public boolean isHasConflict() {
        return hasConflict;
    }
    
    public void setHasConflict(boolean hasConflict) {
        this.hasConflict = hasConflict;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public List<InterviewResponse> getConflictingInterviews() {
        return conflictingInterviews;
    }
    
    public void setConflictingInterviews(List<InterviewResponse> conflictingInterviews) {
        this.conflictingInterviews = conflictingInterviews;
    }
    
    public List<String> getSuggestedSlots() {
        return suggestedSlots;
    }
    
    public void setSuggestedSlots(List<String> suggestedSlots) {
        this.suggestedSlots = suggestedSlots;
    }
}