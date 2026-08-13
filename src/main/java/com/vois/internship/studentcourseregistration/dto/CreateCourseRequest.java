package com.vois.internship.studentcourseregistration.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCourseRequest(
    @NotBlank(message = "Course code is required")
    String code,
    
    @NotBlank(message = "Course title is required")
    String title,
    
    String description,
    
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be greater than 0")
    Integer capacity
) {}
