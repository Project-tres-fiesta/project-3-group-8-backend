package com.example.EventLink.service;

import com.example.EventLink.entity.EventAttendance;
import com.example.EventLink.entity.EventAttendance.AttendanceStatus;
import com.example.EventLink.entity.EventEntity;
import com.example.EventLink.entity.UserEntity;
import com.example.EventLink.repository.EventAttendanceRepository;
import com.example.EventLink.repository.EventRepository;
import com.example.EventLink.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EventAttendanceService {
    
    private final EventAttendanceRepository eventAttendanceRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    
    public EventAttendanceService(EventAttendanceRepository eventAttendanceRepository,
                                 UserRepository userRepository,
                                 EventRepository eventRepository) {
        this.eventAttendanceRepository = eventAttendanceRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }
    
    /**
     * Register user attendance for an event
     */
    @Transactional
    public EventAttendance registerAttendance(Long userId, Long eventId, AttendanceStatus status) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        // Check if attendance already exists
        Optional<EventAttendance> existingAttendance = eventAttendanceRepository.findByUserAndEvent(user, event);
        
        if (existingAttendance.isPresent()) {
            // Update existing attendance
            EventAttendance attendance = existingAttendance.get();
            attendance.setAttendanceStatus(status);
            return eventAttendanceRepository.save(attendance);
        } else {
            // Create new attendance
            EventAttendance attendance = new EventAttendance(user, event, status);
            return eventAttendanceRepository.save(attendance);
        }
    }
    
    /**
     * Update attendance status
     */
    @Transactional
    public EventAttendance updateAttendanceStatus(Long userId, Long eventId, AttendanceStatus status) {
        return registerAttendance(userId, eventId, status);
    }
    
    /**
     * Remove user attendance for an event
     */
    @Transactional
    public void removeAttendance(Long userId, Long eventId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        eventAttendanceRepository.deleteByUserAndEvent(user, event);
    }
    
    /**
     * Get user's attendance for a specific event
     */
    public Optional<EventAttendance> getUserEventAttendance(Long userId, Long eventId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        return eventAttendanceRepository.findByUserAndEvent(user, event);
    }
    
    /**
     * Get all events user is attending with specific status
     */
    public List<EventEntity> getUserEventsWithStatus(Long userId, AttendanceStatus status) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        return eventAttendanceRepository.findEventsByUserAndStatus(user, status);
    }
    
    /**
     * Get all users attending an event with specific status
     */
    public List<UserEntity> getEventAttendeesWithStatus(Long eventId, AttendanceStatus status) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        return eventAttendanceRepository.findUsersByEventAndStatus(event, status);
    }
    
    /**
     * Get all attendances for a user
     */
    public List<EventAttendance> getUserAttendances(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        return eventAttendanceRepository.findByUser(user);
    }
    
    /**
     * Get all attendances for an event
     */
    public List<EventAttendance> getEventAttendances(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        return eventAttendanceRepository.findByEvent(event);
    }
    
    /**
     * Count total attendees for an event
     */
    public long getEventAttendeeCount(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        return eventAttendanceRepository.countByEvent(event);
    }
    
    /**
     * Count attendees by status for an event
     */
    public long getEventAttendeeCountByStatus(Long eventId, AttendanceStatus status) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        return eventAttendanceRepository.countByEventAndAttendanceStatus(event, status);
    }
    
    /**
     * Check if user is attending event with specific status
     */
    public boolean isUserAttendingEventWithStatus(Long userId, Long eventId, AttendanceStatus status) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        return eventAttendanceRepository.existsByUserAndEventAndAttendanceStatus(user, event, status);
    }
}