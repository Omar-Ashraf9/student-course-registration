package com.vois.internship.studentcourseregistration.repository;

import com.vois.internship.studentcourseregistration.entities.Enrollment;
import com.vois.internship.studentcourseregistration.entities.EnrollmentStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

  List<Enrollment> findByStudent_Id(Long studentId);

  List<Enrollment> findByCourse_Id(Long courseId);

  List<Enrollment> findByStudent_IdAndStatus(Long studentId, EnrollmentStatus status);
}

