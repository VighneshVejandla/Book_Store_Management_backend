package com.cts.userservice.dto;

import com.cts.userservice.entity.Profile;
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

	private Profile profile;

}
