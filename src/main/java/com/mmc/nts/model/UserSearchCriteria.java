package com.mmc.nts.model;

import lombok.Data;

@Data
public class UserSearchCriteria {

	private String name;

	private String email;

	private String status;

	private int page;

	private int size;

	private boolean sortingOrder;

	private String sortingField;
}
