package com.vois.internship.studentcourseregistration.exception;

/**
 * Exception thrown when an Admin is not found.
 */
public class AdminNotFoundException extends RuntimeException {
  
  public AdminNotFoundException(Long id) {
    super("Admin not found with id: " + id);
  }
  
  public AdminNotFoundException(String email) {
    super("Admin not found with email: " + email);
  }
}
