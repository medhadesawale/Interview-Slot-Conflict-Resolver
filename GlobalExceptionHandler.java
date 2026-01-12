package com.interview.scheduler.exception;

import com.interview.scheduler.dto.ConflictResponse;
import com.interview.scheduler.dto.InterviewResponse;
import com.interview.scheduler.model.Interview;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ConflictResponse> handleConflictException(ConflictException ex) {
        List<InterviewResponse> conflicts = ex.getConflictingInterviews().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        ConflictResponse response = new ConflictResponse();
        response.setHasConflict(true);
        response.setMessage(ex.getMessage());
        response.setConflictingInterviews(conflicts);
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Not Found");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation Failed");
        response.put("validationErrors", errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal Server Error");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
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