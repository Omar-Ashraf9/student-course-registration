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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;

  @Override
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

  @Override
  @Transactional(readOnly = true)
  public List<CourseResponse> getAllCourses() {
    return courseRepository.findAll().stream()
        .map(courseMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public CourseResponse getCourseById(Long id) {
    Course course = courseRepository.findById(id)
        .orElseThrow(() -> new CourseNotFoundException(id));
    return courseMapper.toResponse(course);
  }

  @Override
  @Transactional
  public CourseResponse updateCourse(Long id, UpdateCourseRequest request) {
    Course course = courseRepository.findById(id)
        .orElseThrow(() -> new CourseNotFoundException(id));

    courseMapper.updateEntityFromUpdateRequest(request, course);
    Course savedCourse = courseRepository.save(course);
    return courseMapper.toResponse(savedCourse);
  }

  @Override
  @Transactional
  public CourseResponse patchCourse(Long id, PatchCourseRequest request) {
    Course course = courseRepository.findById(id)
        .orElseThrow(() -> new CourseNotFoundException(id));

    courseMapper.patchEntity(request, course);
    Course savedCourse = courseRepository.save(course);
    return courseMapper.toResponse(savedCourse);
  }

  @Override
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
