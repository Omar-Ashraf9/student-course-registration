package com.vois.internship.studentcourseregistration.repository;

import com.vois.internship.studentcourseregistration.entities.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for AppUser base entity.
 * Used primarily for Spring Security authentication via email lookup.
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

  /**
   * Find user by email for authentication.
   * Email is used as the username in HTTP Basic authentication.
   *
   * @param email the user's email address
   * @return Optional containing the user if found
   */
  Optional<AppUser> findByEmail(String email);

  /**
   * Check if email already exists (for uniqueness validation).
   *
   * @param email the email to check
   * @return true if email exists
   */
  boolean existsByEmail(String email);
}
