package com.mmc.nts.entity;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRoles {

	@NotEmpty(message = "role name must not be empty")
	private String role;

	private String status;

	@NotEmpty(message = "users list must not be empty")
	private List<@Email(message = "user name must be a well-formed email address for role") String> users;
}
