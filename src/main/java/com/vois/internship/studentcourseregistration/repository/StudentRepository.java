package com.vois.internship.studentcourseregistration.repository;

import com.vois.internship.studentcourseregistration.entities.Student;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

  Optional<Student> findByEmail(String email);

  boolean existsByEmail(String email);

  Optional<Student> findByEmailAndLastName(String email, String lastName);
}
