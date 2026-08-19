package com.vois.internship.studentcourseregistration.exception;

public class DuplicateCourseException extends RuntimeException {
  public DuplicateCourseException(String code) {
    super("Course with code already exists: " + code);
  }
}
