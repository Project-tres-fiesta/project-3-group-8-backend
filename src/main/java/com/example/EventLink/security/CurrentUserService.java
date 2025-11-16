package com.example.EventLink.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.EventLink.entity.UserEntity;
import com.example.EventLink.repository.UserRepository;

@Service
public class CurrentUserService {

    private final UserRepository users;

    public CurrentUserService(UserRepository users) {
        this.users = users;
    }

    /** For OAuth2 users (from Google login) */
    @Transactional
    public Integer currentUserId(OAuth2User principal) {
        if (principal == null) throw new IllegalStateException("No authenticated user");

        String email = (String) principal.getAttributes().get("email");
        String name  = (String) principal.getAttributes().getOrDefault("name", email);

        if (email == null || email.isBlank()) {
            throw new IllegalStateException("Authenticated user missing email claim");
        }

        Optional<UserEntity> existing = users.findByUserEmail(email);
        UserEntity user = existing.orElseGet(() -> {
            UserEntity u = new UserEntity();
            u.setUserEmail(email);
            u.setUserName(name != null ? name : email);
            return users.save(u);
        });

        Long id = user.getUserId();
        return id == null ? null : Math.toIntExact(id);
    }

    /** ✅ Handles either OAuth2 or JWT Authentication */
    @Transactional
public Integer userIdFromAuth(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
        throw new IllegalStateException("No authenticated user");
    }

    Object principal = authentication.getPrincipal();

    // OAuth2 session user → reuse existing method
    if (principal instanceof OAuth2User oauth) {
        return currentUserId(oauth);
    }

    String username;
    if (principal instanceof UserDetails ud) {
        username = ud.getUsername();        // from JWT filter
    } else {
        username = authentication.getName(); // fallback
    }

    if (username == null || username.isBlank()) {
        throw new IllegalStateException("Authenticated principal missing username");
    }

    // Make it effectively final for the lambda
    final String uname = username;

    UserEntity user = users.findByUserEmail(uname).orElseGet(() -> {
        UserEntity u = new UserEntity();
        u.setUserEmail(uname);
        u.setUserName(uname);
        return users.save(u);
    });

    return Math.toIntExact(user.getUserId());
}

}

