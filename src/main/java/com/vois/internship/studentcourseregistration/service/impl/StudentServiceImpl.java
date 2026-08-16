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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;
  private final StudentMapper studentMapper;

  @Override
  @Transactional
  public StudentResponse registerStudent(CreateStudentRequest request) {
    // Check for duplicate email
    if (studentRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateStudentException(request.getEmail());
    }

    // Create and save student
    Student student = studentMapper.toEntity(request);
    student.setRegistrationDate(Instant.now());

    Student savedStudent = studentRepository.save(student);
    return studentMapper.toResponse(savedStudent);
  }

  @Override
  @Transactional(readOnly = true)
  public List<StudentResponse> getAllStudents() {
    return studentRepository.findAll().stream()
        .map(studentMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public StudentResponse getStudentById(Long id) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException(id));
    return studentMapper.toResponse(student);
  }

  @Override
  @Transactional
  public StudentResponse updateStudent(Long id, UpdateStudentRequest request) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException(id));

    studentMapper.updateEntityFromUpdateRequest(request, student);
    Student savedStudent = studentRepository.save(student);
    return studentMapper.toResponse(savedStudent);
  }

  @Override
  @Transactional
  public StudentResponse patchStudent(Long id, PatchStudentRequest request) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException(id));

    studentMapper.patchEntity(request, student);
    Student savedStudent = studentRepository.save(student);
    return studentMapper.toResponse(savedStudent);
  }

  @Override
  @Transactional
  public void deleteStudent(Long id) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException(id));

    // Check for enrollment history
    if (!student.getEnrollments().isEmpty()) {
      throw new ResourceDeletionConflictException(
          "Student",
          id,
          "Student has enrollment history. Cannot delete students with enrollments."
      );
    }

    studentRepository.deleteById(id);
  }
}
