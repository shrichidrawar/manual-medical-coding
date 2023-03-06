package com.mmc.nts.model;

import lombok.Data;

@Data
public class ProjectSearchCriteria {

	private String project;

	private String status;

	private int page;

	private int size;

	private boolean sortingOrder;

	private String sortingField;
}
