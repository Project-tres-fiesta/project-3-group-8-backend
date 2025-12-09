package com.example.EventLink.controller;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
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

import com.example.EventLink.repository.GroupEventsRepository;

class GroupEventsControllerTest {

    private MockMvc mockMvc;
    private GroupEventsRepository groupEventsRepository;

    @BeforeEach
    void setUp() throws Exception {
        groupEventsRepository = mock(GroupEventsRepository.class);

        GroupEventsController controller = new GroupEventsController();
        var field = GroupEventsController.class.getDeclaredField("groupEventsRepository");
        field.setAccessible(true);
        field.set(controller, groupEventsRepository);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetEventIdsByGroup_Empty() throws Exception {
        // controller maps entities -> eventId list, but we can just return an empty list
        given(groupEventsRepository.findByIdGroupId(1L))
                .willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/groupEvents/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
