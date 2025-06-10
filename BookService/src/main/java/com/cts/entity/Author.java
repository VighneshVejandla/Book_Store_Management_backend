package com.cts.entity;

import java.time.LocalDateTime;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authId;

//    @NotEmpty
    @NotNull
    
    private String authName;

    private LocalDateTime authorCreatedDate;
    private boolean isAuthDeleted;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Book> books;

	public String getAuthName() {
		return authName;
	}

	public Long getAuthId() {
		return authId;
	}

	public void setAuthId(Long authId) {
		this.authId = authId;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}
    
    
}
