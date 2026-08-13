package com.vois.internship.studentcourseregistration.dto;

import com.vois.internship.studentcourseregistration.entities.EnrollmentStatus;
import java.time.Instant;

public record EnrollmentResponse(
    Long id,
    Long studentId,
    String studentName,
    Long courseId,
    String courseCode,
    String courseTitle,
    EnrollmentStatus status,
    Instant enrollmentDate
) {}
