package com.vois.internship.studentcourseregistration.controller;

import com.vois.internship.studentcourseregistration.api.AdminsApi;
import com.vois.internship.studentcourseregistration.dto.AdminResponse;
import com.vois.internship.studentcourseregistration.dto.CreateAdminRequest;
import com.vois.internship.studentcourseregistration.dto.PatchAdminRequest;
import com.vois.internship.studentcourseregistration.dto.UpdateAdminRequest;
import com.vois.internship.studentcourseregistration.service.AdminService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Admin operations.
 * Implements the OpenAPI-generated AdminsApi interface.
 * 
 * Note: Authorization will be implemented when Spring Security is added.
 * These endpoints currently have no access control.
 */
@RestController
@RequiredArgsConstructor
public class AdminController implements AdminsApi {

  private final AdminService adminService;

  @Override
  public ResponseEntity<AdminResponse> createAdmin(CreateAdminRequest createAdminRequest) {
    AdminResponse response = adminService.createAdmin(createAdminRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  public ResponseEntity<List<AdminResponse>> listAdmins() {
    List<AdminResponse> admins = adminService.getAllAdmins();
    return ResponseEntity.ok(admins);
  }

  @Override
  public ResponseEntity<AdminResponse> getAdminById(Long adminId) {
    AdminResponse response = adminService.getAdminById(adminId);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<AdminResponse> updateAdmin(
      Long adminId, 
      UpdateAdminRequest updateAdminRequest) {
    AdminResponse response = adminService.updateAdmin(adminId, updateAdminRequest);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<AdminResponse> patchAdmin(
      Long adminId, 
      PatchAdminRequest patchAdminRequest) {
    AdminResponse response = adminService.patchAdmin(adminId, patchAdminRequest);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Void> deleteAdmin(Long adminId) {
    adminService.deleteAdmin(adminId);
    return ResponseEntity.noContent().build();
  }
}
