package com.vois.internship.studentcourseregistration.service.impl;

import com.vois.internship.studentcourseregistration.dto.CreateEnrollmentRequest;
import com.vois.internship.studentcourseregistration.dto.EnrollmentResponse;
import com.vois.internship.studentcourseregistration.dto.PatchEnrollmentRequest;
import com.vois.internship.studentcourseregistration.entities.Course;
import com.vois.internship.studentcourseregistration.entities.Enrollment;
import com.vois.internship.studentcourseregistration.entities.EnrollmentStatus;
import com.vois.internship.studentcourseregistration.entities.Student;
import com.vois.internship.studentcourseregistration.exception.CourseFullException;
import com.vois.internship.studentcourseregistration.exception.CourseNotFoundException;
import com.vois.internship.studentcourseregistration.exception.DuplicateEnrollmentException;
import com.vois.internship.studentcourseregistration.exception.EnrollmentNotFoundException;
import com.vois.internship.studentcourseregistration.exception.InvalidEnrollmentStateException;
import com.vois.internship.studentcourseregistration.exception.StudentNotFoundException;
import com.vois.internship.studentcourseregistration.mapper.EnrollmentMapper;
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
  private final EnrollmentMapper enrollmentMapper;

  @Override
  @Transactional
  public EnrollmentResponse enrollStudent(CreateEnrollmentRequest request) {
    // Verify student exists
    Student student = studentRepository.findById(request.getStudentId())
        .orElseThrow(() -> new StudentNotFoundException(request.getStudentId()));

    // Verify course exists
    Course course = courseRepository.findById(request.getCourseId())
        .orElseThrow(() -> new CourseNotFoundException(request.getCourseId()));

    // Check for duplicate enrollment
    if (enrollmentRepository.existsByStudent_IdAndCourse_Id(request.getStudentId(), request.getCourseId())) {
      throw new DuplicateEnrollmentException(request.getStudentId(), request.getCourseId());
    }

    // Check course capacity (count only ACTIVE enrollments)
    long activeEnrollments = enrollmentRepository.countByCourse_IdAndStatus(
        request.getCourseId(), 
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
    return enrollmentMapper.toResponse(savedEnrollment);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EnrollmentResponse> getAllEnrollments() {
    return enrollmentRepository.findAll().stream()
        .map(enrollmentMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public EnrollmentResponse getEnrollmentById(Long id) {
    Enrollment enrollment = enrollmentRepository.findById(id)
        .orElseThrow(() -> new EnrollmentNotFoundException(id));
    return enrollmentMapper.toResponse(enrollment);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EnrollmentResponse> getStudentEnrollments(Long studentId) {
    // Verify student exists
    if (!studentRepository.existsById(studentId)) {
      throw new StudentNotFoundException(studentId);
    }

    return enrollmentRepository.findByStudent_Id(studentId).stream()
        .map(enrollmentMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional
  public EnrollmentResponse patchEnrollment(Long id, PatchEnrollmentRequest request) {
    Enrollment enrollment = enrollmentRepository.findById(id)
        .orElseThrow(() -> new EnrollmentNotFoundException(id));

    // Validate state transitions if status is being changed
    if (request.getStatus() != null) {
      // Convert DTO StatusEnum to entity EnrollmentStatus
      EnrollmentStatus newStatus = EnrollmentStatus.valueOf(request.getStatus().name());
      validateStateTransition(enrollment.getStatus(), newStatus);
    }

    enrollmentMapper.patchEntity(request, enrollment);
    Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
    return enrollmentMapper.toResponse(savedEnrollment);
  }

  @Override
  @Transactional
  @Deprecated
  public EnrollmentResponse withdrawEnrollment(Long enrollmentId) {
    Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
        .orElseThrow(() -> new EnrollmentNotFoundException(enrollmentId));

    // Update status to WITHDRAWN (do not delete the record)
    enrollment.setStatus(EnrollmentStatus.WITHDRAWN);
    Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

    return enrollmentMapper.toResponse(updatedEnrollment);
  }

  /**
   * Validate enrollment status transitions.
   * Basic rules:
   * - ACTIVE can transition to WITHDRAWN or COMPLETED
   * - WITHDRAWN and COMPLETED are terminal states (no further transitions)
   */
  private void validateStateTransition(EnrollmentStatus currentStatus, EnrollmentStatus newStatus) {
    if (currentStatus == newStatus) {
      return; // Same status, no transition needed
    }

    switch (currentStatus) {
      case ACTIVE:
        // ACTIVE can transition to WITHDRAWN or COMPLETED
        if (newStatus != EnrollmentStatus.WITHDRAWN && newStatus != EnrollmentStatus.COMPLETED) {
          throw new InvalidEnrollmentStateException(
              String.format("Cannot transition from %s to %s", currentStatus, newStatus)
          );
        }
        break;

      case WITHDRAWN:
      case COMPLETED:
        // Terminal states - no transitions allowed
        throw new InvalidEnrollmentStateException(
            String.format("Cannot change status from terminal state %s to %s", currentStatus, newStatus)
        );

      default:
        throw new InvalidEnrollmentStateException("Unknown enrollment status: " + currentStatus);
    }
  }
}
