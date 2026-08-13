package com.vois.internship.studentcourseregistration.service.impl;

import com.vois.internship.studentcourseregistration.dto.CourseResponse;
import com.vois.internship.studentcourseregistration.dto.CreateCourseRequest;
import com.vois.internship.studentcourseregistration.entities.Course;
import com.vois.internship.studentcourseregistration.exception.CourseNotFoundException;
import com.vois.internship.studentcourseregistration.exception.DuplicateCourseException;
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

  @Override
  @Transactional
  public CourseResponse createCourse(CreateCourseRequest request) {
    // Check for duplicate course code
    if (courseRepository.existsByCode(request.code())) {
      throw new DuplicateCourseException(request.code());
    }

    // Validate capacity (although @Min annotation already checks this at controller level)
    if (request.capacity() <= 0) {
      throw new IllegalArgumentException("Capacity must be greater than 0");
    }

    // Create and save course
    Course course = new Course();
    course.setCode(request.code());
    course.setTitle(request.title());
    course.setDescription(request.description());
    course.setCapacity(request.capacity());

    Course savedCourse = courseRepository.save(course);
    return toResponse(savedCourse);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CourseResponse> getAllCourses() {
    return courseRepository.findAll().stream()
        .map(this::toResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public CourseResponse getCourseById(Long id) {
    Course course = courseRepository.findById(id)
        .orElseThrow(() -> new CourseNotFoundException(id));
    return toResponse(course);
  }

  private CourseResponse toResponse(Course course) {
    return new CourseResponse(
        course.getId(),
        course.getCode(),
        course.getTitle(),
        course.getDescription(),
        course.getCapacity()
    );
  }
}
