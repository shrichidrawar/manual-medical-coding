package com.mmc.nts.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mmc.nts.entity.User;
import com.mmc.nts.model.Response;
import com.mmc.nts.model.UserSearchCriteria;

@Service
public interface UserService {

	public ResponseEntity<Response> createUser(User user);
	
	public ResponseEntity<Response> updateUser(User user);
	
	public ResponseEntity<Response> updateStatus(String userID, String status);
	
	public ResponseEntity<Response> findAll();
	
	public ResponseEntity<Response> findByUserID(String userId);
	
	public ResponseEntity<Response> findByEmail(String email);
	
	public ResponseEntity<Response> findAllByStatusEnabled();
	
	public Boolean checkUserEnabled(String userId);
	
	public User findByEmailAndStatus(String email);
	
	public ResponseEntity<Response> findBySearchCriteria(UserSearchCriteria searchCriteria);
}
