package com.example.EventLink.controller;

import com.example.EventLink.dto.TicketmasterEvent;
import com.example.EventLink.service.TicketmasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ticketmaster")
public class TicketmasterController {

    @Autowired
    private TicketmasterService ticketmasterService;

    /**
     * Test endpoint to verify Ticketmaster API connection
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testConnection() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean isConnected = ticketmasterService.testConnection();
            response.put("connected", isConnected);
            response.put("service", "ticketmaster");
            response.put("message", isConnected ? "API connection successful" : "API connection failed");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("connected", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Search events endpoint
     */
    @GetMapping("/events/search")
    public ResponseEntity<Map<String, Object>> searchEvents(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String startDate,
            @RequestParam(defaultValue = "20") Integer size) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<TicketmasterEvent> events = ticketmasterService.searchEvents(
                keyword, city, state, category, startDate, size
            );
            
            response.put("events", events);
            response.put("count", events.size());
            response.put("source", "ticketmaster");
            response.put("success", true);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("events", List.of());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get specific event by ID
     */
    @GetMapping("/events/{eventId}")
    public ResponseEntity<Map<String, Object>> getEvent(@PathVariable String eventId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            TicketmasterEvent event = ticketmasterService.getEventById(eventId);
            
            if (event != null) {
                response.put("event", event);
                response.put("success", true);
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

    /**
     * Get events by city (popular endpoint)
     */
    @GetMapping("/events/city/{city}")
    public ResponseEntity<Map<String, Object>> getEventsByCity(
            @PathVariable String city,
            @RequestParam(required = false) String state,
            @RequestParam(defaultValue = "20") Integer size) {
        
        return searchEvents(null, city, state, null, null, size);
    }

    /**
     * Get events by category
     */
    @GetMapping("/events/category/{category}")
    public ResponseEntity<Map<String, Object>> getEventsByCategory(
            @PathVariable String category,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "20") Integer size) {
        
        return searchEvents(null, city, null, category, null, size);
    }
}