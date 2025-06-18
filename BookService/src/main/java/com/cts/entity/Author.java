package com.cts.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity

public class Author {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long authId;
	
	@NotEmpty
	private String authName;
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	private List<Book> books;
	
	private LocalDateTime authorCreatedDate;
	private boolean isAuthDeleted;
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
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	public LocalDateTime getAuthorCreatedDate() {
		return authorCreatedDate;
	}
	public void setAuthorCreatedDate(LocalDateTime authorCreatedDate) {
		this.authorCreatedDate = authorCreatedDate;
	}
	public boolean isAuthDeleted() {
		return isAuthDeleted;
	}
	public void setAuthDeleted(boolean isAuthDeleted) {
		this.isAuthDeleted = isAuthDeleted;
	}
	public Author(Long authId, @NotEmpty String authName, List<Book> books, LocalDateTime authorCreatedDate,
			boolean isAuthDeleted) {
		super();
		this.authId = authId;
		this.authName = authName;
		this.books = books;
		this.authorCreatedDate = authorCreatedDate;
		this.isAuthDeleted = isAuthDeleted;
		
		
		
	}
	public Author() {
		super();
		// TODO Auto-generated constructor stub
	}
}
