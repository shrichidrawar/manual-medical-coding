package com.mmc.nts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.util.CollectionUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import com.mmc.nts.constants.Constants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.mmc.nts.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import com.mmc.nts.model.Response;
import com.mmc.nts.model.ResponseBuilder;
import com.mmc.nts.model.UserSearchCriteria;
import com.mmc.nts.model.response.UserPageResponse;
import com.mmc.nts.repository.UserRepository;
import com.mmc.nts.service.UserService;
import com.mmc.nts.utility.DateUtil;
import com.mmc.nts.utility.HasherUtils;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ResponseBuilder responseBuilder;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public ResponseEntity<Response> createUser(User user) {
		Optional<User> userByUserEmail = userRepository.findByEmail(user.getEmail());
		String message = null;
		if (userByUserEmail.isPresent()) {
			message = String.format("User exists with email %s", user.getEmail());

			return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E", message, null),
					HttpStatus.OK);

		}
		String randomPassword = RandomStringUtils.randomAscii(8);
		user.setEmail(user.getEmail().toLowerCase());
		user.setCreated(DateUtil.getDateTimeStamp());
		user.setUpdated(DateUtil.getDateTimeStamp());
		user.setPassword(HasherUtils.getHash(randomPassword));
		user.setSystemPasswordResetStatus(Constants.STATUS_COMPLETE);
		user.setIsOtpValidationRequired(false);
		user = userRepository.save(user);
		message = String.format("User created with id %s", user.getId());

		return new ResponseEntity<Response>(responseBuilder.createResponse("SU_DE_204", "S", message, user),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> updateUser(User user) {

		Optional<User> userByUserId = userRepository.findById(user.getId());
		String message = null;
		if (!userByUserId.isPresent()) {
			message = String.format("User does not exist with ID %s", user.getId());

			return new ResponseEntity<>(responseBuilder.createResponse("EX_DE_304", "E", message, null), HttpStatus.OK);
		}
		User userFromDB = userByUserId.get();
		String userEmailFromDB = userFromDB.getEmail();

		if (!userEmailFromDB.equals(user.getEmail())) {
			Optional<User> userByUserEmail = userRepository.findByEmailIgnoreCase(user.getEmail());
			if (userByUserEmail.isPresent()) {
				message = String.format("User exists with email %s", user.getEmail());

				return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E", message, null),
						HttpStatus.OK);
			}
			userFromDB.setEmail(user.getEmail());
		}
		userFromDB.setName(user.getName());
		if (null != user.getPassword()) {
			userFromDB.setPassword(user.getPassword());
		}
		userFromDB.setStatus(user.getStatus());
		userFromDB.setUpdated(DateUtil.getDateTimeStamp());
		userFromDB.setAssignedProjects(user.getAssignedProjects());
		userFromDB.setIsOtpValidationRequired(user.getIsOtpValidationRequired());
		user = userRepository.save(userFromDB);
		message = String.format("Updated details for user id %s", user.getId());

		return new ResponseEntity<Response>(responseBuilder.createResponse("SU_DE_204", "S", "User updated", user),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> updateStatus(String userID, String status) {

		Optional<User> userByUserId = userRepository.findById(userID);
		String message = null;
		if (!userByUserId.isPresent()) {
			message = String.format("User does not exist with id %s", userID);

			return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E", message, null),
					HttpStatus.OK);
		}
		User user = userByUserId.get();
		if (!user.getStatus().equals(status)) {
			user.setStatus(status);
		}
		user.setUpdated(DateUtil.getDateTimeStamp());
		user = userRepository.save(user);
		message = String.format("Status updated for user %s", user.getEmail());

		return new ResponseEntity<Response>(responseBuilder.createResponse("SU_DE_204", "S", "Status updated", user),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> findAll() {

		List<User> userList = userRepository.findAll();
		if (CollectionUtils.isEmpty(userList)) {

			return new ResponseEntity<Response>(
					responseBuilder.createResponse("SU_DE_204", "S", "No users found.", null), HttpStatus.OK);
		}
		excludePlatformAdmin(userList);
		/*
		 * for (User user : userList) { findAssignedProjects(user); }
		 */

		return new ResponseEntity<Response>(
				responseBuilder.createResponse("SU_DE_204", "S", "Fetched users list", userList), HttpStatus.OK);
	}

	private void excludePlatformAdmin(List<User> users) {
		users.removeIf(u -> u.getEmail().equals(Constants.PLATFORM_ADMIN_EMAIL));
	}

	@Override
	public ResponseEntity<Response> findByUserID(String userId) {

		Optional<User> userByUserId = userRepository.findById(userId);
		String message = null;
		if (!userByUserId.isPresent()) {
			message = String.format("User does not exist with id %s", userId);

			return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E", message, null),
					HttpStatus.OK);
		}
		User user = userByUserId.get();
		// findAssignedProjects(user);
		message = String.format("Fetched details for user id %s", user.getId());

		return new ResponseEntity<Response>(responseBuilder.createResponse("SU_DE_204", "S", message, user),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> findByEmail(String email) {

		Optional<User> userByEmail = userRepository.findByEmailIgnoreCase(email);
		String message = null;

		if (!userByEmail.isPresent()) {
			message = String.format("User does not exist with email %s", email);

			return new ResponseEntity<Response>(responseBuilder.createResponse("EX_DE_304", "E", message, null),
					HttpStatus.OK);
		}

		User userFromDB = userByEmail.get();
		message = String.format("Fetched user details for email %s", email);

		return new ResponseEntity<Response>(responseBuilder.createResponse("SU_DE_204", "S", message, userFromDB),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> findAllByStatusEnabled() {

		List<User> users = userRepository.findAllByStatus();

		if (CollectionUtils.isEmpty(users)) {

			return new ResponseEntity<Response>(
					responseBuilder.createResponse("SU_DE_204", "S", "No users found with status enabled.", null),
					HttpStatus.OK);
		}
		excludePlatformAdmin(users);

		return new ResponseEntity<Response>(
				responseBuilder.createResponse("SU_DE_204", "S", "Fetched users with status enabled", users),
				HttpStatus.OK);
	}

	@Override
	public Boolean checkUserEnabled(String userId) {
		Optional<User> userOptional = userRepository.findByIdAndStatus(userId);
		return (userOptional.isPresent()) ? true : false;
	}

	@Override
	public User findByEmailAndStatus(String email) {
		Optional<User> user = userRepository.findByEmailAndStatus(email);
		return user.isPresent() ? user.get() : null;
	}

	@Override
	public ResponseEntity<Response> findBySearchCriteria(UserSearchCriteria searchCriteria) {

		String message = null;

		List<User> users = null;

		Pageable paging;
		Page<User> userPage;

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

		long count = mongoTemplate.count(query, User.class);
		query.with(paging);
		query.collation(Collation.of("en").strength(Collation.ComparisonLevel.secondary()));
		List<User> userList = mongoTemplate.find(query, User.class);

		userPage = new PageImpl<>(userList, paging, count);

		if (userPage.isEmpty()) {
			return new ResponseEntity<>(responseBuilder.createResponse("SU_DE_204", "E", "No page found.", null),
					HttpStatus.OK);
		}

		users = userPage.getContent();
		// excludePlatformAdmin(users);
		UserPageResponse response = new UserPageResponse();

		response.setTotalElementOfPage(userPage.getNumberOfElements());
		response.setTotalRecords(userPage.getTotalElements());
		response.setTotalPages(userPage.getTotalPages());
		response.setCurrentPage(userPage.getNumber());
		response.setUsers(users);

		message = String.format("User records found");

		return new ResponseEntity<>(responseBuilder.createResponse("SU_DE_200", "S", message, response), HttpStatus.OK);
	}

	private List<Criteria> getSearchCriteria(UserSearchCriteria searchCriteria) {
		List<Criteria> criteriaList = new ArrayList<>();
		if (StringUtils.isNotBlank(searchCriteria.getName())) {
			criteriaList.add(Criteria.where("name").regex(".*" + searchCriteria.getName() + ".*", "i"));
		}
		if (StringUtils.isNotBlank(searchCriteria.getEmail())) {
			criteriaList.add(Criteria.where("email").regex(".*" + searchCriteria.getEmail() + ".*", "i"));
		}
		if (StringUtils.isNotBlank(searchCriteria.getStatus())) {
			criteriaList.add(Criteria.where("status").is(searchCriteria.getStatus()));
		}
		criteriaList.add(Criteria.where("email").ne(Constants.PLATFORM_ADMIN_EMAIL));
		return criteriaList;
	}

}
