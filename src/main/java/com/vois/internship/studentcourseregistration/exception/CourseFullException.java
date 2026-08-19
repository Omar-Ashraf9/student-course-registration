package com.vois.internship.studentcourseregistration.exception;

public class CourseFullException extends RuntimeException {
  public CourseFullException(String courseCode) {
    super("Course is full: " + courseCode);
  }
}
