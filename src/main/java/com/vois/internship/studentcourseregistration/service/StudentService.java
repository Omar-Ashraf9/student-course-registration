package com.vois.internship.studentcourseregistration.service;

import com.vois.internship.studentcourseregistration.dto.CreateStudentRequest;
import com.vois.internship.studentcourseregistration.dto.StudentResponse;
import java.util.List;

public interface StudentService {

  StudentResponse registerStudent(CreateStudentRequest request);

  List<StudentResponse> getAllStudents();

  StudentResponse getStudentById(Long id);
}
