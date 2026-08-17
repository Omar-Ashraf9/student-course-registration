package com.vois.internship.studentcourseregistration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for HTTP Basic authentication.
 * 
 * Authentication Flow:
 * 1. Client sends: Authorization: Basic base64(email:password)
 * 2. BasicAuthenticationFilter extracts credentials
 * 3. Creates UsernamePasswordAuthenticationToken (unauthenticated)
 * 4. Passes to AuthenticationManager
 * 5. ProviderManager delegates to DaoAuthenticationProvider
 * 6. DaoAuthenticationProvider:
 *    - Calls CustomUserDetailsService.loadUserByUsername(email)
 *    - Retrieves AuthenticatedUser with BCrypt-encoded password
 *    - Uses BCryptPasswordEncoder.matches(rawPassword, encodedPassword)
 *    - Creates authenticated UsernamePasswordAuthenticationToken
 * 7. Stores authenticated principal in SecurityContext
 * 8. Request proceeds to controllers with SecurityContextHolder populated
 * 
 * Authorization:
 * - Public endpoints: POST /students, GET /courses
 * - Authenticated endpoints: require valid credentials
 * - Role/ownership checks: via @PreAuthorize on service methods
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enables @PreAuthorize, @Secured, etc.
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final CustomUserDetailsService userDetailsService;

  /**
   * BCrypt password encoder for hashing passwords.
   * Uses BCrypt with default strength (10 rounds).
   * Automatically handles salt generation and verification.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * DaoAuthenticationProvider wired with:
   * - Custom UserDetailsService for loading user by email
   * - BCryptPasswordEncoder for password verification
   * 
   * This provider handles the core authentication logic.
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  /**
   * AuthenticationManager handles authentication requests.
   * Uses the DaoAuthenticationProvider configured above.
   */
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authConfig
  ) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  /**
   * SecurityFilterChain configures HTTP security.
   * 
   * CSRF: Disabled - this is a stateless REST API
   * Form Login: Disabled - using HTTP Basic instead
   * HTTP Basic: Enabled - credentials sent with each request
   * Sessions: STATELESS - no server-side session storage
   * 
   * Public endpoints (permitAll):
   * - POST /students - student self-registration
   * - GET /courses - public course catalog
   * - GET /courses/{id} - public course details
   * - H2 console (development only)
   * 
   * All other endpoints require authentication.
   * Role/ownership authorization is handled via @PreAuthorize.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(Customizer.withDefaults())
        .sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(auth -> auth
            // Public endpoints
            .requestMatchers(HttpMethod.POST, "/students").permitAll()
            .requestMatchers(HttpMethod.GET, "/courses", "/courses/*").permitAll()
            
            // H2 console for development
            .requestMatchers("/h2-console", "/h2-console/**").permitAll()
            
            // All other endpoints require authentication
            .anyRequest().authenticated()
        )
        .headers(headers -> headers
            // Allow H2 console frames
            .frameOptions(frame -> frame.sameOrigin())
        )
        .build();
  }
}
