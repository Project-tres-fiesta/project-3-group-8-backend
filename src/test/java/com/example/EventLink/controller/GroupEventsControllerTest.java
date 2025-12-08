package com.example.EventLink.controller;

import com.example.EventLink.entity.GroupEventsEntity;
import com.example.EventLink.repository.GroupEventsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GroupEventsControllerTest {

    private MockMvc mockMvc;
    private GroupEventsRepository groupEventsRepository;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        groupEventsRepository = mock(GroupEventsRepository.class);
        GroupEventsController controller = new GroupEventsController(groupEventsRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllGroupEvents() throws Exception {
        GroupEventsEntity groupEvent = new GroupEventsEntity();
        groupEvent.setGroupEventId(1L);

        given(groupEventsRepository.findAll()).willReturn(Collections.singletonList(groupEvent));

        mockMvc.perform(get("/api/group-events"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].groupEventId", is(1)));
    }

    @Test
    void testGetGroupEventById_Found() throws Exception {
        GroupEventsEntity groupEvent = new GroupEventsEntity();
        groupEvent.setGroupEventId(1L);

        given(groupEventsRepository.findById(1L)).willReturn(Optional.of(groupEvent));

        mockMvc.perform(get("/api/group-events/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupEventId", is(1)));
    }

    @Test
    void testGetGroupEventById_NotFound() throws Exception {
        given(groupEventsRepository.findById(999L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/group-events/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteGroupEvent() throws Exception {
        doNothing().when(groupEventsRepository).deleteById(1L);

        mockMvc.perform(delete("/api/group-events/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(groupEventsRepository, times(1)).deleteById(1L);
    }
}
