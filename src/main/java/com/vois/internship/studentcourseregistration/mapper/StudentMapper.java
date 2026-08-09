package com.vois.internship.studentcourseregistration.mapper;

import com.vois.internship.studentcourseregistration.dto.StudentDto;
import com.vois.internship.studentcourseregistration.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StudentMapper {

  StudentDto toDto(Student student);
}
