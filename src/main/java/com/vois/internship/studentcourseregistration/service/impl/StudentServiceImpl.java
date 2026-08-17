package com.vois.internship.studentcourseregistration.service.impl;

import com.vois.internship.studentcourseregistration.dto.CreateStudentRequest;
import com.vois.internship.studentcourseregistration.dto.PatchStudentRequest;
import com.vois.internship.studentcourseregistration.dto.StudentResponse;
import com.vois.internship.studentcourseregistration.dto.UpdateStudentRequest;
import com.vois.internship.studentcourseregistration.entities.Student;
import com.vois.internship.studentcourseregistration.exception.DuplicateStudentException;
import com.vois.internship.studentcourseregistration.exception.ResourceDeletionConflictException;
import com.vois.internship.studentcourseregistration.exception.StudentNotFoundException;
import com.vois.internship.studentcourseregistration.mapper.StudentMapper;
import com.vois.internship.studentcourseregistration.repository.StudentRepository;
import com.vois.internship.studentcourseregistration.service.StudentService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;
  private final StudentMapper studentMapper;
  private final PasswordEncoder passwordEncoder;

  /**
   * Register a new student - PUBLIC endpoint.
   * Always creates STUDENT (never ADMIN).
   * Password is BCrypt-encoded before persistence.
   */
  @Override
  @Transactional
  public StudentResponse registerStudent(CreateStudentRequest request) {
    // Check for duplicate email
    if (studentRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateStudentException(request.getEmail());
    }

    // Create student entity (MapStruct handles basic mapping)
    Student student = studentMapper.toEntity(request);
    
    // Encode password with BCrypt
    student.setPassword(passwordEncoder.encode(request.getPassword()));
    
    // Set registration date
    student.setRegistrationDate(Instant.now());

    Student savedStudent = studentRepository.save(student);
    return studentMapper.toResponse(savedStudent);
  }

  /**
   * Get all students - ADMIN ONLY.
   */
  @Override
  @PreAuthorize("hasRole('ADMIN')")
  @Transactional(readOnly = true)
  public List<StudentResponse> getAllStudents() {
    return studentRepository.findAll().stream()
        .map(studentMapper::toResponse)
        .toList();
  }

  /**
   * Get student by ID.
   * Allowed for:
   * - ADMIN (any student)
   * - STUDENT (self only)
   */
  @Override
  @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
  @Transactional(readOnly = true)
  public StudentResponse getStudentById(Long id) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException(id));
    return studentMapper.toResponse(student);
  }

  /**
   * Update student (PUT).
   * Allowed for:
   * - ADMIN (any student)
   * - STUDENT (self only)
   * 
   * Password is BCrypt-encoded if provided.
   */
  @Override
  @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
  @Transactional
  public StudentResponse updateStudent(Long id, UpdateStudentRequest request) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException(id));

    // Map editable fields
    studentMapper.updateEntityFromUpdateRequest(request, student);
    
    // Encode new password if provided
    student.setPassword(passwordEncoder.encode(request.getPassword()));

    Student savedStudent = studentRepository.save(student);
    return studentMapper.toResponse(savedStudent);
  }

  /**
   * Partially update student (PATCH).
   * Allowed for:
   * - ADMIN (any student)
   * - STUDENT (self only)
   * 
   * Password is BCrypt-encoded if provided, unchanged if omitted.
   */
  @Override
  @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
  @Transactional
  public StudentResponse patchStudent(Long id, PatchStudentRequest request) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException(id));

    // Map provided fields only (MapStruct ignores nulls)
    studentMapper.patchEntity(request, student);
    
    // Encode password if provided in patch
    if (request.getPassword() != null) {
      student.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    Student savedStudent = studentRepository.save(student);
    return studentMapper.toResponse(savedStudent);
  }

  /**
   * Delete student.
   * Allowed for:
   * - ADMIN (any student)
   * - STUDENT (self only)
   * 
   * Security authorization does NOT override business rules.
   * Deletion still blocked if enrollment history exists.
   */
  @Override
  @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
  @Transactional
  public void deleteStudent(Long id) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException(id));

    // Business rule: prevent deletion if enrollment history exists
    if (!student.getEnrollments().isEmpty()) {
      throw new ResourceDeletionConflictException(
          "Student",
          id,
          "Student has enrollment history. Cannot delete students with enrollments."
      );
    }

    studentRepository.delete(student);
  }
}
