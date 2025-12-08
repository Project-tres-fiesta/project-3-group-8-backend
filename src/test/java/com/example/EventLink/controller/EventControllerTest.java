package com.example.EventLink.controller;

import com.example.EventLink.entity.EventEntity;
import com.example.EventLink.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EventControllerTest {

    private MockMvc mockMvc;
    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventService = mock(EventService.class);
        EventController controller = new EventController(eventService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetAllEvents() throws Exception {
    
        EventEntity event = new EventEntity();
        event.setEventId(1L);
        event.setTitle("Test Event");
        event.setVenueName("Test Venue");
        event.setVenueCity("Los Angeles");
        event.setEventDate(LocalDate.of(2025, 12, 15));
        event.setMinPrice(BigDecimal.valueOf(50.00));
        event.setMaxPrice(BigDecimal.valueOf(150.00));

        given(eventService.getAllEvents()).willReturn(Collections.singletonList(event));

        
        mockMvc.perform(get("/api/events"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventId", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Event")))
                .andExpect(jsonPath("$[0].venueName", is("Test Venue")))
                .andExpect(jsonPath("$[0].venueCity", is("Los Angeles")));
    }

    @Test
    void testGetEventById_Found() throws Exception {
        
        EventEntity event = new EventEntity();
        event.setEventId(1L);
        event.setTitle("Test Event");

        given(eventService.getEventById(1L)).willReturn(Optional.of(event));

        
        mockMvc.perform(get("/api/events/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId", is(1)))
                .andExpect(jsonPath("$.title", is("Test Event")));
    }

    @Test
    void testGetEventsByCity() throws Exception {
        
        EventEntity event = new EventEntity();
        event.setEventId(1L);
        event.setVenueCity("New York");

        given(eventService.getEventsByCity("New York")).willReturn(Collections.singletonList(event));

        
        mockMvc.perform(get("/api/events/city/New York"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].venueCity", is("New York")));
    }
}
