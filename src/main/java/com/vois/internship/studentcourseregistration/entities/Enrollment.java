package com.vois.internship.studentcourseregistration.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "ENROLLMENT")
@Getter
@Setter
public class Enrollment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "STUDENT_ID", nullable = false)
  private Student student;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "COURSE_ID", nullable = false)
  // DEMO: For batch fetching @ManyToOne in Hibernate 7.1+:
  // Put @BatchSize on the Course entity class, NOT here!
  // See Course.java for the annotation location.
  private Course course;

  @Column(name = "ENROLLMENT_DATE", nullable = false)
  private Instant enrollmentDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false, length = 30)
  private EnrollmentStatus status;
}