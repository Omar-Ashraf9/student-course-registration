package com.vois.internship.studentcourseregistration.security;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Custom UserDetails implementation for Spring Security authentication.
 * 
 * This principal contains the authenticated user's essential information:
 * - id: Domain user ID for authorization checks
 * - email: Used as username in HTTP Basic auth
 * - password: BCrypt-encoded password for authentication
 * - authorities: ROLE_STUDENT or ROLE_ADMIN
 * 
 * The principal is stored in SecurityContext after successful authentication
 * and is available via @PreAuthorize("principal.id") expressions.
 */
@Getter
public class AuthenticatedUser implements UserDetails {

  private final Long id;
  private final String email;
  private final String password;
  private final Collection<? extends GrantedAuthority> authorities;

  public AuthenticatedUser(
      Long id,
      String email,
      String password,
      Collection<? extends GrantedAuthority> authorities
  ) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  /**
   * Returns the email as the username for Spring Security.
   * HTTP Basic auth will use email:password format.
   */
  @Override
  public String getUsername() {
    return email;
  }

  /**
   * Returns the BCrypt-encoded password.
   */
  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
