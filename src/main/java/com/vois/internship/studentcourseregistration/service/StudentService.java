package com.vois.internship.studentcourseregistration.service;

import com.vois.internship.studentcourseregistration.dto.StudentDto;
import com.vois.internship.studentcourseregistration.entities.Student;
import com.vois.internship.studentcourseregistration.mapper.StudentMapper;
import com.vois.internship.studentcourseregistration.repository.StudentRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

  private final StudentRepository studentRepository;
  private final StudentMapper studentMapper;

  public List<StudentDto> getAllStudents() {
    return studentRepository.findAll().stream()
        .map(studentMapper::toDto)
        .toList();
  }

  public Student getStudent(Long id) {
    return studentRepository.findById(id)
        .orElseThrow();
  }

  public Student register(Student student) {
    student.setRegistrationDate(Instant.now());
    return studentRepository.save(student);
  }
}
