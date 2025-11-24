package com.example.EventLink.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.EventLink.service.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final JwtService jwt;
  private final CurrentUserService current;

  public AuthController(JwtService jwt, CurrentUserService current) {
    this.jwt = jwt;
    this.current = current;
  }

  /** ✅ Generate JWT token after successful OAuth login */
  @GetMapping("/token")
  public ResponseEntity<?> mint(@AuthenticationPrincipal OAuth2User principal) {
    if (principal == null) return ResponseEntity.status(401).build();

    Integer userId = current.currentUserId(principal);
    String email = principal.getAttribute("email");

    String token = jwt.generateToken(String.valueOf(userId));

    record TokenResponse(String token, Integer userId, String email) {}
    return ResponseEntity.ok(new TokenResponse(token, userId, email));
  }

  /** ✅ Validate token (useful for mobile apps or debugging) */
  @PostMapping("/validate")
  public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
    Map<String, Object> response = new HashMap<>();

    try {
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        response.put("valid", false);
        response.put("error", "Invalid authorization header");
        return ResponseEntity.badRequest().body(response);
      }

      String token = authHeader.substring(7);
      String username = jwt.extractUsername(token);
      boolean isValid = jwt.validateToken(token, username);

      response.put("valid", isValid);
      response.put("username", isValid ? username : null);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      response.put("valid", false);
      response.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }

  /** ✅ Get current OAuth2 user info */
  @GetMapping("/user")
  public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal OAuth2User oauthUser) {
    Map<String, Object> response = new HashMap<>();

    if (oauthUser == null) {
      response.put("error", "Not authenticated");
      return ResponseEntity.status(401).body(response);
    }

    response.put("name", oauthUser.getAttribute("name"));
    response.put("email", oauthUser.getAttribute("email"));
    response.put("picture", oauthUser.getAttribute("picture"));
    response.put("provider", "google");
    return ResponseEntity.ok(response);
  }
}

