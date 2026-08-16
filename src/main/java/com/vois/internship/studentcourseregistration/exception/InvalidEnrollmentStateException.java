package com.vois.internship.studentcourseregistration.exception;

/**
 * Exception thrown when an invalid enrollment state transition is attempted.
 * For example: trying to activate a withdrawn enrollment, or complete a non-active enrollment.
 */
public class InvalidEnrollmentStateException extends RuntimeException {
  
  public InvalidEnrollmentStateException(String message) {
    super(message);
  }
}
