package com.vois.internship.studentcourseregistration.exception;

public class DuplicateStudentException extends RuntimeException {
  public DuplicateStudentException(String email) {
    super("Student with email already exists: " + email);
  }
}
