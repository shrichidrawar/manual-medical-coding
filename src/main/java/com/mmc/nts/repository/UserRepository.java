package com.mmc.nts.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mmc.nts.entity.User;

public interface UserRepository extends MongoRepository<User, String>{

	Optional<User> findByEmail(String email);
	
	public Optional<User> findByEmailIgnoreCase(String email);
	
	@Query(value="{ 'status' : 'enabled'}")
	public List<User> findAllByStatus();
	
	@Query(value="{ 'id' : ?0 , 'status' : 'enabled'}")
	public Optional<User> findByIdAndStatus(String userId);
	
	@Query(value="{ 'email' : ?0, 'status' : 'enabled'}")
	public Optional<User> findByEmailAndStatus(String email);
	
	public Optional<User> findByPlatformAdmin(Boolean platformAdmin);
	
	@Query(value="{ 'email' : ?0,'systemPasswordResetStatus':'pending'}")
	public Optional<User> findByFirstLoginStatus(String email);
	
	@Query(value="{ 'email' : ?0,'status':'disabled'}", fields="{ 'status' : 1}")
	public Optional<User> findByStatusByEmail(String email);



}
