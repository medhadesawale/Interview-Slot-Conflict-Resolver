package com.interview.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class InterviewRequest {
    
    @NotBlank(message = "Candidate name is required")
    @Size(min = 2, max = 100, message = "Candidate name must be between 2 and 100 characters")
    private String candidateName;
    
    @NotBlank(message = "Candidate email is required")
    @Email(message = "Invalid candidate email format")
    private String candidateEmail;
    
    @NotBlank(message = "Interviewer name is required")
    @Size(min = 2, max = 100, message = "Interviewer name must be between 2 and 100 characters")
    private String interviewerName;
    
    @NotBlank(message = "Interviewer email is required")
    @Email(message = "Invalid interviewer email format")
    private String interviewerEmail;
    
    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    
    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
    
    @NotBlank(message = "Position is required")
    @Size(min = 2, max = 100, message = "Position must be between 2 and 100 characters")
    private String position;
    
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
    
    // Constructors
    public InterviewRequest() {
    }
    
    public InterviewRequest(String candidateName, String candidateEmail, String interviewerName, 
                           String interviewerEmail, LocalDateTime startTime, LocalDateTime endTime, 
                           String position, String notes) {
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.interviewerName = interviewerName;
        this.interviewerEmail = interviewerEmail;
        this.startTime = startTime;
        this.endTime = endTime;
        this.position = position;
        this.notes = notes;
    }
    
    // Getters and Setters
    public String getCandidateName() {
        return candidateName;
    }
    
    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }
    
    public String getCandidateEmail() {
        return candidateEmail;
    }
    
    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }
    
    public String getInterviewerName() {
        return interviewerName;
    }
    
    public void setInterviewerName(String interviewerName) {
        this.interviewerName = interviewerName;
    }
    
    public String getInterviewerEmail() {
        return interviewerEmail;
    }
    
    public void setInterviewerEmail(String interviewerEmail) {
        this.interviewerEmail = interviewerEmail;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    // Validation method
    @AssertTrue(message = "End time must be after start time")
    public boolean isEndTimeAfterStartTime() {
        if (startTime == null || endTime == null) {
            return true;
        }
        return endTime.isAfter(startTime);
    }
}