package com.mmc.nts.model.response;

import java.util.ArrayList;
import java.util.List;

import com.mmc.nts.entity.Project;

import lombok.Data;

@Data
public class ProjectPageResponse {

	private long totalRecords;

	private int totalPages;

	private int totalElementOfPage;

	private int currentPage;

	private List<Project> projects = new ArrayList<>();
}
