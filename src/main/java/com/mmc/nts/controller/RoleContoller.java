package com.mmc.nts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mmc.nts.entity.Role;
import com.mmc.nts.model.Response;
import com.mmc.nts.model.RoleSearchCriteria;
import com.mmc.nts.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/role")
public class RoleContoller {

	@Autowired
	private RoleService roleService;
	
	@PostMapping()
	public ResponseEntity<Response> createRole(@Valid @RequestBody Role role) {
		return roleService.createRole(role);
	}
	
	@PutMapping()
	public ResponseEntity<Response> updateRole(@Valid @RequestBody Role role) {
		return roleService.updateRole(role);
	}
	
	@DeleteMapping(value = "/status/{roleId}/{status}")
	public ResponseEntity<Response> updateStatus(@PathVariable String roleId, @PathVariable String status) {
		return roleService.updateStatus(roleId, status);
	}
	
	@GetMapping()
	public ResponseEntity<Response> fetchRoleList() {
		return roleService.findAll();
	}
	
	@GetMapping(value = "/id/{roleID}")
	public ResponseEntity<Response> fetchByRoleID(@PathVariable String roleID) {
		return roleService.findByRoleID(roleID);
	}
	
	@GetMapping(value = "/role/{role}")
	public ResponseEntity<Response> fetchUserByEmail(@PathVariable String role) {
		return roleService.findByRole(role);
	}
	
	@GetMapping(value = "/enabled")
	public ResponseEntity<Response> findAllByStatusEnabled() {
		return roleService.findAllByStatusEnabled();
	}
	
	@PostMapping(value = "/search_criteria")
	public ResponseEntity<Response> findBySearchCriteria(@RequestBody RoleSearchCriteria searchCriteria) {
		return roleService.findBySearchCriteria(searchCriteria);
	}
}
