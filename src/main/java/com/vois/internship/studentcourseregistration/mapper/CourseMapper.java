package com.vois.internship.studentcourseregistration.mapper;

import com.vois.internship.studentcourseregistration.dto.CourseResponse;
import com.vois.internship.studentcourseregistration.dto.CreateCourseRequest;
import com.vois.internship.studentcourseregistration.dto.PatchCourseRequest;
import com.vois.internship.studentcourseregistration.dto.UpdateCourseRequest;
import com.vois.internship.studentcourseregistration.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper for Course entity and DTOs.
 * Handles conversion between entity and OpenAPI-generated request/response DTOs.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CourseMapper {

  /**
   * Map CreateCourseRequest to Course entity.
   * ID will be set by the database.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "enrollments", ignore = true)
  Course toEntity(CreateCourseRequest request);

  /**
   * Map Course entity to CourseResponse DTO.
   */
  CourseResponse toResponse(Course course);

  /**
   * Update existing Course entity from UpdateCourseRequest (full replacement).
   * ID should not be modified.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "enrollments", ignore = true)
  void updateEntityFromUpdateRequest(UpdateCourseRequest request, @MappingTarget Course course);

  /**
   * Partially update existing Course entity from PatchCourseRequest.
   * Only non-null fields in the request will update the entity.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "enrollments", ignore = true)
  void patchEntity(PatchCourseRequest request, @MappingTarget Course course);
}
