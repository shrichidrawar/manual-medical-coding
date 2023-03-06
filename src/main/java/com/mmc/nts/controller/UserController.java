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

import com.mmc.nts.entity.User;
import com.mmc.nts.model.Response;
import com.mmc.nts.model.UserSearchCriteria;
import com.mmc.nts.repository.UserRepository;
import com.mmc.nts.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

//	@PostMapping("/create")
//	public ResponseEntity<Response> registerUser(@Valid @RequestBody User user) {
//
//		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//			return ResponseEntity.badRequest().body("Email address is already taken");
//		}
//		userRepository.save(user);
//
//		return ResponseEntity.ok("User registered successfully");
//	}
	@PostMapping()
	public ResponseEntity<Response> createUser(@Valid @RequestBody User user) {
		return userService.createUser(user);
	}

	@PutMapping()
	public ResponseEntity<Response> updateUser(@Valid @RequestBody User user) {
		return userService.updateUser(user);
	}

	@DeleteMapping(value = "/status/{userId}/{status}")
	public ResponseEntity<Response> updateStatus(@PathVariable String userId, @PathVariable String status) {
		return userService.updateStatus(userId, status);
	}

	@GetMapping()
	public ResponseEntity<Response> fetchUserList() {
		return userService.findAll();
	}

	@GetMapping(value = "/id/{userId}")
	public ResponseEntity<Response> fetchUserById(@PathVariable String userId) {
		return userService.findByUserID(userId);
	}

	@GetMapping(value = "/email/{Email}")
	public ResponseEntity<Response> fetchUserByEmail(@PathVariable @Email String Email) {
		return userService.findByEmail(Email);
	}

	@GetMapping(value = "/enabled")
	public ResponseEntity<Response> findAllByStatusEnabled() {
		return userService.findAllByStatusEnabled();
	}

	@PostMapping(value = "/search_criteria")
	public ResponseEntity<Response> findBySearchCriteria(@RequestBody UserSearchCriteria searchCriteria) {
		return userService.findBySearchCriteria(searchCriteria);
	}

}
