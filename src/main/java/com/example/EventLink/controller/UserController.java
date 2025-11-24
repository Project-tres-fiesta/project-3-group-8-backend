package com.example.EventLink.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;  // 👈 missing
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;  // 👈 missing
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.EventLink.entity.UserEntity;
import com.example.EventLink.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8081")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @GetMapping("/profile")
//    public ResponseEntity<UserEntity> getUserProfile(@AuthenticationPrincipal OAuth2User oauthUser) {
//        if (oauthUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//
//        String email = oauthUser.getAttribute("email");
//        if (email == null) {
//            // Try GitHub alternative
//            email = (String) oauthUser.getAttribute("login") + "@github.com"; // fallback
//        }
//
//        String name = oauthUser.getAttribute("name");
//        String picture = oauthUser.getAttribute("picture"); // Google
//        if (picture == null) picture = oauthUser.getAttribute("avatar_url"); // GitHub fallback
//
//        final String userEmail = email;
//        final String userPicture = picture;
//
//        UserEntity user = userRepository.findByUserEmail(email)
//                .orElseGet(() -> {
//                    UserEntity newUser = new UserEntity();
//                    newUser.setUserEmail(userEmail);
//                    newUser.setUserName(name);
//                    newUser.setProfilePicture(userPicture);
//                    return userRepository.save(newUser);
//                });
//
//        return ResponseEntity.ok(user);
//    }

    @GetMapping("/profile")
    public ResponseEntity<UserEntity> getUserProfile() {

        // Extract email from SecurityContext
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserEntity user = userRepository.findByUserEmail(email)
                .orElse(null);

        if (user == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(user);
    }


    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return ResponseEntity.ok(users); // 200 OK
    }

    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteUserAccount(@AuthenticationPrincipal OAuth2User oauthUser) {
        if (oauthUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = oauthUser.getAttribute("email");
        if (email == null) {
            // Try GitHub alternative
            email = (String) oauthUser.getAttribute("login") + "@github.com"; // fallback
        }

        // Find the user by email
        UserEntity user = userRepository.findByUserEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Delete the user account
        userRepository.delete(user);
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
