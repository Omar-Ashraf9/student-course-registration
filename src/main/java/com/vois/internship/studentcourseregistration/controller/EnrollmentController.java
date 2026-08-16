package com.vois.internship.studentcourseregistration.controller;

import com.vois.internship.studentcourseregistration.dto.CreateEnrollmentRequest;
import com.vois.internship.studentcourseregistration.dto.EnrollmentResponse;
import com.vois.internship.studentcourseregistration.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

  private final EnrollmentService enrollmentService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public EnrollmentResponse enrollStudent(@Valid @RequestBody CreateEnrollmentRequest request) {
    return enrollmentService.enrollStudent(request);
  }

  @PatchMapping("/{id}/withdraw")
  public EnrollmentResponse withdrawEnrollment(@PathVariable Long id) {
    return enrollmentService.withdrawEnrollment(id);
  }
}

// find all enrollments
// find enrollment by id