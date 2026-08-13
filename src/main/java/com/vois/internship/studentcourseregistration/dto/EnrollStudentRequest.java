package com.vois.internship.studentcourseregistration.dto;

import jakarta.validation.constraints.NotNull;

public record EnrollStudentRequest(
    @NotNull(message = "Student ID is required")
    Long studentId,
    
    @NotNull(message = "Course ID is required")
    Long courseId
) {}
