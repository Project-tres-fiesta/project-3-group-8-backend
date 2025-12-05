package com.example.EventLink.controller;

import com.example.EventLink.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/oauth2")
@CrossOrigin(origins = {
    "http://localhost:8081",
    "https://group8-frontend-7f72234233d0.herokuapp.com"
})
public class GithubAuthController {

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    private final JwtService jwtService;

    public GithubAuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/callbackGithub")
    public ResponseEntity<Map<String, Object>> callback(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        String codeVerifier = body.get("codeVerifier"); // PKCE verifier from frontend
        if (code == null) return ResponseEntity.badRequest().build();

        // 1️⃣ Exchange code for tokens
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        //params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        //params.add("grant_type", "authorization_code");
        params.add("code_verifier", codeVerifier); // <-- PKCE verifier included

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        Map<String, Object> tokenResponse = restTemplate.postForObject(
                "https://github.com/login/oauth/access_token", request, Map.class);

        if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String accessToken = (String) tokenResponse.get("access_token");

        // 2️⃣ Fetch Google user info
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(accessToken);

        ResponseEntity<Map> userResponse = restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                new HttpEntity<>(userHeaders),
                Map.class
        );

        ResponseEntity<List<Map<String, Object>>> userEmail = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                new HttpEntity<>(userHeaders),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );


        Map<String, Object> githubUser = userResponse.getBody();
        List<Map<String, Object>> githubEmail = userEmail.getBody();

        if (githubEmail == null || githubEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No emails returned by GitHub"));
        }


        // Extract user fields
        String email = (String) githubEmail.stream()
                .filter(e -> Boolean.TRUE.equals(e.get("primary")))
                .map(e -> e.get("email"))
                .findFirst()
                .orElse(null);


        String name = (String) githubUser.get("login");
        String picture = (String) githubUser.get("avatar_url");

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

