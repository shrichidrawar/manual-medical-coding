package com.mmc.nts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mmc.nts.entity.Project;
import com.mmc.nts.model.ProjectSearchCriteria;
import com.mmc.nts.model.Response;
import com.mmc.nts.service.ProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/admin/project")
public class ProjectContoller {

	@Autowired
	private ProjectService projectService;
	
	@PostMapping()
	public ResponseEntity<Response> createProject(@Valid @RequestBody Project project) {
		return projectService.createProject(project);
	}

	@PutMapping()
	public ResponseEntity<Response> updateProject(@Valid @RequestBody Project project) {
		return projectService.updateProject(project);
	}
	
	@DeleteMapping(value = "/status/{projectId}/{status}")
	public ResponseEntity<Response> updateStatus(@PathVariable String projectId, @PathVariable String status) {
		// not in use
		return projectService.updateStatus(projectId, status);
	}
	
	@GetMapping()
	public ResponseEntity<Response> fetchProjectList() {
		return projectService.findAll();
	}
	
	@GetMapping(value = "/id/{projectId}")
	public ResponseEntity<Response> fetchProjectById(@PathVariable String projectId) {
		return projectService.findByProjectID(projectId);
	}
	
	@GetMapping(value = "/project/{Project}")
	public ResponseEntity<Response> fetchProjectByProjectName(@PathVariable String Project) {
		return projectService.findByProjectName(Project);
	}

	@PostMapping(value = "/search_criteria")
	public ResponseEntity<Response> findBySearchCriteria(@RequestBody ProjectSearchCriteria searchCriteria) {
		return projectService.findBySearchCriteria(searchCriteria);
	}
}
