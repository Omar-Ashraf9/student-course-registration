package com.vois.internship.studentcourseregistration.service.impl;

import com.vois.internship.studentcourseregistration.dto.CourseResponse;
import com.vois.internship.studentcourseregistration.dto.CreateCourseRequest;
import com.vois.internship.studentcourseregistration.dto.PatchCourseRequest;
import com.vois.internship.studentcourseregistration.dto.UpdateCourseRequest;
import com.vois.internship.studentcourseregistration.entities.Course;
import com.vois.internship.studentcourseregistration.exception.CourseNotFoundException;
import com.vois.internship.studentcourseregistration.exception.DuplicateCourseException;
import com.vois.internship.studentcourseregistration.exception.ResourceDeletionConflictException;
import com.vois.internship.studentcourseregistration.mapper.CourseMapper;
import com.vois.internship.studentcourseregistration.repository.CourseRepository;
import com.vois.internship.studentcourseregistration.service.CourseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;

  /**
   * Create course - ADMIN ONLY.
   */
  @Override
  @PreAuthorize("hasRole('ADMIN')")
  @Transactional
  public CourseResponse createCourse(CreateCourseRequest request) {
    // Check for duplicate course code
    if (courseRepository.existsByCode(request.getCode())) {
      throw new DuplicateCourseException(request.getCode());
    }

    // Validate capacity (although Bean Validation already checks this at controller level)
    if (request.getCapacity() <= 0) {
      throw new IllegalArgumentException("Capacity must be greater than 0");
    }

    // Create and save course
    Course course = courseMapper.toEntity(request);
    Course savedCourse = courseRepository.save(course);
    return courseMapper.toResponse(savedCourse);
  }

  /**
   * Get all courses - PUBLIC.
   * No authorization required.
   */
  @Override
  @Transactional(readOnly = true)
  public List<CourseResponse> getAllCourses() {
    return courseRepository.findAll().stream()
        .map(courseMapper::toResponse)
        .toList();
  }

  /**
   * Get course by ID - PUBLIC.
   * No authorization required.
   */
  @Override
  @Transactional(readOnly = true)
  public CourseResponse getCourseById(Long id) {
    Course course = courseRepository.findById(id)
        .orElseThrow(() -> new CourseNotFoundException(id));
    return courseMapper.toResponse(course);
  }

  /**
   * Update course (PUT) - ADMIN ONLY.
   */
  @Override
  @PreAuthorize("hasRole('ADMIN')")
  @Transactional
  public CourseResponse updateCourse(Long id, UpdateCourseRequest request) {
    Course course = courseRepository.findById(id)
        .orElseThrow(() -> new CourseNotFoundException(id));

    courseMapper.updateEntityFromUpdateRequest(request, course);
    Course savedCourse = courseRepository.save(course);
    return courseMapper.toResponse(savedCourse);
  }

  /**
   * Partially update course (PATCH) - ADMIN ONLY.
   */
  @Override
  @PreAuthorize("hasRole('ADMIN')")
  @Transactional
  public CourseResponse patchCourse(Long id, PatchCourseRequest request) {
    Course course = courseRepository.findById(id)
        .orElseThrow(() -> new CourseNotFoundException(id));

    courseMapper.patchEntity(request, course);
    Course savedCourse = courseRepository.save(course);
    return courseMapper.toResponse(savedCourse);
  }

  /**
   * Delete course - ADMIN ONLY.
   * Business rule: prevent deletion if enrollment history exists.
   */
  @Override
  @PreAuthorize("hasRole('ADMIN')")
  @Transactional
  public void deleteCourse(Long id) {
    Course course = courseRepository.findById(id)
        .orElseThrow(() -> new CourseNotFoundException(id));

    // Check for enrollment history
    if (!course.getEnrollments().isEmpty()) {
      throw new ResourceDeletionConflictException(
          "Course",
          id,
          "Course has enrollment history. Cannot delete courses with enrollments."
      );
    }

    courseRepository.deleteById(id);
  }
}
