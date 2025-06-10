package com.cts.dto;

import lombok.Data;

@Data
public class UserDTO {

	private Long userId;
	
	private String name;
	
	private String email;

	private String password;
//	private String role;
}

