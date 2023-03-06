package com.mmc.nts.model;

import lombok.Data;


@Data
public class Response {

	//EX_DE_1001 // SU_DE_200
	private String responseCode;

	// E for exception/error S for success
	private String status;

	//error/succ message
	private String description;

	//return object
	private Object detail;
}
