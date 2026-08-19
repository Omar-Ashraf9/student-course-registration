package com.vois.internship.studentcourseregistration.service;

import com.vois.internship.studentcourseregistration.dto.CreateEnrollmentRequest;
import com.vois.internship.studentcourseregistration.dto.EnrollmentResponse;
import com.vois.internship.studentcourseregistration.dto.PatchEnrollmentRequest;
import java.util.List;

/**
 * Service interface for Enrollment operations.
 */
public interface EnrollmentService {

  /**
   * Create a new enrollment (enroll a student in a course).
   * @param request the enrollment creation request
   * @return the created enrollment response
   * @throws com.vois.internship.studentcourseregistration.exception.StudentNotFoundException if student not found
   * @throws com.vois.internship.studentcourseregistration.exception.CourseNotFoundException if course not found
   * @throws com.vois.internship.studentcourseregistration.exception.DuplicateEnrollmentException if enrollment already exists
   * @throws com.vois.internship.studentcourseregistration.exception.CourseFullException if course capacity reached
   */
  EnrollmentResponse enrollStudent(CreateEnrollmentRequest request);

  /**
   * Get all enrollments.
   * @return list of all enrollments
   */
  List<EnrollmentResponse> getAllEnrollments();

  /**
   * Get enrollment by ID.
   * @param id the enrollment ID
   * @return the enrollment response
   * @throws com.vois.internship.studentcourseregistration.exception.EnrollmentNotFoundException if enrollment not found
   */
  EnrollmentResponse getEnrollmentById(Long id);

  /**
   * Get all enrollments for a specific student.
   * @param studentId the student ID
   * @return list of student enrollments
   * @throws com.vois.internship.studentcourseregistration.exception.StudentNotFoundException if student not found
   */
  List<EnrollmentResponse> getStudentEnrollments(Long studentId);

  /**
   * Update enrollment status (PATCH).
   * Typically used for withdrawing enrollment.
   * @param id the enrollment ID
   * @param request the patch request containing status
   * @return the updated enrollment response
   * @throws com.vois.internship.studentcourseregistration.exception.EnrollmentNotFoundException if enrollment not found
   * @throws com.vois.internship.studentcourseregistration.exception.InvalidEnrollmentStateException if state transition invalid
   */
  EnrollmentResponse patchEnrollment(Long id, PatchEnrollmentRequest request);

  /**
   * Withdraw an enrollment (deprecated - use patchEnrollment instead).
   * @param enrollmentId the enrollment ID
   * @return the updated enrollment response
   * @throws com.vois.internship.studentcourseregistration.exception.EnrollmentNotFoundException if enrollment not found
   * @deprecated Use {@link #patchEnrollment(Long, PatchEnrollmentRequest)} instead
   */
  @Deprecated
  EnrollmentResponse withdrawEnrollment(Long enrollmentId);
}
