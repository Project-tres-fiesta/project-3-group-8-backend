package com.example.EventLink.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.EventLink.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

  private final CorsConfigurationSource corsSource;
  private final JwtAuthenticationFilter jwtFilter;

  public SecurityConfig(CorsConfigurationSource corsSource, JwtAuthenticationFilter jwtFilter) {
    this.corsSource = corsSource;
    this.jwtFilter = jwtFilter;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .csrf(csrf -> csrf.disable())
      .cors(cors -> cors.configurationSource(corsSource))
      .authorizeHttpRequests(auth -> {
        auth.requestMatchers(
            "/", "/test-db", "/actuator/health",
            // OAuth login endpoints for browser flow
            "/oauth2/**", "/login/**"
        ).permitAll();

        // Public-ish auth helper endpoints (token mint needs you to be logged in via OAuth session)
        auth.requestMatchers("/api/auth/validate", "/api/auth/user").permitAll();
        auth.requestMatchers("/api/auth/token").authenticated();

        // CORS preflight
        auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

        // Everything else needs either a session (OAuth) or a valid Bearer token
        auth.anyRequest().authenticated();
      })
      .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
      .oauth2Login(oauth -> {}) // keep Google OAuth for browser
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }
}

