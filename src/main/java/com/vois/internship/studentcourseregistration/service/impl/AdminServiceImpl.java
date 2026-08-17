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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for Admin operations.
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final AdminRepository adminRepository;
  private final AdminMapper adminMapper;
  private final PasswordEncoder passwordEncoder;

  /**
   * Create admin - ADMIN ONLY.
   * Requires existing admin to create new admin.
   * Password is BCrypt-encoded before persistence.
   */
  @Override
  @PreAuthorize("hasRole('ADMIN')")
  @Transactional
  public AdminResponse createAdmin(CreateAdminRequest request) {
    // Check for duplicate email
    if (adminRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateAdminException(request.getEmail());
    }

    // Create admin entity (MapStruct handles basic mapping)
    Admin admin = adminMapper.toEntity(request);
    
    // Encode password with BCrypt
    admin.setPassword(passwordEncoder.encode(request.getPassword()));
    
    // Set registration date
    admin.setRegistrationDate(Instant.now());

    Admin savedAdmin = adminRepository.save(admin);
    return adminMapper.toResponse(savedAdmin);
  }

  /**
   * Get all admins - ADMIN ONLY.
   */
  @Override
  @PreAuthorize("hasRole('ADMIN')")
  @Transactional(readOnly = true)
  public List<AdminResponse> getAllAdmins() {
    return adminRepository.findAll().stream()
        .map(adminMapper::toResponse)
        .toList();
  }

  /**
   * Get admin by ID - ADMIN (self only).
   */
  @Override
  @PreAuthorize("hasRole('ADMIN') and #id == principal.id")
  @Transactional(readOnly = true)
  public AdminResponse getAdminById(Long id) {
    Admin admin = adminRepository.findById(id)
        .orElseThrow(() -> new AdminNotFoundException(id));
    return adminMapper.toResponse(admin);
  }

  /**
   * Update admin (PUT) - ADMIN (self only).
   * Password is BCrypt-encoded if provided.
   */
  @Override
  @PreAuthorize("hasRole('ADMIN') and #id == principal.id")
  @Transactional
  public AdminResponse updateAdmin(Long id, UpdateAdminRequest request) {
    Admin admin = adminRepository.findById(id)
        .orElseThrow(() -> new AdminNotFoundException(id));

    // Map editable fields
    adminMapper.updateEntityFromUpdateRequest(request, admin);
    
    // Encode new password
    admin.setPassword(passwordEncoder.encode(request.getPassword()));

    Admin savedAdmin = adminRepository.save(admin);
    return adminMapper.toResponse(savedAdmin);
  }

  /**
   * Partially update admin (PATCH) - ADMIN (self only).
   * Password is BCrypt-encoded if provided, unchanged if omitted.
   */
  @Override
  @PreAuthorize("hasRole('ADMIN') and #id == principal.id")
  @Transactional
  public AdminResponse patchAdmin(Long id, PatchAdminRequest request) {
    Admin admin = adminRepository.findById(id)
        .orElseThrow(() -> new AdminNotFoundException(id));

    // Map provided fields only (MapStruct ignores nulls)
    adminMapper.patchEntity(request, admin);
    
    // Encode password if provided in patch
    if (request.getPassword() != null) {
      admin.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    Admin savedAdmin = adminRepository.save(admin);
    return adminMapper.toResponse(savedAdmin);
  }

  /**
   * Delete admin - ADMIN (self only).
   */
  @Override
  @PreAuthorize("hasRole('ADMIN') and #id == principal.id")
  @Transactional
  public void deleteAdmin(Long id) {
    if (!adminRepository.existsById(id)) {
      throw new AdminNotFoundException(id);
    }
    adminRepository.deleteById(id);
  }
}
