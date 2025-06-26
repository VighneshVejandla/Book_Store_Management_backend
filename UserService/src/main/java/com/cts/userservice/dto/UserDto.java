package com.cts.userservice.dto;

import lombok.Data;

@Data
public class UserDto {

	private Long userId;
	
//	@NotEmpty
	private String name;
	
//	@NotEmpty
//	@Email
	private String email;
	
//	@NotEmpty
	private String password;

	private String role;
}
