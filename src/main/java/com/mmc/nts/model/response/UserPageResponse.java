package com.mmc.nts.model.response;

import java.util.ArrayList;
import java.util.List;

import com.mmc.nts.entity.User;

import lombok.Data;


@Data
public class UserPageResponse {

	private long totalRecords;

	private int totalPages;

	private int totalElementOfPage;

	private int currentPage;

	private List<User> users = new ArrayList<>();
}
