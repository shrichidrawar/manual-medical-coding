package com.mmc.nts.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import com.mmc.nts.entity.Project;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.apache.commons.lang3.StringUtils;
import com.mmc.nts.entity.User;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.domain.Page;
import com.mmc.nts.model.ProjectSearchCriteria;
import org.springframework.data.mongodb.core.query.Criteria;
import com.mmc.nts.model.Response;
import com.mmc.nts.model.ResponseBuilder;
import com.mmc.nts.model.response.ProjectPageResponse;
import com.mmc.nts.repository.ProjectRepository;
import com.mmc.nts.repository.UserRepository;
import com.mmc.nts.service.ProjectService;
import com.mmc.nts.utility.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.PageRequest;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.data.mongodb.core.query.Query;

@Service
public class ProjectServiceImpl implements ProjectService  {

	private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ResponseBuilder responseBuilder;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public List<Project> findAllByUserName(String userName) {
		logger.debug("find projects with email associated with user - " + userName);
		return projectRepository.findByRoles_Users(userName);
	}

	@Override
	public ResponseEntity<Response> createProject(Project project) {
		logger.debug("createProject invoked. ");

		Optional<Project> projectByName = projectRepository.findByProjectIgnoreCase(project.getProject());

		String message = null;
		if (projectByName.isPresent()) {
			message = String.format("Project exists with name %s", project.getProject());
			logger.debug(message);
			return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E", message, null),
					HttpStatus.OK);
		}

		Optional<User> UserByEmail = userRepository.findByEmailIgnoreCase(project.getLead());

