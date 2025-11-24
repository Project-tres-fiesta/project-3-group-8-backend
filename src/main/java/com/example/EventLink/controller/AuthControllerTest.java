//package com.example.EventLink.controller;
//
//import com.example.EventLink.entity.UserEntity;
//import com.example.EventLink.service.GitHubAuthService;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthControllerTest {
//
//    private final GitHubAuthService gitHubAuthService;
//
//    public AuthControllerTest(GitHubAuthService gitHubAuthService) {
//        this.gitHubAuthService = gitHubAuthService;
//    }
//
//    @PostMapping("/github")
//    public Map<String, Object> githubLogin(@RequestBody Map<String, String> body) {
//        String code = body.get("code");
//        UserEntity user = gitHubAuthService.loginWithGitHub(code);
//        String token = gitHubAuthService.generateJwt(user);
//
//        return Map.of(
//                "token", token,
//                "user", Map.of(
//                        "id", user.getUserId(),
//                        "name", user.getUserName(),
//                        "email", user.getUserEmail(),
//                        "avatarUrl", user.getProfilePicture()
//                )
//        );
//    }
//}
