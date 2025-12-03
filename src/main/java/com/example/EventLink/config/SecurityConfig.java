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

  // SecurityConfig.java
@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  return http
    .csrf(csrf -> csrf.disable())
    .cors(cors -> cors.configurationSource(corsSource))
    .authorizeHttpRequests(auth -> {
      auth.requestMatchers(
          "/", "/test-db", "/actuator/health",
          "/oauth2/**", "/login/**",
          "/api/events/**", "/api/ticketmaster/**", "/api/events/stored/**", "/api/user-events/**"
      ).permitAll();

      // Public GET for users (for listing)
      auth.requestMatchers(HttpMethod.GET, "/api/users/**").permitAll();

      auth.requestMatchers("/api/auth/validate", "/api/auth/user").permitAll();
      auth.requestMatchers("/api/auth/token").authenticated();

      auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

      auth.anyRequest().authenticated();
    })
    .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
    .oauth2Login(oauth -> {})
    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
    .build();
}

}

