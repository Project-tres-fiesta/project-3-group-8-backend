package com.example.EventLink.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${JWT_SECRET:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String secretKey;

    @Value("${JWT_EXPIRATION:86400000}")
    private long jwtExpiration;

    /**
     * Generate a simple JWT token for a user
     * Using a basic implementation without external JWT library dependencies
     */
    public String generateToken(String username) {
        try {
            // Create header
            String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            String encodedHeader = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(header.getBytes(StandardCharsets.UTF_8));

            // Create payload
            long now = System.currentTimeMillis();
            long exp = now + jwtExpiration;
            String payload = String.format(
                    "{\"sub\": \"%s\", \"iat\": %d, \"exp\": %d}", 
                    username, now / 1000, exp / 1000
            );
            String encodedPayload = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(payload.getBytes(StandardCharsets.UTF_8));

            // Create signature
            String data = encodedHeader + "." + encodedPayload;
            String signature = createSignature(data, secretKey);

            return data + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    /**
     * Generate JWT token for UserDetails
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    /**
     * Validate JWT token
     */
    public boolean validateToken(String token, String username) {
        try {
            String extractedUsername = extractUsername(token);
            return extractedUsername.equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extract username from token
     */
    public String extractUsername(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new RuntimeException("Invalid JWT token format");
            }

            String payload = new String(
                    Base64.getUrlDecoder().decode(parts[1]), 
                    StandardCharsets.UTF_8
            );

            // Simple JSON parsing for username (sub claim)
            int subIndex = payload.indexOf("\"sub\":");
            if (subIndex == -1) {
                throw new RuntimeException("No subject claim found");
            }

            int startQuote = payload.indexOf("\"", subIndex + 6);
            int endQuote = payload.indexOf("\"", startQuote + 1);
            
            return payload.substring(startQuote + 1, endQuote);
        } catch (Exception e) {
            throw new RuntimeException("Error extracting username from token", e);
        }
    }

    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(
                    Base64.getUrlDecoder().decode(parts[1]), 
                    StandardCharsets.UTF_8
            );

            // Extract expiration time
            int expIndex = payload.indexOf("\"exp\":");
            if (expIndex == -1) {
                return true;
            }

            int startNum = expIndex + 6;
            int endNum = payload.indexOf(",", startNum);
            if (endNum == -1) {
                endNum = payload.indexOf("}", startNum);
            }

            long expTime = Long.parseLong(payload.substring(startNum, endNum).trim());
            long currentTime = System.currentTimeMillis() / 1000;

            return currentTime > expTime;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Create HMAC signature
     */
    private String createSignature(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8), 
                    "HmacSHA256"
            );
            mac.init(secretKeySpec);
            byte[] signature = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signature);
        } catch (Exception e) {
            throw new RuntimeException("Error creating signature", e);
        }
    }
}