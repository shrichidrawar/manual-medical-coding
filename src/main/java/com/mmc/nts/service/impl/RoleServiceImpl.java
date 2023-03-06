package com.mmc.nts.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mmc.nts.constants.Constants;
import com.mmc.nts.entity.Project;
import com.mmc.nts.entity.ProjectRoles;
import org.springframework.data.domain.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.domain.PageImpl;
import com.mmc.nts.entity.Role;
import org.springframework.data.domain.Sort;
import com.mmc.nts.model.Response;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import com.mmc.nts.model.ResponseBuilder;
import com.mmc.nts.model.RoleSearchCriteria;
import com.mmc.nts.model.response.RolePageResponse;
import com.mmc.nts.repository.ProjectRepository;
import com.mmc.nts.repository.RoleRepository;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mmc.nts.service.ProjectService;
import com.mmc.nts.service.RoleService;
import org.springframework.data.domain.Pageable;
import com.mmc.nts.utility.DateUtil;
import org.springframework.util.CollectionUtils;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	

	@Autowired
	private ResponseBuilder responseBuilder;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public ResponseEntity<Response> createRole(Role role) {
//		logger.debug("createRole invoked");
		Optional<Role> roleByRoleType = roleRepository.findByRoleIgnoreCase(role.getRole());
		String message = null;
		if (roleByRoleType.isPresent()) {
			message = String.format("Role exist with name %s", role.getRole());
//			logger.debug(message);
			return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E", message, null),
					HttpStatus.OK);
		}
		role.setCreated(DateUtil.getDateTimeStamp());
		role.setUpdated(DateUtil.getDateTimeStamp());
		role = roleRepository.save(role);
		message = String.format("Role created with id %s", role.getId());
//		logger.debug(message);
//		logger.debug("Leaving createRole");
		return new ResponseEntity<Response>(responseBuilder.createResponse("SU_DE_204", "S", message, role),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> updateRole(Role role) {
//		logger.debug("updateRole invoked");
		Optional<Role> roleByRoleId = roleRepository.findById(role.getId());
		String message = null;
		if (!roleByRoleId.isPresent()) {
			message = String.format("Role does not exist with ID %s", role.getId());
//			logger.debug(message);
			return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E", message, null),
					HttpStatus.OK);
		}
		Role roleFromDB = roleByRoleId.get();
		String roleEmailFromDB = roleFromDB.getRole();
		if (!roleEmailFromDB.equalsIgnoreCase(role.getRole())) {
			Optional<Role> roleByRoleName = roleRepository.findByRoleIgnoreCase(role.getRole());
			if (roleByRoleName.isPresent()) {
				message = String.format("Role exists with name %s", role.getRole());
//				logger.debug(message);
				return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E", message, null),
						HttpStatus.OK);
			}
			roleFromDB.setRole(role.getRole());
		}
		roleFromDB.setStatus(role.getStatus());
		roleFromDB.setPermissions(role.getPermissions());
		roleFromDB.setUpdated(DateUtil.getDateTimeStamp());
		role = roleRepository.save(roleFromDB);
		message = String.format("Role updated with ID %s", role.getId());
//		logger.debug(message);
		if (!roleEmailFromDB.equals(role.getRole())) {
			List<Project> projects = projectService.findAllByRoleName(roleEmailFromDB);
			if (!CollectionUtils.isEmpty(projects)) {
				for (Project project : projects) {
					List<ProjectRoles> roles = project.getRoles();
					for (ProjectRoles projectRole : roles) {
						if (projectRole.getRole().equals(roleEmailFromDB)) {
							projectRole.setRole(role.getRole());
							project.setUpdated(DateUtil.getDateTimeStamp());
						}
					}
					project.setRoles(roles);
				}
				projectRepository.saveAll(projects);
//				logger.debug("Projects updated with role {}", role.getRole());
			} else {
//				logger.debug("No projects found with role {}", role.getRole());
			}
		}
