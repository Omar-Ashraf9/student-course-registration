package com.vois.internship.studentcourseregistration.service;

import com.vois.internship.studentcourseregistration.dto.CourseResponse;
import com.vois.internship.studentcourseregistration.dto.CreateCourseRequest;
import java.util.List;

public interface CourseService {

  CourseResponse createCourse(CreateCourseRequest request);

  List<CourseResponse> getAllCourses();

  CourseResponse getCourseById(Long id);
}
