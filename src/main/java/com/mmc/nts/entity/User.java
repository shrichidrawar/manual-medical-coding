package com.mmc.nts.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Document(collection = "data")
public class User {
	@Id
    
	private String id;

	private String name;

	@Indexed(unique = true)
	@Email
	@NotEmpty(message = "Email must not be empty")
	private String email;

	private String password;

	private String status;

	
	private List<AssignedProjects> assignedProjects;
	
	private String systemPasswordResetStatus;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date created;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updated;

	private Boolean platformAdmin;

	private String otp;

	private Date otpValidationTime;

	private Boolean isOtpValidationRequired;
}
