package com.mmc.nts.model.response;

import java.util.ArrayList;
import java.util.List;

import com.mmc.nts.entity.Role;

import lombok.Data;

@Data
public class RolePageResponse {

	private long totalRecords;

	private int totalPages;

	private int totalElementOfPage;

	private int currentPage;

	private List<Role> roles = new ArrayList<>();
}
