package com.mmc.nts.service;

import org.springframework.stereotype.Service;

import com.mmc.nts.entity.Role;
import com.mmc.nts.model.Response;
import com.mmc.nts.model.RoleSearchCriteria;

import org.springframework.http.ResponseEntity;

@Service
public interface RoleService {

	public ResponseEntity<Response> createRole(Role role);
	
	public ResponseEntity<Response> updateRole(Role role);
	
	public ResponseEntity<Response> updateStatus(String roleID, String status);
	
	public ResponseEntity<Response> findAll();
	
	public ResponseEntity<Response> findByRoleID(String userId);
	
	public ResponseEntity<Response> findByRole(String role);
	
	public ResponseEntity<Response> findAllByStatusEnabled();
	
	public ResponseEntity<Response> findBySearchCriteria(RoleSearchCriteria searchCriteria);
}
