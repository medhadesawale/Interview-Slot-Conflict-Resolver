package com.interview.scheduler.repository;

import com.interview.scheduler.model.Interview;
import com.interview.scheduler.model.InterviewStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    
    /**
     * Find conflicting interviews for an interviewer with pessimistic locking
     * Used during actual booking to prevent race conditions
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Interview i WHERE " +
           "i.interviewerEmail = :interviewerEmail AND " +
           "i.status != 'CANCELLED' AND " +
           "((i.startTime < :endTime AND i.endTime > :startTime))")
    List<Interview> findInterviewerConflictsWithLock(
            @Param("interviewerEmail") String interviewerEmail,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * Find conflicting interviews for a candidate with pessimistic locking
     * Used during actual booking to prevent race conditions
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Interview i WHERE " +
           "i.candidateEmail = :candidateEmail AND " +
           "i.status != 'CANCELLED' AND " +
           "((i.startTime < :endTime AND i.endTime > :startTime))")
    List<Interview> findCandidateConflictsWithLock(
            @Param("candidateEmail") String candidateEmail,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * Find conflicting interviews for an interviewer WITHOUT locking
     * Used for read-only conflict checking
     */
    @Query("SELECT i FROM Interview i WHERE " +
           "i.interviewerEmail = :interviewerEmail AND " +
           "i.status != 'CANCELLED' AND " +
           "((i.startTime < :endTime AND i.endTime > :startTime))")
    List<Interview> findInterviewerConflicts(
            @Param("interviewerEmail") String interviewerEmail,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * Find conflicting interviews for a candidate WITHOUT locking
     * Used for read-only conflict checking
     */
    @Query("SELECT i FROM Interview i WHERE " +
           "i.candidateEmail = :candidateEmail AND " +
           "i.status != 'CANCELLED' AND " +
           "((i.startTime < :endTime AND i.endTime > :startTime))")
    List<Interview> findCandidateConflicts(
            @Param("candidateEmail") String candidateEmail,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * Find all interviews by status
     */
    List<Interview> findByStatus(InterviewStatus status);
    
    /**
     * Find all interviews for a specific interviewer
     */
    List<Interview> findByInterviewerEmailOrderByStartTimeDesc(String interviewerEmail);
    
    /**
     * Find all interviews for a specific candidate
     */
    List<Interview> findByCandidateEmailOrderByStartTimeDesc(String candidateEmail);
    
    /**
     * Find upcoming interviews (status not cancelled and start time in future)
     */
    @Query("SELECT i FROM Interview i WHERE " +
           "i.status != 'CANCELLED' AND " +
           "i.startTime > :currentTime " +
           "ORDER BY i.startTime ASC")
    List<Interview> findUpcomingInterviews(@Param("currentTime") LocalDateTime currentTime);
}