		if (!UserByEmail.isPresent()) {
			message = String.format("Lead %s does not exist as a user.", project.getLead());
			logger.debug(message);
			return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E", message, null),
					HttpStatus.OK);
		}
		project = projectRepository.save(project);
		logger.debug("Project created with id %s", project.getId());

		logger.debug("leaving createProject");
		return new ResponseEntity<Response>(responseBuilder.createResponse("SU_DE_204", "S", message, project),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> updateProject(Project project) {

		logger.debug("updateProject invoked. ");

		Optional<Project> projectByProjectId = projectRepository.findById(project.getId());
		String message = null;
		if (!projectByProjectId.isPresent()) {
			message = String.format("Project does not exist with ID %s", project.getId());
			logger.debug(message);
			return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E", message, null),
					HttpStatus.OK);
		}
		Project projectFromDB = projectByProjectId.get();
		if (!projectFromDB.getProject().equals(project.getProject())) {
			Optional<Project> projectByName = projectRepository.findByProjectIgnoreCase(project.getProject());

			if (projectByName.isPresent()) {
				message = String.format("Project exists with name %s", project.getProject());
				logger.debug(message);
				return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E", message, null),
						HttpStatus.OK);
			}
			projectFromDB.setProject(project.getProject());
		}

		projectFromDB.setStartDate(project.getStartDate());

		projectFromDB.setEndDate(project.getEndDate());

		if (!projectFromDB.getLead().equals(project.getLead())) {
			Optional<User> UserByEmail = userRepository.findByEmailIgnoreCase(project.getLead());
			if (!UserByEmail.isPresent()) {
				message = String.format("Lead %s does not exists as user", project.getLead());
				logger.debug(message);

				return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E", message, null),
						HttpStatus.OK);
			}
		}
		projectFromDB.setLead(project.getLead());

		projectFromDB.setStatus(project.getStatus());

		projectFromDB.setRoles(project.getRoles());

		projectFromDB.setUpdated(DateUtil.getDateTimeStamp());

		project = projectRepository.save(projectFromDB);

		logger.debug("leaving updateProject");
		return new ResponseEntity<Response>(
				responseBuilder.createResponse("SU_DE_204", "S", "Project updated.", project), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> updateStatus(String projectID, String status) {
		logger.debug("updateStatus invoked");
		Optional<Project> projectByProjectId = projectRepository.findById(projectID);
		if (!projectByProjectId.isPresent()) {
			logger.debug("Project does not exist with ID - " + projectID);
			return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E",
					"Project does not exist with ID - " + projectID, null), HttpStatus.OK);
		}
		Project project = projectByProjectId.get();
		if (!project.getStatus().equals(status)) {
			project.setStatus(status);
			project.setUpdated(DateUtil.getDateTimeStamp());
			project = projectRepository.save(project);
			logger.debug("Status updated");
		}
		logger.debug("leaving updateStatus");
		return new ResponseEntity<Response>(responseBuilder.createResponse("SU_DE_204", "S", "Status updated", project),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> findByProjectID(String projectID) {
		logger.debug("findByProjectID invoked. ");
		Optional<Project> projectByProjectId = projectRepository.findById(projectID);
		if (!projectByProjectId.isPresent()) {
			logger.debug("Project does not exist with ID-  " + projectID);
			return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E",
					"Project does not exist with ID - " + projectID, null), HttpStatus.OK);
		}
		Project projectFromDB = projectByProjectId.get();
		logger.debug("Fetched project by ID - " + projectFromDB.getId());
		logger.debug("leaving findByProjectID");
		return new ResponseEntity<Response>(responseBuilder.createResponse("SU_DE_204", "S",
				"Fetched project for id - " + projectID, projectFromDB), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> findAll() {
		logger.debug("findAll invoked");
		List<Project> projects = projectRepository.findAll();
		if (CollectionUtils.isEmpty(projects)) {
			logger.debug("No projects found");
			return new ResponseEntity<Response>(
					responseBuilder.createResponse("SU_DE_204", "S", "No projects found.", null), HttpStatus.OK);
		}
		logger.debug("Fetched projects list");
		logger.debug("leaving findAll");
		return new ResponseEntity<Response>(
				responseBuilder.createResponse("SU_DE_204", "S", "Fetched projects list", projects), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> findByProjectName(String project) {
		logger.debug("findByProject invoked. ");
		Optional<Project> ProjectByName = projectRepository.findByProjectIgnoreCase(project);
		if (!ProjectByName.isPresent()) {
			logger.debug("Project does not exist with name - " + project);
			return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E",
					"Project does not exist with name - " + project, null), HttpStatus.OK);
		}
		Project projectFromDB = ProjectByName.get();
		logger.debug("Fetched project by name - " + project);
		logger.debug("leaving findByProject");
		return new ResponseEntity<Response>(
				responseBuilder.createResponse("SU_DE_204", "S", "Fetched project by name - " + project, projectFromDB),
				HttpStatus.OK);
	}

	@Override
	public List<Project> findAllByStatus(String status) {
		logger.debug("find projects with status enabled");
		return projectRepository.findByStatus(status);
	}

	@Override
	public Project findProjectByName(String projectName) {
		Optional<Project> project = projectRepository.findByProjectIgnoreCase(projectName);
		return project.isPresent() ? project.get() : null;
	}

	@Override
	public List<Project> findAllByRoleName(String roleName) {
		logger.debug("find projects with Role name - " + roleName);
		return projectRepository.findByRoles_Role(roleName);
	}

	@Override
	public List<Project> findAllByUserNameAndStatus(String userName, String status) {
		return projectRepository.findByStatusAndRoles_UsersIgnoreCase(status, userName);
	}

	@Override
	public ResponseEntity<Response> findBySearchCriteria(ProjectSearchCriteria searchCriteria) {
		logger.debug("findBySearchCriteria invoked");
		String message = null;

		List<Project> projects = null;

		Pageable paging;
		Page<Project> projectPage;

		if (null == searchCriteria.getSortingField()) {
			searchCriteria.setSortingField("updated");
		}
		if (searchCriteria.isSortingOrder()) {
			paging = PageRequest.of(searchCriteria.getPage(), searchCriteria.getSize(), Sort.Direction.ASC,
					searchCriteria.getSortingField());
		} else {
			paging = PageRequest.of(searchCriteria.getPage(), searchCriteria.getSize(), Sort.Direction.DESC,
					searchCriteria.getSortingField());
		}

		Query query = new Query();
		List<Criteria> criteriaList = getSearchCriteria(searchCriteria);

		if (!CollectionUtils.isEmpty(criteriaList)) {
			query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
		}

		long count = mongoTemplate.count(query, Project.class);
		query.with(paging);
		query.collation(Collation.of("en").strength(Collation.ComparisonLevel.secondary()));
		List<Project> projectList = mongoTemplate.find(query, Project.class);

		projectPage = new PageImpl<>(projectList, paging, count);

		if (projectPage.isEmpty()) {
			return new ResponseEntity<>(responseBuilder.createResponse("SU_DE_204", "E", "No page found.", null),
					HttpStatus.OK);
		}

		projects = projectPage.getContent();

		ProjectPageResponse response = new ProjectPageResponse();

		response.setTotalElementOfPage(projectPage.getNumberOfElements());
		response.setTotalRecords(projectPage.getTotalElements());
		response.setTotalPages(projectPage.getTotalPages());
		response.setCurrentPage(projectPage.getNumber());
		response.setProjects(projects);

		message = String.format("Project records found");
		logger.debug(message);
		logger.debug("leaving findBySearchCriteria");

		return new ResponseEntity<>(responseBuilder.createResponse("SU_DE_200", "S", message, response),
				HttpStatus.OK);
	}
	
	private List<Criteria> getSearchCriteria(ProjectSearchCriteria searchCriteria) {
		List<Criteria> criteriaList = new ArrayList<>();
		if (StringUtils.isNotBlank(searchCriteria.getProject())) {
			criteriaList.add(Criteria.where("project").regex(".*" + searchCriteria.getProject() + ".*", "i"));
		}
		if (StringUtils.isNotBlank(searchCriteria.getStatus())) {
			criteriaList.add(Criteria.where("status").is(searchCriteria.getStatus()));
		}
		return criteriaList;
	}

}
