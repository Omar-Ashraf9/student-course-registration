package com.vois.internship.studentcourseregistration.mapper;

import com.vois.internship.studentcourseregistration.dto.CreateStudentRequest;
import com.vois.internship.studentcourseregistration.dto.PatchStudentRequest;
import com.vois.internship.studentcourseregistration.dto.StudentResponse;
import com.vois.internship.studentcourseregistration.dto.UpdateStudentRequest;
import com.vois.internship.studentcourseregistration.entities.Student;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper for Student entity and DTOs.
 * Handles conversion between JPA entities and OpenAPI-generated DTOs.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StudentMapper {

  /**
   * Maps CreateStudentRequest to Student entity.
   * Ignores id (generated) and registrationDate (set by service).
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "registrationDate", ignore = true)
  @Mapping(target = "enrollments", ignore = true)
  Student toEntity(CreateStudentRequest request);

  /**
   * Maps Student entity to StudentResponse DTO.
   * Password is NOT in response DTO (security).
   * Instant is converted to OffsetDateTime automatically via default methods below.
   */
  StudentResponse toResponse(Student student);

  /**
   * Updates entity from UpdateStudentRequest (full update).
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "registrationDate", ignore = true)
  @Mapping(target = "enrollments", ignore = true)
  void updateEntityFromUpdateRequest(UpdateStudentRequest request, @MappingTarget Student student);

  /**
   * Patches entity from PatchStudentRequest (partial update).
   * Null values are ignored per class-level NullValuePropertyMappingStrategy.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "registrationDate", ignore = true)
  @Mapping(target = "enrollments", ignore = true)
  void patchEntity(PatchStudentRequest request, @MappingTarget Student student);

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
}
