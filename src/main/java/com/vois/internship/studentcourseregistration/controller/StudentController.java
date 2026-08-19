package com.vois.internship.studentcourseregistration.controller;

import com.vois.internship.studentcourseregistration.api.StudentsApi;
import com.vois.internship.studentcourseregistration.dto.CreateStudentRequest;
import com.vois.internship.studentcourseregistration.dto.PatchStudentRequest;
import com.vois.internship.studentcourseregistration.dto.StudentResponse;
import com.vois.internship.studentcourseregistration.dto.UpdateStudentRequest;
import com.vois.internship.studentcourseregistration.service.StudentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Student operations.
 * Implements the OpenAPI-generated StudentsApi interface.
 */
@RestController
@RequiredArgsConstructor
public class StudentController implements StudentsApi {

  private final StudentService studentService;

  @Override
  public ResponseEntity<StudentResponse> createStudent(CreateStudentRequest createStudentRequest) {
    StudentResponse response = studentService.registerStudent(createStudentRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  public ResponseEntity<List<StudentResponse>> listStudents() {
    List<StudentResponse> students = studentService.getAllStudents();
    return ResponseEntity.ok(students);
  }

  @Override
  public ResponseEntity<StudentResponse> getStudentById(Long studentId) {
    StudentResponse response = studentService.getStudentById(studentId);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<StudentResponse> updateStudent(
      Long studentId, 
      UpdateStudentRequest updateStudentRequest) {
    StudentResponse response = studentService.updateStudent(studentId, updateStudentRequest);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<StudentResponse> patchStudent(
      Long studentId, 
      PatchStudentRequest patchStudentRequest) {
    StudentResponse response = studentService.patchStudent(studentId, patchStudentRequest);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Void> deleteStudent(Long studentId) {
    studentService.deleteStudent(studentId);
    return ResponseEntity.noContent().build();
  }
}
// add: replace - update - delete
