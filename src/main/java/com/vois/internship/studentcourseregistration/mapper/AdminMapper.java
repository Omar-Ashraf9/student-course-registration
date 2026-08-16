package com.vois.internship.studentcourseregistration.mapper;

import com.vois.internship.studentcourseregistration.dto.AdminResponse;
import com.vois.internship.studentcourseregistration.dto.CreateAdminRequest;
import com.vois.internship.studentcourseregistration.dto.PatchAdminRequest;
import com.vois.internship.studentcourseregistration.dto.UpdateAdminRequest;
import com.vois.internship.studentcourseregistration.entities.Admin;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper for Admin entity and DTOs.
 * Handles conversion between JPA entities and OpenAPI-generated DTOs.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AdminMapper {

  /**
   * Maps CreateAdminRequest to Admin entity.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "registrationDate", ignore = true)
  Admin toEntity(CreateAdminRequest request);

  /**
   * Maps Admin entity to AdminResponse DTO.
   * Password is NOT in response DTO (security).
   */
  AdminResponse toResponse(Admin admin);

  /**
   * Updates entity from UpdateAdminRequest (full update).
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "registrationDate", ignore = true)
  void updateEntityFromUpdateRequest(UpdateAdminRequest request, @MappingTarget Admin admin);

  /**
   * Patches entity from PatchAdminRequest (partial update).
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "registrationDate", ignore = true)
  void patchEntity(PatchAdminRequest request, @MappingTarget Admin admin);

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
