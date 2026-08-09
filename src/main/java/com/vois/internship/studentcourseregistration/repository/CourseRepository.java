package com.vois.internship.studentcourseregistration.repository;

import com.vois.internship.studentcourseregistration.entities.Course;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

  List<Course> findByTitleContainingIgnoreCase(String title);
}
