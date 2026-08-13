package com.vois.internship.studentcourseregistration.service.impl;

import com.vois.internship.studentcourseregistration.dto.EnrollStudentRequest;
import com.vois.internship.studentcourseregistration.dto.EnrollmentResponse;
import com.vois.internship.studentcourseregistration.entities.Course;
import com.vois.internship.studentcourseregistration.entities.Enrollment;
import com.vois.internship.studentcourseregistration.entities.EnrollmentStatus;
import com.vois.internship.studentcourseregistration.entities.Student;
import com.vois.internship.studentcourseregistration.exception.CourseFullException;
import com.vois.internship.studentcourseregistration.exception.CourseNotFoundException;
import com.vois.internship.studentcourseregistration.exception.DuplicateEnrollmentException;
import com.vois.internship.studentcourseregistration.exception.EnrollmentNotFoundException;
import com.vois.internship.studentcourseregistration.exception.StudentNotFoundException;
import com.vois.internship.studentcourseregistration.repository.CourseRepository;
import com.vois.internship.studentcourseregistration.repository.EnrollmentRepository;
import com.vois.internship.studentcourseregistration.repository.StudentRepository;
import com.vois.internship.studentcourseregistration.service.EnrollmentService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

  private final EnrollmentRepository enrollmentRepository;
  private final StudentRepository studentRepository;
  private final CourseRepository courseRepository;

  @Override
  @Transactional
  public EnrollmentResponse enrollStudent(EnrollStudentRequest request) {
    // Verify student exists
    Student student = studentRepository.findById(request.studentId())
        .orElseThrow(() -> new StudentNotFoundException(request.studentId()));

    // Verify course exists
    Course course = courseRepository.findById(request.courseId())
        .orElseThrow(() -> new CourseNotFoundException(request.courseId()));

    // Check for duplicate enrollment
    if (enrollmentRepository.existsByStudent_IdAndCourse_Id(request.studentId(), request.courseId())) {
      throw new DuplicateEnrollmentException(request.studentId(), request.courseId());
    }

    // Check course capacity (count only ACTIVE enrollments)
    // Note: In a production system, concurrent enrollment requests may require 
    // locking or another concurrency-control mechanism to prevent overbooking.
    long activeEnrollments = enrollmentRepository.countByCourse_IdAndStatus(
        request.courseId(), 
        EnrollmentStatus.ACTIVE
    );
    
    if (activeEnrollments >= course.getCapacity()) {
      throw new CourseFullException(course.getCode());
    }

    // Create and save enrollment
    Enrollment enrollment = new Enrollment();
    enrollment.setStudent(student);
    enrollment.setCourse(course);
    enrollment.setEnrollmentDate(Instant.now());
    enrollment.setStatus(EnrollmentStatus.ACTIVE);

    Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
    return toResponse(savedEnrollment);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EnrollmentResponse> getStudentEnrollments(Long studentId) {
    // Verify student exists
    if (!studentRepository.existsById(studentId)) {
      throw new StudentNotFoundException(studentId);
    }

    return enrollmentRepository.findByStudent_Id(studentId).stream()
        .map(this::toResponse)
        .toList();
  }

  @Override
  @Transactional
  public EnrollmentResponse withdrawEnrollment(Long enrollmentId) {
    Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
        .orElseThrow(() -> new EnrollmentNotFoundException(enrollmentId));

    // Update status to WITHDRAWN (do not delete the record)
    enrollment.setStatus(EnrollmentStatus.WITHDRAWN);
    Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

    return toResponse(updatedEnrollment);
  }

  private EnrollmentResponse toResponse(Enrollment enrollment) {
    Student student = enrollment.getStudent();
    Course course = enrollment.getCourse();

    return new EnrollmentResponse(
        enrollment.getId(),
        student.getId(),
        student.getFirstName() + " " + student.getLastName(),
        course.getId(),
        course.getCode(),
        course.getTitle(),
        enrollment.getStatus(),
        enrollment.getEnrollmentDate()
    );
  }
}
