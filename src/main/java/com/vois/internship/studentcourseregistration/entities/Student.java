package com.vois.internship.studentcourseregistration.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
// Uncomment these for fetching strategy demos:
// import org.hibernate.annotations.Fetch;
// import org.hibernate.annotations.FetchMode;
// import org.hibernate.annotations.BatchSize;

/**
 * Student entity representing students in the course registration system.
 * Extends AppUser with SINGLE_TABLE inheritance using discriminator value 'STUDENT'.
 * 
 * Student-specific attributes:
 * - enrollments: Collection of course enrollments for this student
 */
@Entity
@DiscriminatorValue("STUDENT")
@Getter
@Setter
public class Student extends AppUser {

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