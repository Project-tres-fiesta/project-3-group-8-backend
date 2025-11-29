package com.example.EventLink.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class EventAttendanceTest {
    
    private UserEntity testUser;
    private EventEntity testEvent;
    private EventAttendance eventAttendance;
    
    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new UserEntity();
        testUser.setUserId(1L);
        testUser.setUserName("Test User");
        testUser.setUserEmail("test@example.com");
        testUser.setProfilePicture("profile.jpg");
        
        // Create test event
        testEvent = new EventEntity();
        testEvent.setId(1L);
        testEvent.setExternalEventId("ext-123");
        testEvent.setTitle("Test Event");
        testEvent.setDescription("A test event");
        testEvent.setVenueName("Test Venue");
        testEvent.setVenueCity("Test City");
        testEvent.setEventDate(LocalDate.of(2025, 12, 25));
        testEvent.setEventTime(LocalTime.of(19, 0));
        testEvent.setCategory("Music");
        testEvent.setExternalSource("ticketmaster");
        
        // Create test attendance
        eventAttendance = new EventAttendance(testUser, testEvent, EventAttendance.AttendanceStatus.ATTENDING);
    }
    
    @Test
    void testEventAttendanceConstructor() {
        assertNotNull(eventAttendance);
        assertEquals(testUser, eventAttendance.getUser());
        assertEquals(testEvent, eventAttendance.getEvent());
        assertEquals(EventAttendance.AttendanceStatus.ATTENDING, eventAttendance.getAttendanceStatus());
        assertNotNull(eventAttendance.getRegisteredAt());
        assertNotNull(eventAttendance.getUpdatedAt());
    }
    
    @Test
    void testDefaultConstructor() {
        EventAttendance attendance = new EventAttendance();
        assertNotNull(attendance);
        assertNull(attendance.getId());
        assertNull(attendance.getUser());
        assertNull(attendance.getEvent());
        assertNull(attendance.getAttendanceStatus());
    }
    
    @Test
    void testSettersAndGetters() {
        EventAttendance attendance = new EventAttendance();
        
        attendance.setId(2L);
        assertEquals(2L, attendance.getId());
        
        attendance.setUser(testUser);
        assertEquals(testUser, attendance.getUser());
        
        attendance.setEvent(testEvent);
        assertEquals(testEvent, attendance.getEvent());
        
        attendance.setAttendanceStatus(EventAttendance.AttendanceStatus.MAYBE);
        assertEquals(EventAttendance.AttendanceStatus.MAYBE, attendance.getAttendanceStatus());
        
        LocalDateTime now = LocalDateTime.now();
        attendance.setRegisteredAt(now);
        assertEquals(now, attendance.getRegisteredAt());
        
        attendance.setUpdatedAt(now);
        assertEquals(now, attendance.getUpdatedAt());
    }
    
    @Test
    void testAttendanceStatusEnum() {
        EventAttendance.AttendanceStatus[] statuses = EventAttendance.AttendanceStatus.values();
        
        assertEquals(4, statuses.length);
        assertTrue(containsStatus(statuses, EventAttendance.AttendanceStatus.ATTENDING));
        assertTrue(containsStatus(statuses, EventAttendance.AttendanceStatus.NOT_ATTENDING));
        assertTrue(containsStatus(statuses, EventAttendance.AttendanceStatus.MAYBE));
        assertTrue(containsStatus(statuses, EventAttendance.AttendanceStatus.INTERESTED));
    }
    
    @Test
    void testSetAttendanceStatusUpdatesTime() throws InterruptedException {
        LocalDateTime originalUpdatedAt = eventAttendance.getUpdatedAt();
        
        // Wait a small amount to ensure time difference
        Thread.sleep(10);
        
        eventAttendance.setAttendanceStatus(EventAttendance.AttendanceStatus.NOT_ATTENDING);
        
        assertNotEquals(originalUpdatedAt, eventAttendance.getUpdatedAt());
        assertTrue(eventAttendance.getUpdatedAt().isAfter(originalUpdatedAt));
        assertEquals(EventAttendance.AttendanceStatus.NOT_ATTENDING, eventAttendance.getAttendanceStatus());
    }
    
    @Test
    void testToString() {
        String toString = eventAttendance.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("EventAttendance"));
        assertTrue(toString.contains("userId=" + testUser.getUserId()));
        assertTrue(toString.contains("eventId=" + testEvent.getId()));
        assertTrue(toString.contains("attendanceStatus=" + EventAttendance.AttendanceStatus.ATTENDING));
    }
    
    @Test
    void testToStringWithNullUserAndEvent() {
        EventAttendance attendance = new EventAttendance();
        attendance.setAttendanceStatus(EventAttendance.AttendanceStatus.INTERESTED);
        
        String toString = attendance.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("EventAttendance"));
        assertTrue(toString.contains("userId=null"));
        assertTrue(toString.contains("eventId=null"));
        assertTrue(toString.contains("attendanceStatus=" + EventAttendance.AttendanceStatus.INTERESTED));
    }
    
    // Helper method to check if status exists in array
    private boolean containsStatus(EventAttendance.AttendanceStatus[] statuses, EventAttendance.AttendanceStatus status) {
        for (EventAttendance.AttendanceStatus s : statuses) {
            if (s == status) {
                return true;
            }
        }
        return false;
    }
}