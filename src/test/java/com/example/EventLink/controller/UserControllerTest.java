// src/test/java/com/example/EventLink/controller/UserControllerTest.java
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

import com.example.EventLink.entity.UserEntity;
import com.example.EventLink.repository.UserRepository;

public class UserControllerTest {

    private MockMvc mockMvc;
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        UserController controller = new UserController(userRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserEntity u = new UserEntity();
        u.setUserId(3L);
        u.setUserName("testuser");
        u.setUserEmail("testuser@example.com");

        given(userRepository.findAll()).willReturn(Collections.singletonList(u));

        // 👇 If your controller is @RequestMapping("/api/users"), change to "/api/users"
        mockMvc.perform(get("/api/users"))
              .andDo(print())
              .andExpect(status().isOk())
              .andExpect(jsonPath("$", hasSize(1)))
              // If your JSON uses "id" instead of "userId", change to "$[0].id"
              .andExpect(jsonPath("$[0].userId", is(3)))
              .andExpect(jsonPath("$[0].userName", is("testuser")))
              .andExpect(jsonPath("$[0].userEmail", is("testuser@example.com")));
    }

    // Note: DELETE /api/users/account endpoint requires OAuth2 authentication
    // Integration tests with proper security context would be needed for full testing
}



