package com.mmc.nts.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mmc.nts.entity.Project;
import com.mmc.nts.model.ProjectSearchCriteria;
import com.mmc.nts.model.Response;

@Service
public interface ProjectService {

	public List<Project> findAllByUserName(String userName);
	
	ResponseEntity<Response> createProject(Project project);
	

	ResponseEntity<Response> updateProject(Project project);

	ResponseEntity<Response> updateStatus(String projectID, String status);

	ResponseEntity<Response> findByProjectID(String projectID);

	ResponseEntity<Response> findAll();

	ResponseEntity<Response> findByProjectName(String project);

	List<Project> findAllByStatus(String status);

	Project findProjectByName(String projectName);

	List<Project> findAllByRoleName(String roleName);

	List<Project> findAllByUserNameAndStatus(String userName, String status);

	public ResponseEntity<Response> findBySearchCriteria(ProjectSearchCriteria searchCriteria);
}
