package com.mmc.nts.model;

import java.util.List;

import lombok.Data;

@Data
public class RoleSearchCriteria {

	private String role;

	private List<String> permissions;

	private String status;

	private int page;

	private int size;

	private boolean sortingOrder;

	private String sortingField;
}
