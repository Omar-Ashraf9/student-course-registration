package com.vois.internship.studentcourseregistration.controller;

import com.vois.internship.studentcourseregistration.dto.StudentDto;
import com.vois.internship.studentcourseregistration.entities.Student;
import com.vois.internship.studentcourseregistration.service.StudentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

  private final StudentService studentService;

  @GetMapping
  public List<StudentDto> getStudents() {
    return studentService.getAllStudents();
  }

  @PostMapping
  public Student registerStudent(
      @RequestBody Student student
  ) {
    return studentService.register(student);
  }
}
