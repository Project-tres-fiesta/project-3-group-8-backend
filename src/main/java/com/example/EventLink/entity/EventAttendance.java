package com.example.EventLink.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_attendances", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "event_id"}))
public class EventAttendance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;
    
    @Column(name = "attendance_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;
    
    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Enum for attendance status
    public enum AttendanceStatus {
        ATTENDING,
        NOT_ATTENDING,
        MAYBE,
        INTERESTED
    }
    
    // Default constructor
    public EventAttendance() {}
    
    // Constructor for creating attendance record
    public EventAttendance(UserEntity user, EventEntity event, AttendanceStatus attendanceStatus) {
        this.user = user;
        this.event = event;
        this.attendanceStatus = attendanceStatus;
        this.registeredAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserEntity getUser() {
        return user;
    }
    
    public void setUser(UserEntity user) {
        this.user = user;
    }
    
    public EventEntity getEvent() {
        return event;
    }
    
    public void setEvent(EventEntity event) {
        this.event = event;
    }
    
    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }
    
    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }
    
    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @PrePersist
    protected void onCreate() {
        registeredAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "EventAttendance{" +
                "id=" + id +
                ", userId=" + (user != null ? user.getUserId() : null) +
                ", eventId=" + (event != null ? event.getId() : null) +
                ", attendanceStatus=" + attendanceStatus +
                ", registeredAt=" + registeredAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}