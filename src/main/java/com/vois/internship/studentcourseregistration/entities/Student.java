package com.vois.internship.studentcourseregistration.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
// Uncomment these for fetching strategy demos:
// import org.hibernate.annotations.Fetch;
// import org.hibernate.annotations.FetchMode;
// import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues.Strategy;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "STUDENT")
@Getter
@Setter
public class Student {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "FIRST_NAME", nullable = false, length = 100)
  private String firstName;

  @Column(name = "LAST_NAME", nullable = false, length = 100)
  private String lastName;

  @Column(name = "EMAIL", nullable = false)
  private String email;

  @Column(name = "REGISTRATION_DATE", nullable = false)
  private Instant registrationDate;

  @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
  // DEMO: Uncomment ONE of these to demonstrate fetching strategies:
  // NOTE: These show HOW to fetch (mechanism), not WHEN (timing)
  
  // === LAZY + different mechanisms ===
  // @Fetch(FetchMode.SELECT)        // LAZY + SELECT: fetch when accessed, separate queries
  // @Fetch(FetchMode.SUBSELECT)     // LAZY + SUBSELECT: fetch when accessed, one subselect
  // @Fetch(FetchMode.JOIN)          // LAZY + JOIN: actually ignores LAZY! fetches immediately with join
  // @BatchSize(size = 10)           // LAZY + BATCH: fetch when accessed, batched IN clause
  
  // === EAGER + different mechanisms (uncomment BOTH lines) ===
  // @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)  // Change to EAGER
  // @Fetch(FetchMode.SELECT)        // EAGER + SELECT: fetch immediately, separate query
  // Or:
  // @Fetch(FetchMode.JOIN)          // EAGER + JOIN: fetch immediately with join
  // Or:
  // @BatchSize(size = 10)           // EAGER + BATCH: fetch immediately, batched
  
  private List<Enrollment> enrollments = new ArrayList<>();
}