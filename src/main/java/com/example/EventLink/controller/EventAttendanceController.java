package com.example.EventLink.controller;

import com.example.EventLink.entity.EventAttendance;
import com.example.EventLink.entity.EventAttendance.AttendanceStatus;
import com.example.EventLink.entity.EventEntity;
import com.example.EventLink.entity.UserEntity;
import com.example.EventLink.service.EventAttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events/attendance")
public class EventAttendanceController {
    
    private final EventAttendanceService eventAttendanceService;
    
    public EventAttendanceController(EventAttendanceService eventAttendanceService) {
        this.eventAttendanceService = eventAttendanceService;
    }
    
    /**
     * Register or update user attendance for an event
     * POST /api/events/attendance
     */
    @PostMapping
    public ResponseEntity<EventAttendance> registerAttendance(
            @RequestParam Long userId,
            @RequestParam Long eventId,
            @RequestParam AttendanceStatus status) {
        
        try {
            EventAttendance attendance = eventAttendanceService.registerAttendance(userId, eventId, status);
            return ResponseEntity.status(HttpStatus.CREATED).body(attendance);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Get user's attendance for a specific event
     * GET /api/events/attendance?userId=1&eventId=2
     */
    @GetMapping
    public ResponseEntity<EventAttendance> getUserEventAttendance(
            @RequestParam Long userId,
            @RequestParam Long eventId) {
        
        try {
            Optional<EventAttendance> attendance = eventAttendanceService.getUserEventAttendance(userId, eventId);
            return attendance.map(ResponseEntity::ok)
                           .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Remove user attendance for an event
     * DELETE /api/events/attendance?userId=1&eventId=2
     */
    @DeleteMapping
    public ResponseEntity<Void> removeAttendance(
            @RequestParam Long userId,
            @RequestParam Long eventId) {
        
        try {
            eventAttendanceService.removeAttendance(userId, eventId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Get all events user is attending with specific status
     * GET /api/events/attendance/user/{userId}/events?status=ATTENDING
     */
    @GetMapping("/user/{userId}/events")
    public ResponseEntity<List<EventEntity>> getUserEventsWithStatus(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "ATTENDING") AttendanceStatus status) {
        
        try {
            List<EventEntity> events = eventAttendanceService.getUserEventsWithStatus(userId, status);
            return ResponseEntity.ok(events);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Get all users attending an event with specific status
     * GET /api/events/attendance/event/{eventId}/users?status=ATTENDING
     */
    @GetMapping("/event/{eventId}/users")
    public ResponseEntity<List<UserEntity>> getEventAttendeesWithStatus(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "ATTENDING") AttendanceStatus status) {
        
        try {
            List<UserEntity> users = eventAttendanceService.getEventAttendeesWithStatus(eventId, status);
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Get all attendances for a user
     * GET /api/events/attendance/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EventAttendance>> getUserAttendances(@PathVariable Long userId) {
        try {
            List<EventAttendance> attendances = eventAttendanceService.getUserAttendances(userId);
            return ResponseEntity.ok(attendances);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Get all attendances for an event
     * GET /api/events/attendance/event/{eventId}
     */
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventAttendance>> getEventAttendances(@PathVariable Long eventId) {
        try {
            List<EventAttendance> attendances = eventAttendanceService.getEventAttendances(eventId);
            return ResponseEntity.ok(attendances);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Get attendee count for an event
     * GET /api/events/attendance/event/{eventId}/count?status=ATTENDING
     */
    @GetMapping("/event/{eventId}/count")
    public ResponseEntity<Long> getEventAttendeeCount(
            @PathVariable Long eventId,
            @RequestParam(required = false) AttendanceStatus status) {
        
        try {
            long count;
            if (status != null) {
                count = eventAttendanceService.getEventAttendeeCountByStatus(eventId, status);
            } else {
                count = eventAttendanceService.getEventAttendeeCount(eventId);
            }
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}