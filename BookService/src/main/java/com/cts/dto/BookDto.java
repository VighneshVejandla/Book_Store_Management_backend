package com.cts.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDto {
	private Long bookId;
	private String title;
	private Double price;
	private int stockQuantity;
	
	private AuthorDto author;
	private CategoryDto category;
	
	
	
	
	public Long getBookId() {
		return bookId;
	}
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public int getStockQuantity() {
		return stockQuantity;
	}
	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	public AuthorDto getAuthor() {
		return author;
	}
	public void setAuthor(AuthorDto author) {
		this.author = author;
	}
	public CategoryDto getCategory() {
		return category;
	}
	public void setCategory(CategoryDto category) {
		this.category = category;
	}
	public BookDto(Long bookId, String title, Double price, int stockQuantity, AuthorDto author, CategoryDto category) {
		super();
		this.bookId = bookId;
		this.title = title;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.author = author;
		this.category = category;
	}
	
	
	
	
}
