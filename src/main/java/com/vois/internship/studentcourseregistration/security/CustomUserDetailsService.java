package com.vois.internship.studentcourseregistration.security;

import com.vois.internship.studentcourseregistration.entities.Admin;
import com.vois.internship.studentcourseregistration.entities.AppUser;
import com.vois.internship.studentcourseregistration.entities.Student;
import com.vois.internship.studentcourseregistration.repository.AppUserRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService for Spring Security authentication.
 * 
 * Authentication Flow:
 * 1. HTTP Basic Auth header received: Authorization: Basic base64(email:password)
 * 2. BasicAuthenticationFilter extracts email (as username) and password
 * 3. Creates UsernamePasswordAuthenticationToken
 * 4. AuthenticationManager delegates to ProviderManager
 * 5. ProviderManager delegates to DaoAuthenticationProvider
 * 6. DaoAuthenticationProvider calls this.loadUserByUsername(email)
 * 7. Returns AuthenticatedUser with BCrypt-encoded password
 * 8. DaoAuthenticationProvider uses BCryptPasswordEncoder to verify password
 * 9. On success, creates authenticated Authentication object
 * 10. Stores in SecurityContext
 * 
 * The email is used as the username because authentication is via email:password.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final AppUserRepository appUserRepository;

  /**
   * Loads user by email for authentication.
   * 
   * Derives Spring Security authority from entity subtype:
   * - Student entity -> ROLE_STUDENT
   * - Admin entity   -> ROLE_ADMIN
   * 
   * @param email the user's email (username for HTTP Basic auth)
   * @return AuthenticatedUser principal with id, email, encoded password, authorities
   * @throws UsernameNotFoundException if email not found
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    AppUser user = appUserRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(
            "User not found with email: " + email
        ));

    // Derive Spring Security authority from entity subtype
    // This avoids duplicating the discriminator column
    String role = determineRole(user);
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

    return new AuthenticatedUser(
        user.getId(),
        user.getEmail(),
        user.getPassword(), // BCrypt-encoded password
        Collections.singletonList(authority)
    );
  }

  /**
   * Determines Spring Security role from entity subtype.
   * 
   * Uses instanceof to check the actual runtime type:
   * - Student instance -> "ROLE_STUDENT"
   * - Admin instance   -> "ROLE_ADMIN"
   * 
   * This approach reuses the existing JPA discriminator without
   * adding another persistent role field.
   */
  private String determineRole(AppUser user) {
    if (user instanceof Student) {
      return "ROLE_STUDENT";
    } else if (user instanceof Admin) {
      return "ROLE_ADMIN";
    }
    throw new IllegalStateException(
        "Unknown user type: " + user.getClass().getName()
    );
  }
}
