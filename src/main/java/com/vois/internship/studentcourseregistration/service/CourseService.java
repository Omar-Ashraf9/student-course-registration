package com.vois.internship.studentcourseregistration.service;

import com.vois.internship.studentcourseregistration.dto.CourseResponse;
import com.vois.internship.studentcourseregistration.dto.CreateCourseRequest;
import com.vois.internship.studentcourseregistration.dto.PatchCourseRequest;
import com.vois.internship.studentcourseregistration.dto.UpdateCourseRequest;
import java.util.List;

/**
 * Service interface for Course operations.
 */
public interface CourseService {

  /**
   * Create a new course.
   * @param request the course creation request
   * @return the created course response
   * @throws com.vois.internship.studentcourseregistration.exception.DuplicateCourseException if course code already exists
   */
  CourseResponse createCourse(CreateCourseRequest request);

  /**
   * Get all courses.
   * @return list of all courses
   */
  List<CourseResponse> getAllCourses();

  /**
   * Get course by ID.
   * @param id the course ID
   * @return the course response
   * @throws com.vois.internship.studentcourseregistration.exception.CourseNotFoundException if course not found
   */
  CourseResponse getCourseById(Long id);

  /**
   * Fully update a course (PUT).
   * @param id the course ID
   * @param request the update request
   * @return the updated course response
   * @throws com.vois.internship.studentcourseregistration.exception.CourseNotFoundException if course not found
   */
  CourseResponse updateCourse(Long id, UpdateCourseRequest request);

  /**
   * Partially update a course (PATCH).
   * @param id the course ID
   * @param request the patch request
   * @return the updated course response
   * @throws com.vois.internship.studentcourseregistration.exception.CourseNotFoundException if course not found
   */
  CourseResponse patchCourse(Long id, PatchCourseRequest request);

  /**
   * Delete a course.
   * @param id the course ID
   * @throws com.vois.internship.studentcourseregistration.exception.CourseNotFoundException if course not found
   * @throws com.vois.internship.studentcourseregistration.exception.ResourceDeletionConflictException if course has enrollment history
   */
  void deleteCourse(Long id);
}
