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
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private TicketmasterService ticketmasterService;

    /**
     * Search events endpoint - unified interface for event searching
     * Currently integrates with Ticketmaster, can be extended for other providers
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchEvents(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String startDate,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "all") String source) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<TicketmasterEvent> events;
            
            // Currently only supporting Ticketmaster, but extensible for other sources
            switch (source.toLowerCase()) {
                case "ticketmaster":
                case "all":
                default:
                    events = ticketmasterService.searchEvents(
                        keyword, city, state, category, startDate, size
                    );
                    break;
            }
            
            response.put("events", events);
            response.put("count", events.size());
            response.put("source", source.equals("all") ? "ticketmaster" : source);
            response.put("success", true);
            
            // Add search metadata
            Map<String, Object> searchParams = new HashMap<>();
            if (keyword != null) searchParams.put("keyword", keyword);
            if (city != null) searchParams.put("city", city);
            if (state != null) searchParams.put("state", state);
            if (category != null) searchParams.put("category", category);
            if (startDate != null) searchParams.put("startDate", startDate);
            searchParams.put("size", size);
            
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
     * Get specific event by ID
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<Map<String, Object>> getEvent(
            @PathVariable String eventId,
            @RequestParam(defaultValue = "ticketmaster") String source) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            TicketmasterEvent event = null;
            
            // Currently only supporting Ticketmaster
            switch (source.toLowerCase()) {
                case "ticketmaster":
                default:
                    event = ticketmasterService.getEventById(eventId);
                    break;
            }
            
            if (event != null) {
                response.put("event", event);
                response.put("success", true);
                response.put("source", source);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Event not found");
                response.put("source", source);
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("source", source);
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get events by location (convenience endpoint)
     */
    @GetMapping("/location/{city}")
    public ResponseEntity<Map<String, Object>> getEventsByLocation(
            @PathVariable String city,
            @RequestParam(required = false) String state,
            @RequestParam(defaultValue = "20") Integer size) {
        
        return searchEvents(null, city, state, null, null, size, "all");
    }

    /**
     * Get events by category (convenience endpoint)
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getEventsByCategory(
            @PathVariable String category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(defaultValue = "20") Integer size) {
        
        return searchEvents(null, city, state, category, null, size, "all");
    }

    /**
     * Get popular/trending events (convenience endpoint)
     */
    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> getPopularEvents(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(defaultValue = "20") Integer size) {
        
        // For now, this returns general search results
        // Could be enhanced with popularity scoring in the future
        return searchEvents(null, city, state, null, null, size, "all");
    }
}