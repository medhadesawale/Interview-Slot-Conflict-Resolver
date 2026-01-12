package com.interview.scheduler.service.impl;

import com.interview.scheduler.dto.ConflictResponse;
import com.interview.scheduler.dto.InterviewRequest;
import com.interview.scheduler.dto.InterviewResponse;
import com.interview.scheduler.exception.ConflictException;
import com.interview.scheduler.exception.ResourceNotFoundException;
import com.interview.scheduler.model.Interview;
import com.interview.scheduler.model.InterviewStatus;
import com.interview.scheduler.repository.InterviewRepository;
import com.interview.scheduler.service.InterviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewServiceImpl implements InterviewService {
    
    private static final Logger log = LoggerFactory.getLogger(InterviewServiceImpl.class);
    
    private final InterviewRepository interviewRepository;
    
    @Autowired
    public InterviewServiceImpl(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }
    
    @Override
    @Transactional
    public InterviewResponse scheduleInterview(InterviewRequest request) {
        log.info("Scheduling interview for candidate: {} with interviewer: {}", 
                 request.getCandidateEmail(), request.getInterviewerEmail());
        
        // Use locking version for actual booking to prevent race conditions
        List<Interview> conflicts = findAllConflictsWithLock(request);
        
        if (!conflicts.isEmpty()) {
            log.warn("Conflicts found for interview scheduling");
            throw new ConflictException(
                "Interview slot conflicts detected. Please choose a different time.", 
                conflicts
            );
        }
        
        Interview interview = new Interview();
        interview.setCandidateName(request.getCandidateName());
        interview.setCandidateEmail(request.getCandidateEmail());
        interview.setInterviewerName(request.getInterviewerName());
        interview.setInterviewerEmail(request.getInterviewerEmail());
        interview.setStartTime(request.getStartTime());
        interview.setEndTime(request.getEndTime());
        interview.setPosition(request.getPosition());
        interview.setNotes(request.getNotes());
        interview.setStatus(InterviewStatus.PENDING);
        
        Interview savedInterview = interviewRepository.save(interview);
        log.info("Interview scheduled successfully with ID: {}", savedInterview.getId());
        
        return convertToResponse(savedInterview);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ConflictResponse checkConflicts(InterviewRequest request) {
        log.info("Checking conflicts for candidate: {} with interviewer: {}", 
                 request.getCandidateEmail(), request.getInterviewerEmail());
        
        // Use non-locking version for read-only checking
        List<Interview> conflicts = findAllConflicts(request);
        
        List<InterviewResponse> conflictResponses = conflicts.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        boolean hasConflict = !conflicts.isEmpty();
        String message = hasConflict 
            ? "Conflicts detected. Please choose a different time slot."
            : "No conflicts found. This slot is available.";
        
        List<String> suggestedSlots = hasConflict 
            ? generateSuggestedSlots(request.getStartTime(), request.getEndTime())
            : new ArrayList<>();
        
        ConflictResponse response = new ConflictResponse();
        response.setHasConflict(hasConflict);
        response.setMessage(message);
        response.setConflictingInterviews(conflictResponses);
        response.setSuggestedSlots(suggestedSlots);
        
        return response;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InterviewResponse> getAllInterviews() {
        log.info("Fetching all interviews");
        return interviewRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public InterviewResponse getInterviewById(Long id) {
        log.info("Fetching interview with ID: {}", id);
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Interview not found with ID: " + id
                ));
        return convertToResponse(interview);
    }
    
    @Override
    @Transactional
    public InterviewResponse updateInterviewStatus(Long id, InterviewStatus status) {
        log.info("Updating interview status for ID: {} to {}", id, status);
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Interview not found with ID: " + id
                ));
        
        interview.setStatus(status);
        Interview updatedInterview = interviewRepository.save(interview);
        
        return convertToResponse(updatedInterview);
    }
    
    @Override
    @Transactional
    public InterviewResponse cancelInterview(Long id) {
        log.info("Cancelling interview with ID: {}", id);
        return updateInterviewStatus(id, InterviewStatus.CANCELLED);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InterviewResponse> getUpcomingInterviews() {
        log.info("Fetching upcoming interviews");
        return interviewRepository.findUpcomingInterviews(LocalDateTime.now()).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InterviewResponse> getInterviewsByInterviewer(String interviewerEmail) {
        log.info("Fetching interviews for interviewer: {}", interviewerEmail);
        return interviewRepository.findByInterviewerEmailOrderByStartTimeDesc(interviewerEmail).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<InterviewResponse> getInterviewsByCandidate(String candidateEmail) {
        log.info("Fetching interviews for candidate: {}", candidateEmail);
        return interviewRepository.findByCandidateEmailOrderByStartTimeDesc(candidateEmail).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // Helper Methods
    
    /**
     * Find conflicts WITHOUT locking - for read-only operations
     */
    private List<Interview> findAllConflicts(InterviewRequest request) {
        List<Interview> conflicts = new ArrayList<>();
        
        List<Interview> interviewerConflicts = interviewRepository.findInterviewerConflicts(
            request.getInterviewerEmail(),
            request.getStartTime(),
            request.getEndTime()
        );
        conflicts.addAll(interviewerConflicts);
        
        List<Interview> candidateConflicts = interviewRepository.findCandidateConflicts(
            request.getCandidateEmail(),
            request.getStartTime(),
            request.getEndTime()
        );
        conflicts.addAll(candidateConflicts);
        
        return conflicts;
    }
    
    /**
     * Find conflicts WITH locking - for actual booking to prevent race conditions
     */
    private List<Interview> findAllConflictsWithLock(InterviewRequest request) {
        List<Interview> conflicts = new ArrayList<>();
        
        List<Interview> interviewerConflicts = interviewRepository.findInterviewerConflictsWithLock(
            request.getInterviewerEmail(),
            request.getStartTime(),
            request.getEndTime()
        );
        conflicts.addAll(interviewerConflicts);
        
        List<Interview> candidateConflicts = interviewRepository.findCandidateConflictsWithLock(
            request.getCandidateEmail(),
            request.getStartTime(),
            request.getEndTime()
        );
        conflicts.addAll(candidateConflicts);
        
        return conflicts;
    }
    
    private List<String> generateSuggestedSlots(LocalDateTime requestedStart, LocalDateTime requestedEnd) {
        List<String> suggestions = new ArrayList<>();
        long durationMinutes = java.time.Duration.between(requestedStart, requestedEnd).toMinutes();
        
        LocalDateTime slot1 = requestedStart.plusHours(2);
        LocalDateTime slot2 = requestedStart.plusHours(4);
        LocalDateTime slot3 = requestedStart.plusDays(1);
        
        suggestions.add(formatTimeSlot(slot1, slot1.plusMinutes(durationMinutes)));
        suggestions.add(formatTimeSlot(slot2, slot2.plusMinutes(durationMinutes)));
        suggestions.add(formatTimeSlot(slot3, slot3.plusMinutes(durationMinutes)));
        
        return suggestions;
    }
    
    private String formatTimeSlot(LocalDateTime start, LocalDateTime end) {
        return String.format("%s to %s", start.toString(), end.toString());
    }
    
    private InterviewResponse convertToResponse(Interview interview) {
        InterviewResponse response = new InterviewResponse();
        response.setId(interview.getId());
        response.setCandidateName(interview.getCandidateName());
        response.setCandidateEmail(interview.getCandidateEmail());
        response.setInterviewerName(interview.getInterviewerName());
        response.setInterviewerEmail(interview.getInterviewerEmail());
        response.setStartTime(interview.getStartTime());
        response.setEndTime(interview.getEndTime());
        response.setStatus(interview.getStatus());
        response.setNotes(interview.getNotes());
        response.setPosition(interview.getPosition());
        response.setCreatedAt(interview.getCreatedAt());
        response.setUpdatedAt(interview.getUpdatedAt());
        return response;
    }
}