package com.example.EventLink.service;

import com.example.EventLink.dto.TicketmasterEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketmasterService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ticketmaster.api.key}")
    private String apiKey;

    @Value("${ticketmaster.api.base-url:https://app.ticketmaster.com/discovery/v2}")
    private String baseUrl;

    public TicketmasterService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Search for events using Ticketmaster Discovery API
     */
    public List<TicketmasterEvent> searchEvents(String keyword, String city, String stateCode, 
                                               String category, String startDateTime, Integer size) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + "/events.json")
                    .queryParam("apikey", apiKey)
                    .queryParam("size", size != null ? size : 20);

            // Add optional query parameters
            if (keyword != null && !keyword.trim().isEmpty()) {
                builder.queryParam("keyword", keyword);
            }
            if (city != null && !city.trim().isEmpty()) {
                builder.queryParam("city", city);
            }
            if (stateCode != null && !stateCode.trim().isEmpty()) {
                builder.queryParam("stateCode", stateCode);
            }
            if (category != null && !category.trim().isEmpty()) {
                builder.queryParam("classificationName", category);
            }
            if (startDateTime != null && !startDateTime.trim().isEmpty()) {
                builder.queryParam("startDateTime", startDateTime);
            }

            URI uri = builder.build().toUri();
            String response = restTemplate.getForObject(uri, String.class);
            
            return parseEventsResponse(response);
            
        } catch (Exception e) {
            System.err.println("Error searching Ticketmaster events: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Get event details by ID
     */
    public TicketmasterEvent getEventById(String eventId) {
        try {
            String url = baseUrl + "/events/" + eventId + ".json?apikey=" + apiKey;
            String response = restTemplate.getForObject(url, String.class);
            
            JsonNode jsonNode = objectMapper.readTree(response);
            return parseEventNode(jsonNode);
            
        } catch (Exception e) {
            System.err.println("Error getting Ticketmaster event: " + e.getMessage());
            return null;
        }
    }

    /**
     * Parse JSON response and extract events
     */
    private List<TicketmasterEvent> parseEventsResponse(String jsonResponse) {
        List<TicketmasterEvent> events = new ArrayList<>();
        
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode embeddedNode = rootNode.path("_embedded");
            JsonNode eventsNode = embeddedNode.path("events");
            
            if (eventsNode.isArray()) {
                for (JsonNode eventNode : eventsNode) {
                    TicketmasterEvent event = parseEventNode(eventNode);
                    if (event != null) {
                        events.add(event);
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error parsing Ticketmaster response: " + e.getMessage());
        }
        
        return events;
    }

    /**
     * Parse individual event JSON node
     */
    private TicketmasterEvent parseEventNode(JsonNode eventNode) {
        try {
            TicketmasterEvent event = new TicketmasterEvent();
            
            // Basic event information
            event.setId(eventNode.path("id").asText());
            event.setName(eventNode.path("name").asText());
            event.setUrl(eventNode.path("url").asText());
            
            // Dates
            JsonNode datesNode = eventNode.path("dates");
            JsonNode startNode = datesNode.path("start");
            event.setLocalDate(startNode.path("localDate").asText());
            event.setLocalTime(startNode.path("localTime").asText());
            event.setDateTime(startNode.path("dateTime").asText());
            
            // Venue information
            JsonNode embeddedNode = eventNode.path("_embedded");
            JsonNode venuesNode = embeddedNode.path("venues");
            if (venuesNode.isArray() && venuesNode.size() > 0) {
                JsonNode venueNode = venuesNode.get(0);
                event.setVenueName(venueNode.path("name").asText());
                event.setVenueCity(venueNode.path("city").path("name").asText());
                event.setVenueState(venueNode.path("state").path("stateCode").asText());
                event.setVenueCountry(venueNode.path("country").path("countryCode").asText());
            }
            
            // Price ranges
            JsonNode priceRangesNode = eventNode.path("priceRanges");
            if (priceRangesNode.isArray() && priceRangesNode.size() > 0) {
                JsonNode priceNode = priceRangesNode.get(0);
                event.setMinPrice(priceNode.path("min").asDouble());
                event.setMaxPrice(priceNode.path("max").asDouble());
                event.setCurrency(priceNode.path("currency").asText());
            }
            
            // Classifications (category/genre)
            JsonNode classificationsNode = eventNode.path("classifications");
            if (classificationsNode.isArray() && classificationsNode.size() > 0) {
                JsonNode classificationNode = classificationsNode.get(0);
                JsonNode segmentNode = classificationNode.path("segment");
                JsonNode genreNode = classificationNode.path("genre");
                
                event.setCategory(segmentNode.path("name").asText());
                event.setGenre(genreNode.path("name").asText());
            }
            
            // Images
            JsonNode imagesNode = eventNode.path("images");
            if (imagesNode.isArray() && imagesNode.size() > 0) {
                event.setImageUrl(imagesNode.get(0).path("url").asText());
            }
            
            // Source identifier
            event.setSource("ticketmaster");
            
            return event;
            
        } catch (Exception e) {
            System.err.println("Error parsing event node: " + e.getMessage());
            return null;
        }
    }

    /**
     * Test API connection
     */
    public boolean testConnection() {
        try {
            String url = baseUrl + "/events.json?apikey=" + apiKey + "&size=1";
            String response = restTemplate.getForObject(url, String.class);
            return response != null && response.contains("events");
        } catch (Exception e) {
            System.err.println("Ticketmaster API connection test failed: " + e.getMessage());
            return false;
        }
    }
}