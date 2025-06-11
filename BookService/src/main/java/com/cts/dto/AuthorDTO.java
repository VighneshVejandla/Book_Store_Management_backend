package com.cts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AuthorDTO {

	private Long authId;
//	@NotEmpty(message = "Author name must not be empty")
//	@NotNull(message = "Author name must not be null")
	private String authName;
	
	
	public Long getAuthId() {
		return authId;
	}
	public void setAuthId(Long authId) {
		this.authId = authId;
	}
	public String getAuthName() {
		return authName;
	}
	public void setAuthName(String authName) {
		this.authName = authName;
	}
}
