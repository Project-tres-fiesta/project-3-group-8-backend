package com.example.EventLink.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EventEntityTest {

    @Test
    void testEventEntityCreation() {
        // Given
        EventEntity event = new EventEntity("TM123", "Test Concert", "ticketmaster");
        
        // When
        event.setDescription("A great concert event");
        event.setVenueName("Madison Square Garden");
        event.setVenueCity("New York");
        event.setVenueState("NY");
        event.setVenueCountry("US");
        event.setEventDate(LocalDate.of(2024, 12, 25));
        event.setEventTime(LocalTime.of(20, 0));
        event.setMinPrice(new BigDecimal("50.00"));
        event.setMaxPrice(new BigDecimal("250.00"));
        event.setCurrency("USD");
        event.setCategory("Music");
        event.setGenre("Rock");
        event.setImageUrl("https://example.com/image.jpg");
        event.setEventUrl("https://example.com/event");
        
        // Then
        assertNotNull(event);
        assertEquals("TM123", event.getExternalEventId());
        assertEquals("Test Concert", event.getTitle());
        assertEquals("ticketmaster", event.getExternalSource());
        assertEquals("A great concert event", event.getDescription());
        assertEquals("Madison Square Garden", event.getVenueName());
        assertEquals("New York", event.getVenueCity());
        assertEquals("NY", event.getVenueState());
        assertEquals("US", event.getVenueCountry());
        assertEquals(LocalDate.of(2024, 12, 25), event.getEventDate());
        assertEquals(LocalTime.of(20, 0), event.getEventTime());
        assertEquals(new BigDecimal("50.00"), event.getMinPrice());
        assertEquals(new BigDecimal("250.00"), event.getMaxPrice());
        assertEquals("USD", event.getCurrency());
        assertEquals("Music", event.getCategory());
        assertEquals("Rock", event.getGenre());
        assertEquals("https://example.com/image.jpg", event.getImageUrl());
        assertEquals("https://example.com/event", event.getEventUrl());
    }

    @Test
    void testEventEntityDefaultConstructor() {
        // Given & When
        EventEntity event = new EventEntity();
        
        // Then
        assertNotNull(event);
        assertNull(event.getId());
        assertNull(event.getExternalEventId());
        assertNull(event.getTitle());
        assertNull(event.getExternalSource());
    }

    @Test
    void testEventEntityToString() {
        // Given
        EventEntity event = new EventEntity("TM456", "Rock Festival", "ticketmaster");
        event.setVenueName("Central Park");
        event.setEventDate(LocalDate.of(2024, 7, 4));
        event.setEventTime(LocalTime.of(18, 30));
        event.setCategory("Music");
        
        // When
        String result = event.toString();
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("TM456"));
        assertTrue(result.contains("Rock Festival"));
        assertTrue(result.contains("ticketmaster"));
        assertTrue(result.contains("Music"));
    }
}