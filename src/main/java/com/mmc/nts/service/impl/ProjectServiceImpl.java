package com.mmc.nts.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mmc.nts.entity.Project;
import com.mmc.nts.model.ProjectSearchCriteria;
import com.mmc.nts.model.Response;
import com.mmc.nts.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService  {

	@Override
	public List<Project> findAllByUserName(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Response> createProject(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Response> updateProject(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Response> updateStatus(String projectID, String status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Response> findByProjectID(String projectID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Response> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Response> findByProjectName(String project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Project> findAllByStatus(String status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Project findProjectByName(String projectName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Project> findAllByRoleName(String roleName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Project> findAllByUserNameAndStatus(String userName, String status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Response> findBySearchCriteria(ProjectSearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

}
