package com.vois.internship.studentcourseregistration.controller;

import com.vois.internship.studentcourseregistration.api.EnrollmentsApi;
import com.vois.internship.studentcourseregistration.dto.CreateEnrollmentRequest;
import com.vois.internship.studentcourseregistration.dto.EnrollmentResponse;
import com.vois.internship.studentcourseregistration.dto.PatchEnrollmentRequest;
import com.vois.internship.studentcourseregistration.service.EnrollmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Enrollment operations.
 * Implements the OpenAPI-generated EnrollmentsApi interface.
 */
@RestController
@RequiredArgsConstructor
public class EnrollmentController implements EnrollmentsApi {

  private final EnrollmentService enrollmentService;

  @Override
  public ResponseEntity<EnrollmentResponse> createEnrollment(
      CreateEnrollmentRequest createEnrollmentRequest) {
    EnrollmentResponse response = enrollmentService.enrollStudent(createEnrollmentRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  public ResponseEntity<List<EnrollmentResponse>> listEnrollments() {
    List<EnrollmentResponse> enrollments = enrollmentService.getAllEnrollments();
    return ResponseEntity.ok(enrollments);
  }

  @Override
  public ResponseEntity<EnrollmentResponse> getEnrollmentById(Long enrollmentId) {
    EnrollmentResponse response = enrollmentService.getEnrollmentById(enrollmentId);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<List<EnrollmentResponse>> listStudentEnrollments(Long studentId) {
    List<EnrollmentResponse> enrollments = enrollmentService.getStudentEnrollments(studentId);
    return ResponseEntity.ok(enrollments);
  }

  @Override
  public ResponseEntity<EnrollmentResponse> patchEnrollment(
      Long enrollmentId, 
      PatchEnrollmentRequest patchEnrollmentRequest) {
    EnrollmentResponse response = enrollmentService.patchEnrollment(
        enrollmentId, 
        patchEnrollmentRequest
    );
    return ResponseEntity.ok(response);
  }
}
