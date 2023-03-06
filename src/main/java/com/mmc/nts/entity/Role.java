package com.mmc.nts.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Document(collection = "roles")
public class Role {

	@Id
	private String id;

	@NotEmpty(message = "role must not be empty")
	private String role;

	private String status;

	@NotEmpty(message = "permissions must not be empty")
	private List<String> permissions;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date created;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updated;

}
