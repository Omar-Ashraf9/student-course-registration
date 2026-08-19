package com.vois.internship.studentcourseregistration.service;

import com.vois.internship.studentcourseregistration.dto.CreateStudentRequest;
import com.vois.internship.studentcourseregistration.dto.PatchStudentRequest;
import com.vois.internship.studentcourseregistration.dto.StudentResponse;
import com.vois.internship.studentcourseregistration.dto.UpdateStudentRequest;
import java.util.List;

/**
 * Service interface for Student operations.
 */
public interface StudentService {

  /**
   * Register a new student.
   * @param request the student creation request
   * @return the created student response
   * @throws com.vois.internship.studentcourseregistration.exception.DuplicateStudentException if email already exists
   */
  StudentResponse registerStudent(CreateStudentRequest request);

  /**
   * Get all students.
   * @return list of all students
   */
  List<StudentResponse> getAllStudents();

  /**
   * Get student by ID.
   * @param id the student ID
   * @return the student response
   * @throws com.vois.internship.studentcourseregistration.exception.StudentNotFoundException if student not found
   */
  StudentResponse getStudentById(Long id);

  /**
   * Fully update a student (PUT).
   * @param id the student ID
   * @param request the update request
   * @return the updated student response
   * @throws com.vois.internship.studentcourseregistration.exception.StudentNotFoundException if student not found
   */
  StudentResponse updateStudent(Long id, UpdateStudentRequest request);

  /**
   * Partially update a student (PATCH).
   * @param id the student ID
   * @param request the patch request
   * @return the updated student response
   * @throws com.vois.internship.studentcourseregistration.exception.StudentNotFoundException if student not found
   */
  StudentResponse patchStudent(Long id, PatchStudentRequest request);

  /**
   * Delete a student.
   * @param id the student ID
   * @throws com.vois.internship.studentcourseregistration.exception.StudentNotFoundException if student not found
   * @throws com.vois.internship.studentcourseregistration.exception.ResourceDeletionConflictException if student has enrollment history
   */
  void deleteStudent(Long id);
}
