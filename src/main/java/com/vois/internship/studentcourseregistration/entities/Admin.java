package com.vois.internship.studentcourseregistration.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Admin entity representing system administrators.
 * Extends AppUser with SINGLE_TABLE inheritance using discriminator value 'ADMIN'.
 * 
 * Currently has no admin-specific attributes beyond the inherited user fields.
 * Authorization and role-based access control will be implemented when
 * Spring Security is added in a future task.
 */
@Entity
@DiscriminatorValue("ADMIN")
@Getter
@Setter
public class Admin extends AppUser {
  // No admin-specific fields at this time
  // Future enhancements may include:
  // - adminLevel
  // - permissions
  // - lastLoginDate
}
