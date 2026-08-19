package com.vois.internship.studentcourseregistration.controller;

import com.vois.internship.studentcourseregistration.dto.EnrollmentResponse;
import com.vois.internship.studentcourseregistration.service.EnrollmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students/{studentId}/enrollments")
@RequiredArgsConstructor
public class StudentEnrollmentController {

  private final EnrollmentService enrollmentService;

  @GetMapping
  public List<EnrollmentResponse> getStudentEnrollments(@PathVariable Long studentId) {
    return enrollmentService.getStudentEnrollments(studentId);
  }
}