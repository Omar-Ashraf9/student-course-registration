package com.vois.internship.studentcourseregistration.controller;

import com.vois.internship.studentcourseregistration.dto.CourseResponse;
import com.vois.internship.studentcourseregistration.dto.CreateCourseRequest;
import com.vois.internship.studentcourseregistration.service.CourseService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

  private final CourseService courseService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CourseResponse createCourse(@Valid @RequestBody CreateCourseRequest request) {
    return courseService.createCourse(request);
  }

  @GetMapping
  public List<CourseResponse> getAllCourses() {
    return courseService.getAllCourses();
  }

  @GetMapping("/{id}")
  public CourseResponse getCourseById(@PathVariable Long id) {
    return courseService.getCourseById(id);
  }

  // add: replace - update - delete
}
