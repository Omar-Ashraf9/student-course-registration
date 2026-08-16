package com.vois.internship.studentcourseregistration.service;

import com.vois.internship.studentcourseregistration.dto.AdminResponse;
import com.vois.internship.studentcourseregistration.dto.CreateAdminRequest;
import com.vois.internship.studentcourseregistration.dto.PatchAdminRequest;
import com.vois.internship.studentcourseregistration.dto.UpdateAdminRequest;
import java.util.List;

/**
 * Service interface for Admin operations.
 * 
 * Note: Authorization will be implemented when Spring Security is added.
 * These endpoints currently have no access control.
 */
public interface AdminService {

  /**
   * Create a new admin.
   * @param request the admin creation request
   * @return the created admin response
   * @throws com.vois.internship.studentcourseregistration.exception.DuplicateAdminException if email already exists
   */
  AdminResponse createAdmin(CreateAdminRequest request);

  /**
   * Get all admins.
   * @return list of all admins
   */
  List<AdminResponse> getAllAdmins();

  /**
   * Get admin by ID.
   * @param id the admin ID
   * @return the admin response
   * @throws com.vois.internship.studentcourseregistration.exception.AdminNotFoundException if admin not found
   */
  AdminResponse getAdminById(Long id);

  /**
   * Fully update an admin (PUT).
   * @param id the admin ID
   * @param request the update request
   * @return the updated admin response
   * @throws com.vois.internship.studentcourseregistration.exception.AdminNotFoundException if admin not found
   */
  AdminResponse updateAdmin(Long id, UpdateAdminRequest request);

  /**
   * Partially update an admin (PATCH).
   * @param id the admin ID
   * @param request the patch request
   * @return the updated admin response
   * @throws com.vois.internship.studentcourseregistration.exception.AdminNotFoundException if admin not found
   */
  AdminResponse patchAdmin(Long id, PatchAdminRequest request);

  /**
   * Delete an admin.
   * @param id the admin ID
   * @throws com.vois.internship.studentcourseregistration.exception.AdminNotFoundException if admin not found
   */
  void deleteAdmin(Long id);
}
