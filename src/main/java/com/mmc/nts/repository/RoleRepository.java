package com.mmc.nts.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mmc.nts.entity.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

	public Optional<Role> findByRoleIgnoreCase(String role);

	@Query(value="{ 'status' : 'enabled'}")
	public List<Role> findByStatus();

	@Query(value="{ 'status' : 'enabled'}", fields="{ 'role' : 1,'permissions' : 1}")
	public List<Role> findRoleByStatus();

	@Query(fields="{'permissions' : 1}")
	public List<Role> findRoleByRoleIn(Set<String> roles);
}
