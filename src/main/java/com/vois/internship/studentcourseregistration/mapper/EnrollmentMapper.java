package com.vois.internship.studentcourseregistration.mapper;

import com.vois.internship.studentcourseregistration.dto.CreateEnrollmentRequest;
import com.vois.internship.studentcourseregistration.dto.EnrollmentResponse;
import com.vois.internship.studentcourseregistration.dto.PatchEnrollmentRequest;
import com.vois.internship.studentcourseregistration.entities.Enrollment;
import com.vois.internship.studentcourseregistration.entities.EnrollmentStatus;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper for Enrollment entity and DTOs.
 * Handles conversion between JPA entities and OpenAPI-generated DTOs.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EnrollmentMapper {

  /**
   * Maps Enrollment entity to EnrollmentResponse DTO.
   * Maps nested student.id and course.id to flat studentId and courseId.
   * Converts entity EnrollmentStatus to DTO StatusEnum.
   */
  @Mapping(target = "studentId", source = "student.id")
  @Mapping(target = "courseId", source = "course.id")
  EnrollmentResponse toResponse(Enrollment enrollment);

  /**
   * Patches entity from PatchEnrollmentRequest (partial update).
   * Used for status updates (e.g., withdrawal).
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "student", ignore = true)
  @Mapping(target = "course", ignore = true)
  @Mapping(target = "enrollmentDate", ignore = true)
  void patchEntity(PatchEnrollmentRequest request, @MappingTarget Enrollment enrollment);

  // ===== TYPE CONVERSIONS =====

  /**
   * Converts Instant (entity) to OffsetDateTime (DTO).
   */
  default OffsetDateTime map(Instant instant) {
    return instant == null ? null : instant.atOffset(ZoneOffset.UTC);
  }

  /**
   * Converts OffsetDateTime (DTO) to Instant (entity).
   */
  default Instant map(OffsetDateTime offsetDateTime) {
    return offsetDateTime == null ? null : offsetDateTime.toInstant();
  }

  /**
   * Converts entity EnrollmentStatus to DTO StatusEnum.
   */
  default EnrollmentResponse.StatusEnum mapResponseStatus(EnrollmentStatus status) {
    if (status == null) return null;
    return EnrollmentResponse.StatusEnum.valueOf(status.name());
  }

  /**
   * Converts DTO StatusEnum (from PatchEnrollmentRequest) to entity EnrollmentStatus.
   */
  default EnrollmentStatus mapPatchStatus(PatchEnrollmentRequest.StatusEnum status) {
    if (status == null) return null;
    return EnrollmentStatus.valueOf(status.name());
  }
}
