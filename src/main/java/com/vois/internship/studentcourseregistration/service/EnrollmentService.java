package com.vois.internship.studentcourseregistration.service;

import com.vois.internship.studentcourseregistration.dto.EnrollStudentRequest;
import com.vois.internship.studentcourseregistration.dto.EnrollmentResponse;
import java.util.List;

public interface EnrollmentService {

  EnrollmentResponse enrollStudent(EnrollStudentRequest request);

  List<EnrollmentResponse> getStudentEnrollments(Long studentId);

  EnrollmentResponse withdrawEnrollment(Long enrollmentId);
}
