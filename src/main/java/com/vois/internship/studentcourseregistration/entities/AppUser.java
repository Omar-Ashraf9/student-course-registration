package com.vois.internship.studentcourseregistration.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/**
 * Base entity for all system users using SINGLE_TABLE inheritance.
 * Subclasses: Student (ROLE='STUDENT'), Admin (ROLE='ADMIN')
 * 
 * Note: Password field is included for future Spring Security implementation.
 * Password encoding and authentication will be implemented in a later task.
 * For now, password is stored as plain text - DO NOT use in production.
 */
@Entity
@Table(name = "APP_USER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name = "ROLE",
    discriminatorType = DiscriminatorType.STRING
)
@Getter
@Setter
public abstract class AppUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "FIRST_NAME", nullable = false, length = 100)
  private String firstName;

  @Column(name = "LAST_NAME", nullable = false, length = 100)
  private String lastName;

  @Column(name = "EMAIL", nullable = false, unique = true, length = 255)
  private String email;

  /**
   * Password field for future Spring Security integration.
   * 
   * IMPORTANT: This is currently stored as plain text.
   * Password encoding with BCryptPasswordEncoder will be implemented
   * when Spring Security is added in a future task.
   * 
   * DO NOT use this in production without proper encoding.
   */
  @Column(name = "PASSWORD", nullable = false, length = 255)
  private String password;

  @Column(name = "REGISTRATION_DATE", nullable = false)
  private Instant registrationDate;
}
