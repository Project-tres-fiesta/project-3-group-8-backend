package com.example.EventLink.service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final String secret = "0123456789abcdef0123456789abcdef"; // 32 chars = 256 bits
    private final SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
            SignatureAlgorithm.HS256.getJcaName());

    public SecretKey getKey() {
        return key;
    }


    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
                .signWith(key)
                .compact();
    }
}