//		logger.debug("Leaving updateRole");
		return new ResponseEntity<Response>(responseBuilder.createResponse("SU_DE_204", "S", "Role updated", role),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> updateStatus(String roleID, String status) {
//		logger.debug("updateStatus invoked");

		Optional<Role> roleByRoleId = roleRepository.findById(roleID);

		if (!roleByRoleId.isPresent()) {
//			logger.debug("Role does not exist with ID - " + roleID);

			return new ResponseEntity<Response>(
					responseBuilder.createResponse("EX_DE_304", "E", "Role does not exist ID - " + roleID, null),
					HttpStatus.OK);
		}
		Role roleFromDB = roleByRoleId.get();

		if (!roleFromDB.getStatus().equalsIgnoreCase(status)) {
			roleFromDB.setStatus(status);
		}
		roleFromDB.setUpdated(DateUtil.getDateTimeStamp());

		Role role = roleRepository.save(roleFromDB);
//		logger.debug("Status updated for role ", roleID);
//		logger.debug("Leaving updateStatus");

		return new ResponseEntity<Response>(responseBuilder.createResponse("SU_DE_204", "S", "Status updated", role),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> findAll() {
	
		List<Role> roles = roleRepository.findAll();

		if (CollectionUtils.isEmpty(roles)) {
			
			return new ResponseEntity<Response>(
					responseBuilder.createResponse("SU_DE_204", "S", "No roles found.", null), HttpStatus.OK);
		}

		excludePlatformAdminRole(roles);
//
//		logger.debug("Fetched roles - " + roles.size());
//		logger.debug("Leaving findAll");
		return new ResponseEntity<Response>(responseBuilder.createResponse("SU_DE_204", "S", "Fetched roles", roles),
				HttpStatus.OK);
	}
	private void excludePlatformAdminRole(List<Role> roles) {
		for (Role role : roles) {
			if (role.getRole().equals(Constants.PLATFORM_ADMIN_ROLE)) {
				roles.remove(role);
				break;
			}
		}
	}

	@Override
	public ResponseEntity<Response> findByRoleID(String roleID) {
//		logger.debug("findByRoleID invoked");

		Optional<Role> roleByRoleId = roleRepository.findById(roleID);

		String message = null;

		if (!roleByRoleId.isPresent()) {
			message = String.format("Role does not exist with ID %s", roleID);
//			logger.debug(message);

			return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E", message, null),
					HttpStatus.OK);
		}

		Role roleFromDB = roleByRoleId.get();
		message = String.format("Fetched details for role id %s", roleID);
//		logger.debug(message);
//		logger.debug("Leaving findByRoleID");

		return new ResponseEntity<Response>(responseBuilder.createResponse("SU_DE_204", "S", message, roleFromDB),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> findByRole(String role) {
//		logger.debug("findByRole invoked");
		Optional<Role> RoleByName = roleRepository.findByRoleIgnoreCase(role);

		String message = null;
		if (!RoleByName.isPresent()) {
			message = String.format("Role does not exist with name %s", role);
//			logger.debug(message);
			return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E", message, null),
					HttpStatus.OK);
		}

		Role roleFromDB = RoleByName.get();
		message = String.format("Fetched details by role name %s", role);
//		logger.debug(message);
//		logger.debug("Leaving findByRole");

		return new ResponseEntity<Response>(responseBuilder.createResponse("SU_DE_204", "S", message, roleFromDB),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> findAllByStatusEnabled() {
//		logger.debug("findAllByStatusEnabled invoked");
		List<Role> roles = roleRepository.findByStatus();
		if (CollectionUtils.isEmpty(roles)) {
//			logger.debug("No roles found with status enabled");
			return new ResponseEntity<Response>(
					responseBuilder.createResponse("SU_DE_204", "S", "No roles found with status enabled.", null),
					HttpStatus.OK);
		}
		excludePlatformAdminRole(roles);
//		logger.debug("Fetched roles with status enabled - {}", roles.size());
//		logger.debug("Leaving findAllByStatusEnabled");
		return new ResponseEntity<Response>(
				responseBuilder.createResponse("SU_DE_204", "S", "Fetched roles with status enabled", roles),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> findBySearchCriteria(RoleSearchCriteria searchCriteria) {
//		logger.debug("findBySearchCriteria invoked");
		String message = null;

		List<Role> roles = null;

		Pageable paging;
		Page<Role> rolePage;

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

		long count = mongoTemplate.count(query, Role.class);
		query.with(paging);
		query.collation(Collation.of("en").strength(Collation.ComparisonLevel.secondary()));
		List<Role> roleList = mongoTemplate.find(query, Role.class);

		rolePage = new PageImpl<>(roleList, paging, count);

		if (rolePage.isEmpty()) {
			return new ResponseEntity<>(responseBuilder.createResponse("SU_DE_204", "E", "No page found.", null),
					HttpStatus.OK);
		}

		roles = rolePage.getContent();

		RolePageResponse response = new RolePageResponse();

		response.setTotalElementOfPage(rolePage.getNumberOfElements());
		response.setTotalRecords(rolePage.getTotalElements());
		response.setTotalPages(rolePage.getTotalPages());
		response.setCurrentPage(rolePage.getNumber());
		response.setRoles(roles);

		message = String.format("Role records found");
//		logger.debug(message);
//		logger.debug("leaving findBySearchCriteria");

		return new ResponseEntity<>(responseBuilder.createResponse("SU_DE_200", "S", message, response),
				HttpStatus.OK);
	}
	private List<Criteria> getSearchCriteria(RoleSearchCriteria searchCriteria) {
		List<Criteria> criteriaList = new ArrayList<>();
		if (StringUtils.isNotBlank(searchCriteria.getRole())) {
			criteriaList.add(Criteria.where("role").regex(".*" + searchCriteria.getRole() + ".*", "i"));
		}
		if (StringUtils.isNotBlank(searchCriteria.getStatus())) {
			criteriaList.add(Criteria.where("status").is(searchCriteria.getStatus()));
		}
		if (null != searchCriteria.getPermissions() && !CollectionUtils.isEmpty(searchCriteria.getPermissions())) {
			criteriaList.add(Criteria.where("permissions").in(searchCriteria.getPermissions()));
		}
		criteriaList.add(Criteria.where("role").ne(Constants.PLATFORM_ADMIN_ROLE));
		return criteriaList;
	}
	

}
