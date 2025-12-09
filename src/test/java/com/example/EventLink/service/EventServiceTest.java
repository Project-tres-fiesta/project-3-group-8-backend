package com.example.EventLink.service;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.EventLink.dto.TicketmasterEvent;
import com.example.EventLink.entity.EventEntity;
import com.example.EventLink.repository.EventRepository;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveFromTicketmasterEvent_NewEvent() {
        TicketmasterEvent tmEvent = new TicketmasterEvent();
        tmEvent.setId("TM123");
        tmEvent.setSource("ticketmaster");
        tmEvent.setName("Test Concert");
        tmEvent.setVenueName("Test Arena");
        tmEvent.setVenueCity("Los Angeles");
        tmEvent.setVenueState("CA");
        tmEvent.setVenueCountry("US");
        tmEvent.setCategory("Music");
        tmEvent.setGenre("Rock");
        tmEvent.setLocalDate("2025-12-15");
        tmEvent.setLocalTime("19:30:00");
        tmEvent.setMinPrice(50.0);
        tmEvent.setMaxPrice(150.0);
        tmEvent.setCurrency("USD");

        when(eventRepository.findByExternalEventIdAndExternalSource("TM123", "ticketmaster"))
                .thenReturn(java.util.Optional.empty());

        EventEntity savedEntity = new EventEntity();
        // no setEventId here – your entity doesn’t expose that
        savedEntity.setExternalEventId("TM123");
        when(eventRepository.save(any(EventEntity.class))).thenReturn(savedEntity);

        EventEntity result = eventService.saveFromTicketmasterEvent(tmEvent);

        assertNotNull(result);
        // assert something that definitely exists on your entity
        assertEquals("TM123", result.getExternalEventId());
        verify(eventRepository, times(1))
                .findByExternalEventIdAndExternalSource("TM123", "ticketmaster");
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void testGetUpcomingEvents() {
        when(eventRepository.findByEventDateAfter(any(LocalDate.class)))
                .thenReturn(java.util.Collections.emptyList());

        var result = eventService.getUpcomingEvents();

        assertNotNull(result);
        verify(eventRepository, times(1))
                .findByEventDateAfter(any(LocalDate.class));
    }

    @Test
    void testSearchEventsByTitle() {
        String keyword = "concert";
        when(eventRepository.findByTitleContainingIgnoreCase(keyword))
                .thenReturn(java.util.Collections.emptyList());

        var result = eventService.searchEventsByTitle(keyword);

        assertNotNull(result);
        verify(eventRepository, times(1))
                .findByTitleContainingIgnoreCase(keyword);
    }

    @Test
    void testEventExists() {
        when(eventRepository.existsByExternalEventIdAndExternalSource("TM123", "ticketmaster"))
                .thenReturn(true);

        boolean result = eventService.eventExists("TM123", "ticketmaster");

        assertTrue(result);
        verify(eventRepository, times(1))
                .existsByExternalEventIdAndExternalSource("TM123", "ticketmaster");
    }
}