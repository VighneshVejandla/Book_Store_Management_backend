package com.cts.bookmanagement.dto;

import lombok.Data;

@Data
public class UserDto {

	private Long userId;
	private String name;
	private String email;
	private String password;
//	private String role;
}
