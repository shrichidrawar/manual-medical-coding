package com.mmc.nts.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Document(collection = "projects")
public class Project {

	@Id
	private String id;

	@NotEmpty(message = "project name must not be empty")
	private String project;

	@NotNull(message = "startDate must not be empty")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date startDate;

	@NotNull(message = "endDate must not be empty")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date endDate;

	@NotEmpty(message = "lead name must not be empty")
	@Email
	private String lead;

	private String status;

	@NotEmpty(message = "roles list must not be empty")
	@Valid
	private List<ProjectRoles> roles;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date created;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updated;
}
