package com.vois.internship.studentcourseregistration.dto;

import java.time.Instant;

public record StudentResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    Instant registrationDate
) {}
