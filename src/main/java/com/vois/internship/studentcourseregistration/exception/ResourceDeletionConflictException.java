package com.vois.internship.studentcourseregistration.exception;

/**
 * Exception thrown when attempting to delete a resource that has related records.
 * For example: deleting a Student or Course that has enrollment history.
 */
public class ResourceDeletionConflictException extends RuntimeException {
  
  public ResourceDeletionConflictException(String resourceType, Long id, String reason) {
    super(String.format("Cannot delete %s with id %d: %s", resourceType, id, reason));
  }
}
