package com.mmc.nts.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mmc.nts.entity.Project;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {

	public Optional<Project> findByProjectIgnoreCase(String project);
	
	@Query(fields = "{ 'project' : 1 , 'documentStructure' :1 , 'roles' : 1 , 'updated': 1}")
	public List<Project> findByStatusAndRoles_Users(String string, String email);

	public List<Project> findByRoles_Role(final String roleName);

	@Query(fields = "{ 'project' : 1 , 'documentStructure' :1 , 'roles' : 1 }")
	public List<Project> findByRoles_Users(final String userName);
	
	public List<Project> findByStatus(String status);
	
	@Query(value="{ '_id' : ?0 }", fields="{ 'project' : 1}")
	public Optional<Project> findProjectById(String docId);
	
	@Query(value="{ '_id' : ?0 }", fields="{ 'project' : 1}")
	public List<Project> findAllByStatus(String status);
	
	@Query(value="{ 'status' : ?0 }", fields="{ 'project' : 1,'roles' : 1}")
	public List<Project> findProjectsByStatus(String status);

	public List<Project> findByStatusAndRoles_UsersIgnoreCase(String status, String userName);
	
}
