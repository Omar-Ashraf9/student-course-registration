package com.vois.internship.studentcourseregistration.controller;

import com.vois.internship.studentcourseregistration.api.CoursesApi;
import com.vois.internship.studentcourseregistration.dto.CourseResponse;
import com.vois.internship.studentcourseregistration.dto.CreateCourseRequest;
import com.vois.internship.studentcourseregistration.dto.PatchCourseRequest;
import com.vois.internship.studentcourseregistration.dto.UpdateCourseRequest;
import com.vois.internship.studentcourseregistration.service.CourseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Course operations.
 * Implements the OpenAPI-generated CoursesApi interface.
 */
@RestController
@RequiredArgsConstructor
public class CourseController implements CoursesApi {

  private final CourseService courseService;

  @Override
  public ResponseEntity<CourseResponse> createCourse(CreateCourseRequest createCourseRequest) {
    CourseResponse response = courseService.createCourse(createCourseRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  public ResponseEntity<List<CourseResponse>> listCourses() {
    List<CourseResponse> courses = courseService.getAllCourses();
    return ResponseEntity.ok(courses);
  }

  @Override
  public ResponseEntity<CourseResponse> getCourseById(Long courseId) {
    CourseResponse response = courseService.getCourseById(courseId);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<CourseResponse> updateCourse(
      Long courseId, 
      UpdateCourseRequest updateCourseRequest) {
    CourseResponse response = courseService.updateCourse(courseId, updateCourseRequest);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<CourseResponse> patchCourse(
      Long courseId, 
      PatchCourseRequest patchCourseRequest) {
    CourseResponse response = courseService.patchCourse(courseId, patchCourseRequest);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Void> deleteCourse(Long courseId) {
    courseService.deleteCourse(courseId);
    return ResponseEntity.noContent().build();
  }
}
