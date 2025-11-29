package com.example.EventLink.repository;

import com.example.EventLink.entity.EventAttendance;
import com.example.EventLink.entity.EventAttendance.AttendanceStatus;
import com.example.EventLink.entity.EventEntity;
import com.example.EventLink.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventAttendanceRepository extends JpaRepository<EventAttendance, Long> {
    
    /**
     * Find attendance record by user and event
     */
    Optional<EventAttendance> findByUserAndEvent(UserEntity user, EventEntity event);
    
    /**
     * Find all attendances for a specific user
     */
    List<EventAttendance> findByUser(UserEntity user);
    
    /**
     * Find all attendances for a specific event
     */
    List<EventAttendance> findByEvent(EventEntity event);
    
    /**
     * Find attendances by user and status
     */
    List<EventAttendance> findByUserAndAttendanceStatus(UserEntity user, AttendanceStatus status);
    
    /**
     * Find attendances by event and status
     */
    List<EventAttendance> findByEventAndAttendanceStatus(EventEntity event, AttendanceStatus status);
    
    /**
     * Count total attendees for an event
     */
    @Query("SELECT COUNT(ea) FROM EventAttendance ea WHERE ea.event = :event")
    long countByEvent(@Param("event") EventEntity event);
    
    /**
     * Count attendees by status for an event
     */
    @Query("SELECT COUNT(ea) FROM EventAttendance ea WHERE ea.event = :event AND ea.attendanceStatus = :status")
    long countByEventAndAttendanceStatus(@Param("event") EventEntity event, @Param("status") AttendanceStatus status);
    
    /**
     * Find events user is attending
     */
    @Query("SELECT ea.event FROM EventAttendance ea WHERE ea.user = :user AND ea.attendanceStatus = :status")
    List<EventEntity> findEventsByUserAndStatus(@Param("user") UserEntity user, @Param("status") AttendanceStatus status);
    
    /**
     * Find users attending an event
     */
    @Query("SELECT ea.user FROM EventAttendance ea WHERE ea.event = :event AND ea.attendanceStatus = :status")
    List<UserEntity> findUsersByEventAndStatus(@Param("event") EventEntity event, @Param("status") AttendanceStatus status);
    
    /**
     * Check if user is attending event
     */
    @Query("SELECT CASE WHEN COUNT(ea) > 0 THEN true ELSE false END FROM EventAttendance ea WHERE ea.user = :user AND ea.event = :event AND ea.attendanceStatus = :status")
    boolean existsByUserAndEventAndAttendanceStatus(@Param("user") UserEntity user, @Param("event") EventEntity event, @Param("status") AttendanceStatus status);
    
    /**
     * Delete attendance record by user and event
     */
    void deleteByUserAndEvent(UserEntity user, EventEntity event);
}