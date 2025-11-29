package com.example.EventLink.service;

import com.example.EventLink.dto.TicketmasterEvent;
import com.example.EventLink.entity.EventEntity;
import com.example.EventLink.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    /**
     * Save or update an event from external API data
     */
    public EventEntity saveFromTicketmasterEvent(TicketmasterEvent ticketmasterEvent) {
        // Check if event already exists
        Optional<EventEntity> existingEvent = eventRepository
                .findByExternalEventIdAndExternalSource(ticketmasterEvent.getId(), ticketmasterEvent.getSource());
        
        EventEntity eventEntity;
        if (existingEvent.isPresent()) {
            eventEntity = existingEvent.get();
        } else {
            eventEntity = new EventEntity();
            eventEntity.setExternalEventId(ticketmasterEvent.getId());
            eventEntity.setExternalSource(ticketmasterEvent.getSource());
        }
        
        // Update fields from TicketmasterEvent
        eventEntity.setTitle(ticketmasterEvent.getName());
        eventEntity.setVenueName(ticketmasterEvent.getVenueName());
        eventEntity.setVenueCity(ticketmasterEvent.getVenueCity());
        eventEntity.setVenueState(ticketmasterEvent.getVenueState());
        eventEntity.setVenueCountry(ticketmasterEvent.getVenueCountry());
        eventEntity.setCategory(ticketmasterEvent.getCategory());
        eventEntity.setGenre(ticketmasterEvent.getGenre());
        eventEntity.setImageUrl(ticketmasterEvent.getImageUrl());
        eventEntity.setEventUrl(ticketmasterEvent.getUrl());
        eventEntity.setCurrency(ticketmasterEvent.getCurrency());
        
        // Parse and set date
        if (ticketmasterEvent.getLocalDate() != null && !ticketmasterEvent.getLocalDate().isEmpty()) {
            try {
                eventEntity.setEventDate(LocalDate.parse(ticketmasterEvent.getLocalDate()));
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing date: " + ticketmasterEvent.getLocalDate());
            }
        }
        
        // Parse and set time
        if (ticketmasterEvent.getLocalTime() != null && !ticketmasterEvent.getLocalTime().isEmpty()) {
            try {
                eventEntity.setEventTime(LocalTime.parse(ticketmasterEvent.getLocalTime()));
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing time: " + ticketmasterEvent.getLocalTime());
            }
        }
        
        // Set price ranges
        if (ticketmasterEvent.getMinPrice() != null) {
            eventEntity.setMinPrice(BigDecimal.valueOf(ticketmasterEvent.getMinPrice()));
        }
        if (ticketmasterEvent.getMaxPrice() != null) {
            eventEntity.setMaxPrice(BigDecimal.valueOf(ticketmasterEvent.getMaxPrice()));
        }
        
        return eventRepository.save(eventEntity);
    }
    
    /**
     * Get all stored events
     */
    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }
    
    /**
     * Get events by source (e.g., "ticketmaster")
     */
    public List<EventEntity> getEventsBySource(String source) {
        return eventRepository.findByExternalSource(source);
    }
    
    /**
     * Search events by title keyword
     */
    public List<EventEntity> searchEventsByTitle(String keyword) {
        return eventRepository.findByTitleContainingIgnoreCase(keyword);
    }
    
    /**
     * Get events by category
     */
    public List<EventEntity> getEventsByCategory(String category) {
        return eventRepository.findByCategory(category);
    }
    
    /**
     * Get events by city
     */
    public List<EventEntity> getEventsByCity(String city) {
        return eventRepository.findByVenueCity(city);
    }
    
    /**
     * Get upcoming events (after today)
     */
    public List<EventEntity> getUpcomingEvents() {
        return eventRepository.findByEventDateAfter(LocalDate.now());
    }
    
    /**
     * Get event by ID
     */
    public Optional<EventEntity> getEventById(Long id) {
        return eventRepository.findById(id);
    }
    
    /**
     * Check if event exists by external ID and source
     */
    public boolean eventExists(String externalEventId, String source) {
        return eventRepository.existsByExternalEventIdAndExternalSource(externalEventId, source);
    }
    
    /**
     * Delete event by ID
     */
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
    
    /**
     * Save multiple events from a list of TicketmasterEvent objects
     */
    public List<EventEntity> saveMultipleEvents(List<TicketmasterEvent> ticketmasterEvents) {
        return ticketmasterEvents.stream()
                .map(this::saveFromTicketmasterEvent)
                .toList();
    }
    
    /**
     * Save an event entity directly (for manual event creation)
     */
    public EventEntity saveEvent(EventEntity event) {
        return eventRepository.save(event);
    }
}