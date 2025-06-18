package com.cts.entity;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long catId;

	@NotBlank
	private String catName;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<Book> books;
	
	private LocalDateTime catCreatedDate;
	private boolean isCatDeleted;
	public Long getCatId() {
		return catId;
	}
	public void setCatId(Long catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	public LocalDateTime getCatCreatedDate() {
		return catCreatedDate;
	}
	public void setCatCreatedDate(LocalDateTime catCreatedDate) {
		this.catCreatedDate = catCreatedDate;
	}
	public boolean isCatDeleted() {
		return isCatDeleted;
	}
	public void setCatDeleted(boolean isCatDeleted) {
		this.isCatDeleted = isCatDeleted;
	}
	public Category(Long catId, @NotBlank String catName, List<Book> books, LocalDateTime catCreatedDate,
			boolean isCatDeleted) {
		super();
		this.catId = catId;
		this.catName = catName;
		this.books = books;
		this.catCreatedDate = catCreatedDate;
		this.isCatDeleted = isCatDeleted;
	}
	
	
	
	
}