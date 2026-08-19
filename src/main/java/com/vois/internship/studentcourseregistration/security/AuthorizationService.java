package com.vois.internship.studentcourseregistration.security;

import com.vois.internship.studentcourseregistration.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Authorization service for ownership checks in method security.
 * 
 * Provides efficient authorization queries without eagerly fetching
 * lazy relationships. Used in @PreAuthorize expressions.
 * 
 * Example usage:
 * @PreAuthorize("hasRole('ADMIN') or @authorizationService.ownsEnrollment(#enrollmentId, principal.id)")
 * 
 * The bean name "authorizationService" is used in SpEL expressions.
 */
@Component("authorizationService")
@RequiredArgsConstructor
public class AuthorizationService {

  private final EnrollmentRepository enrollmentRepository;

  /**
   * Checks if a user owns an enrollment.
   * 
   * Uses an efficient existence query to avoid:
   * - Loading the full Enrollment entity
   * - Initializing the lazy Student relationship
   * 
   * @param enrollmentId the enrollment ID from path parameter
   * @param userId the authenticated user's ID (principal.id)
   * @return true if enrollment exists and belongs to user
   */
  public boolean ownsEnrollment(Long enrollmentId, Long userId) {
    return enrollmentRepository.existsByIdAndStudent_Id(enrollmentId, userId);
  }
}
