package com.example.EventLink.controller;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.EventLink.service.TicketmasterService;

class EventControllerTest {

    private MockMvc mockMvc;
    private TicketmasterService ticketmasterService;

    @BeforeEach
    void setUp() throws Exception {
        ticketmasterService = mock(TicketmasterService.class);

        EventController controller = new EventController();
        // inject mock into field ticketmasterService
        var field = EventController.class.getDeclaredField("ticketmasterService");
        field.setAccessible(true);
        field.set(controller, ticketmasterService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testSearchEvents() throws Exception {
        // we don’t care about the event contents here, only that the controller
        // wraps the list into the response map correctly
        given(ticketmasterService.searchEvents("rock", "LA", null, null, null, 10))
                .willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/events/search")
                        .param("keyword", "rock")
                        .param("city", "LA")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.events", hasSize(0)))
                .andExpect(jsonPath("$.count", is(0)));
    }

    @Test
    void testGetEventById_NotFound() throws Exception {
        given(ticketmasterService.getEventById("abc123"))
                .willReturn(null); // controller treats null as “not found”

        mockMvc.perform(get("/api/events/abc123"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetEventsByLocation() throws Exception {
        given(ticketmasterService.searchEvents(null, "New York", null, null, null, 20))
                .willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/events/location/New York"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.events", hasSize(0)))
                .andExpect(jsonPath("$.count", is(0)));
    }
}