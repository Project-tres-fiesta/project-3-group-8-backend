package com.example.EventLink.controller;

import com.example.EventLink.entity.EventEntity;
import com.example.EventLink.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/events/stored")
public class StoredEventController {

    @Autowired
    private EventService eventService;

    /**
     * Get all stored events from database
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllStoredEvents() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<EventEntity> events = eventService.getAllEvents();
            
            response.put("success", true);
            response.put("count", events.size());
            response.put("events", events);
            response.put("source", "database");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("events", List.of());
            response.put("count", 0);
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Search stored events by title keyword
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchStoredEvents(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String source) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<EventEntity> events;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                events = eventService.searchEventsByTitle(keyword);
            } else if (category != null && !category.trim().isEmpty()) {
                events = eventService.getEventsByCategory(category);
            } else if (city != null && !city.trim().isEmpty()) {
                events = eventService.getEventsByCity(city);
            } else if (source != null && !source.trim().isEmpty()) {
                events = eventService.getEventsBySource(source);
            } else {
                events = eventService.getAllEvents();
            }
            
            response.put("success", true);
            response.put("count", events.size());
            response.put("events", events);
            response.put("source", "database");
            
            // Add search metadata
            Map<String, Object> searchParams = new HashMap<>();
            if (keyword != null) searchParams.put("keyword", keyword);
            if (category != null) searchParams.put("category", category);
            if (city != null) searchParams.put("city", city);
            if (source != null) searchParams.put("source", source);
            
            response.put("searchParams", searchParams);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("events", List.of());
            response.put("count", 0);
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get specific stored event by database ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getStoredEvent(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<EventEntity> event = eventService.getEventById(id);
            
            if (event.isPresent()) {
                response.put("success", true);
                response.put("event", event.get());
                response.put("source", "database");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Event not found");
                response.put("source", "database");
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("source", "database");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get upcoming stored events
     */
    @GetMapping("/upcoming")
    public ResponseEntity<Map<String, Object>> getUpcomingStoredEvents() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<EventEntity> events = eventService.getUpcomingEvents();
            
            response.put("success", true);
            response.put("count", events.size());
            response.put("events", events);
            response.put("source", "database");
            response.put("filter", "upcoming events only");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("events", List.of());
            response.put("count", 0);
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Delete stored event by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteStoredEvent(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<EventEntity> event = eventService.getEventById(id);
            
            if (event.isPresent()) {
                eventService.deleteEvent(id);
                response.put("success", true);
                response.put("message", "Event deleted successfully");
                response.put("deletedEventId", id);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Event not found");
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}