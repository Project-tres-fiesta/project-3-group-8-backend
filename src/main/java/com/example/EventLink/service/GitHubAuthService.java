//package com.example.EventLink.service;
//
//import com.example.EventLink.entity.UserEntity;
//import com.example.EventLink.repository.UserRepository;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class GitHubAuthService {
//
//    private final UserRepository userRepository;
//    private final WebClient webClient;
//    private final String clientId = "Ov23liHWadJK0RkGR02x";
//    private final String clientSecret = "e73c7db515bd055b0b8729938aa61d3ab7cc3f43";
//    private final String jwtSecret = "YOUR_JWT_SECRET";
//
//    public GitHubAuthService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//        this.webClient = WebClient.create();
//    }
//
//    public UserEntity loginWithGitHub(String code) {
//        try {
//            // 1️⃣ Exchange code for access token
//            Map<String, Object> tokenResponse = webClient.post()
//                    .uri("https://github.com/login/oauth/access_token")
//                    .header("Accept", "application/json")
//                    .bodyValue(Map.of(
//                            "client_id", clientId,
//                            "client_secret", clientSecret,
//                            "code", code
//                    ))
//                    .retrieve()
//                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
//                    .block();
//
//            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
//                throw new RuntimeException("Failed to get GitHub access token");
//            }
//
//            String accessToken = (String) tokenResponse.get("access_token");
//
//            // 2️⃣ Fetch GitHub user info
//            Map<String, Object> gitHubUser = webClient.get()
//                    .uri("https://api.github.com/user")
//                    .header("Authorization", "Bearer " + accessToken)
//                    .retrieve()
//                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
//                    .block();
//
//            if (gitHubUser == null) {
//                throw new RuntimeException("Failed to fetch GitHub user info");
//            }
//
//            String name = (String) gitHubUser.get("name");
//            String avatarUrl = (String) gitHubUser.get("avatar_url");
//            String email = (String) gitHubUser.get("email");
//
//            // 3️⃣ Fetch primary email if null
//            if (email == null) {
//                List<Map<String, Object>> emails = webClient.get()
//                        .uri("https://api.github.com/user/emails")
//                        .header("Authorization", "Bearer " + accessToken)
//                        .retrieve()
//                        .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
//                        .block();
//
//                if (emails != null) {
//                    email = emails.stream()
//                            .filter(e -> Boolean.TRUE.equals(e.get("primary")))
//                            .map(e -> (String) e.get("email"))
//                            .findFirst()
//                            .orElse(null);
//                }
//            }
//
//            if (email == null) {
//                throw new RuntimeException("GitHub user does not have a public email");
//            }
//
//            // 4️⃣ Check if user exists in DB
//            UserEntity user = userRepository.findByUserEmail(email)
//                    .orElse(null);
//
//            if (user == null) {
//                user = new UserEntity();
//                user.setUserEmail(email);
//                user.setUserName(name != null ? name : "GitHub User");
//                user.setProfilePicture(avatarUrl != null ? avatarUrl : "");
//                userRepository.save(user);
//            }
//
//            return user;
//
//        } catch (WebClientResponseException e) {
//            throw new RuntimeException("GitHub API error: " + e.getResponseBodyAsString(), e);
//        }
//    }
//
//    // 5️⃣ Generate JWT
//    public String generateJwt(UserEntity user) {
//        return Jwts.builder()
//                .setSubject(String.valueOf(user.getUserId()))
//                .claim("name", user.getUserName())
//                .claim("email", user.getUserEmail())
//                .signWith(SignatureAlgorithm.HS256, jwtSecret)
//                .compact();
//    }
//}
