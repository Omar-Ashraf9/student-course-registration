package com.vois.internship.studentcourseregistration.service.impl;

import com.vois.internship.studentcourseregistration.dto.AdminResponse;
import com.vois.internship.studentcourseregistration.dto.CreateAdminRequest;
import com.vois.internship.studentcourseregistration.dto.PatchAdminRequest;
import com.vois.internship.studentcourseregistration.dto.UpdateAdminRequest;
import com.vois.internship.studentcourseregistration.entities.Admin;
import com.vois.internship.studentcourseregistration.exception.AdminNotFoundException;
import com.vois.internship.studentcourseregistration.exception.DuplicateAdminException;
import com.vois.internship.studentcourseregistration.mapper.AdminMapper;
import com.vois.internship.studentcourseregistration.repository.AdminRepository;
import com.vois.internship.studentcourseregistration.service.AdminService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for Admin operations.
 * 
 * Note: Authorization will be implemented when Spring Security is added.
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final AdminRepository adminRepository;
  private final AdminMapper adminMapper;

  @Override
  @Transactional
  public AdminResponse createAdmin(CreateAdminRequest request) {
    // Check for duplicate email
    if (adminRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateAdminException(request.getEmail());
    }

    // Create and save admin
    Admin admin = adminMapper.toEntity(request);
    admin.setRegistrationDate(Instant.now());

    Admin savedAdmin = adminRepository.save(admin);
    return adminMapper.toResponse(savedAdmin);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AdminResponse> getAllAdmins() {
    return adminRepository.findAll().stream()
        .map(adminMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public AdminResponse getAdminById(Long id) {
    Admin admin = adminRepository.findById(id)
        .orElseThrow(() -> new AdminNotFoundException(id));
    return adminMapper.toResponse(admin);
  }

  @Override
  @Transactional
  public AdminResponse updateAdmin(Long id, UpdateAdminRequest request) {
    Admin admin = adminRepository.findById(id)
        .orElseThrow(() -> new AdminNotFoundException(id));

    adminMapper.updateEntityFromUpdateRequest(request, admin);
    Admin savedAdmin = adminRepository.save(admin);
    return adminMapper.toResponse(savedAdmin);
  }

  @Override
  @Transactional
  public AdminResponse patchAdmin(Long id, PatchAdminRequest request) {
    Admin admin = adminRepository.findById(id)
        .orElseThrow(() -> new AdminNotFoundException(id));

    adminMapper.patchEntity(request, admin);
    Admin savedAdmin = adminRepository.save(admin);
    return adminMapper.toResponse(savedAdmin);
  }

  @Override
  @Transactional
  public void deleteAdmin(Long id) {
    if (!adminRepository.existsById(id)) {
      throw new AdminNotFoundException(id);
    }
    adminRepository.deleteById(id);
  }
}
