package com.vois.internship.studentcourseregistration.dto;

public record CourseResponse(
    Long id,
    String code,
    String title,
    String description,
    Integer capacity
) {}
