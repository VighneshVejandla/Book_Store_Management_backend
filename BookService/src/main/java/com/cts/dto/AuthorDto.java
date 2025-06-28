package com.cts.dto;

import com.cts.entity.Author;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthorDto {
	private Long authId;
	private String authName;
//	private List<BookDto> books;
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
	public AuthorDto(Long authId, String authName) {
		super();
		this.authId = authId;
		this.authName = authName;
	}


}

