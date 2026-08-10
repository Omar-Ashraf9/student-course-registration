package com.vois.internship.studentcourseregistration.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
// Uncomment for batch fetching demo (Hibernate 7.1+):
// import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "COURSE")
@Getter
@Setter
// DEMO: Uncomment this to batch-fetch Course entities when accessed via Enrollment.course
// In Hibernate 7.1+, @BatchSize goes on the entity class, not on the @ManyToOne property
// @BatchSize(size = 10)
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "CODE", nullable = false, length = 50)
  private String code;

  @Column(name = "TITLE", nullable = false, length = 150)
  private String title;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @Column(name = "CAPACITY", nullable = false)
  private Integer capacity;

  @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
  private List<Enrollment> enrollments = new ArrayList<>();
}