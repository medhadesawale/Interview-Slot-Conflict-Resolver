package com.interview.scheduler.exception;

import com.interview.scheduler.model.Interview;

import java.util.List;

public class ConflictException extends RuntimeException {

    private final List<Interview> conflictingInterviews;

    public ConflictException(String message, List<Interview> conflictingInterviews) {
        super(message);
        this.conflictingInterviews = conflictingInterviews;
    }

    public List<Interview> getConflictingInterviews() {
        return conflictingInterviews;
    }
}
