package com.vois.internship.studentcourseregistration.exception;

/**
 * Exception thrown when attempting to create a duplicate Admin.
 * For example: email already exists in the system.
 */
public class DuplicateAdminException extends RuntimeException {
  
  public DuplicateAdminException(String email) {
    super("Admin already exists with email: " + email);
  }
}
