package com.vois.internship.studentcourseregistration.service.impl;

import com.vois.internship.studentcourseregistration.dto.CreateStudentRequest;
import com.vois.internship.studentcourseregistration.dto.StudentResponse;
import com.vois.internship.studentcourseregistration.entities.Student;
import com.vois.internship.studentcourseregistration.exception.DuplicateStudentException;
import com.vois.internship.studentcourseregistration.exception.StudentNotFoundException;
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

  @Override
  @Transactional
  public StudentResponse registerStudent(CreateStudentRequest request) {
    // Check for duplicate email
    if (studentRepository.existsByEmail(request.email())) {
      throw new DuplicateStudentException(request.email());
    }

    // Create and save student
    Student student = new Student();
    student.setFirstName(request.firstName());
    student.setLastName(request.lastName());
    student.setEmail(request.email());
    student.setRegistrationDate(Instant.now());

    Student savedStudent = studentRepository.save(student);
    return toResponse(savedStudent);
  }

  @Override
  @Transactional(readOnly = true)
  public List<StudentResponse> getAllStudents() {
    return studentRepository.findAll().stream()
        .map(this::toResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public StudentResponse getStudentById(Long id) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException(id));
    return toResponse(student);
  }

  private StudentResponse toResponse(Student student) {
    return new StudentResponse(
        student.getId(),
        student.getFirstName(),
        student.getLastName(),
        student.getEmail(),
        student.getRegistrationDate()
    );
  }
}
