package com.vois.internship.studentcourseregistration.repository;

import com.vois.internship.studentcourseregistration.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Admin entity operations.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
  
  /**
   * Check if an admin exists with the given email.
   * @param email the email to check
   * @return true if admin exists, false otherwise
   */
  boolean existsByEmail(String email);
}
