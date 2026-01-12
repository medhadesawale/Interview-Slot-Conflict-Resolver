package com.interview.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.interview.scheduler.model.InterviewStatus;

import java.time.LocalDateTime;

public class InterviewResponse {
    
    private Long id;
    private String candidateName;
    private String candidateEmail;
    private String interviewerName;
    private String interviewerEmail;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
    
    private InterviewStatus status;
    private String notes;
    private String position;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Constructors
    public InterviewResponse() {
    }
    
    public InterviewResponse(Long id, String candidateName, String candidateEmail, 
                            String interviewerName, String interviewerEmail, 
                            LocalDateTime startTime, LocalDateTime endTime, 
                            InterviewStatus status, String notes, String position,
                            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.interviewerName = interviewerName;
        this.interviewerEmail = interviewerEmail;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.notes = notes;
        this.position = position;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public InterviewStatus getStatus() {
        return status;
    }
    
    public void setStatus(InterviewStatus status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}