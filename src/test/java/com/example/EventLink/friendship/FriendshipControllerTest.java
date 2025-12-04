package com.example.EventLink.friendship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.EventLink.friendship.dto.FriendRequestCreate;
import com.example.EventLink.friendship.dto.FriendshipDto;
import com.example.EventLink.repository.UserRepository;
import com.example.EventLink.security.CurrentUserService;

class FriendshipControllerTest {

    private MockMvc mvc;
    private FriendshipService service;
    private CurrentUserService currentUser;
    private UserRepository userRepository;

    /**
     * Custom resolver to inject a fake Authentication into controller
     * methods that have an Authentication parameter.
     */
    static class DummyAuthResolver implements HandlerMethodArgumentResolver {
        private final Authentication auth;

        DummyAuthResolver(Authentication auth) {
            this.auth = auth;
        }

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return Authentication.class.isAssignableFrom(parameter.getParameterType());
        }

        @Override
        public Object resolveArgument(
                MethodParameter parameter,
                ModelAndViewContainer mavContainer,
                NativeWebRequest webRequest,
                WebDataBinderFactory binderFactory
        ) {
            return auth;
        }
    }

    @BeforeEach
    void setup() {
        service = mock(FriendshipService.class);
        currentUser = mock(CurrentUserService.class);
        userRepository = mock(UserRepository.class);

        // ✅ Match your new FriendshipController constructor
        FriendshipController controller =
                new FriendshipController(service, currentUser, userRepository);

        // Fake Authentication object that will be injected into the controller
        Authentication fakeAuth = mock(Authentication.class);

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new DummyAuthResolver(fakeAuth))
                .build();

        // ✅ Match the method used in the controller: userIdFromAuth(auth)
        when(currentUser.userIdFromAuth(fakeAuth)).thenReturn(1);
    }

    @Test
    void send_ok_whenAuthenticated() throws Exception {
        when(service.sendRequest(eq(1), any(FriendRequestCreate.class)))
                .thenReturn(new FriendshipDto(
                        10,
                        1,
                        2,
                        Friendship.Status.pending,
                        1
                ));

        mvc.perform(post("/api/friendships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"toUserId\": 2}"))
                .andExpect(status().isCreated());
    }
}






