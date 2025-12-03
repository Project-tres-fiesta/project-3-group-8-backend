package com.example.EventLink.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.EventLink.service.JwtService;

@RestController
@RequestMapping("/oauth2")
@CrossOrigin(origins = {
        "http://localhost:8081",
        "https://group8-frontend-7f72234233d0.herokuapp.com"
})
public class GoogleAuthController {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private final JwtService jwtService;

    public GoogleAuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

   @PostMapping("/callback")
public ResponseEntity<Map<String, Object>> callback(@RequestBody Map<String, String> body) {
    String code = body.get("code");
    String codeVerifier = body.get("codeVerifier"); // PKCE verifier from frontend

    System.out.println(">>> /oauth2/callback called");
    System.out.println("code        = " + code);
    System.out.println("codeVerifier= " + codeVerifier);
    System.out.println("redirectUri = " + redirectUri);

    if (code == null) return ResponseEntity.badRequest().build();

    // 1️⃣ Exchange code for tokens
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("code", code);
    params.add("client_id", clientId);
    params.add("client_secret", clientSecret);
    params.add("redirect_uri", redirectUri);
    params.add("grant_type", "authorization_code");
    params.add("code_verifier", codeVerifier); // <-- PKCE verifier included

    HttpEntity<MultiValueMap<String, String>> request =
            new HttpEntity<>(params, headers);

    Map<String, Object> tokenResponse;

    try {
        tokenResponse = restTemplate.postForObject(
                "https://oauth2.googleapis.com/token",
                request,
                Map.class
        );
        System.out.println("Google tokenResponse = " + tokenResponse);
    } catch (Exception ex) {
        ex.printStackTrace();
        Map<String, Object> error = new HashMap<>();
        error.put("error", "token_exchange_failed");
        error.put("exception", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "token_exchange_failed");
        error.put("details", tokenResponse);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    String accessToken = (String) tokenResponse.get("access_token");

    // 2️⃣ Fetch Google user info
    HttpHeaders userHeaders = new HttpHeaders();
    userHeaders.setBearerAuth(accessToken);

    ResponseEntity<Map> userResponse = restTemplate.exchange(
            "https://www.googleapis.com/oauth2/v2/userinfo",
            HttpMethod.GET,
            new HttpEntity<>(userHeaders),
            Map.class
    );

    Map<String, Object> googleUser = userResponse.getBody();

    String email = (String) googleUser.get("email");
    if (email == null || email.isEmpty()) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Google user email not found"));
    }

    String name = (String) googleUser.get("name");
    String picture = (String) googleUser.get("picture");

    // 3️⃣ Generate YOUR backend JWT
    String token;
    try {
        token = jwtService.generateToken(email);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to generate JWT"));
    }

    // 4️⃣ Build response for frontend
    Map<String, Object> response = new HashMap<>();
    response.put("token", token);

    Map<String, Object> userObj = new HashMap<>();
    userObj.put("email", email);
    userObj.put("name", name);
    userObj.put("picture", picture);

    response.put("user", userObj);

    return ResponseEntity.ok(response);
}
}
