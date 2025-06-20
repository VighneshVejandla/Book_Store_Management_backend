package com.cts.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookId;
	
	@NotBlank(message = "Title must be blank")
	@Size(max = 200, message = "Title must not exceed 200 characters")
	private String title;
	
	@ManyToOne
	@JoinColumn(name = "author_id")
	private Author author;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	@NotNull(message = "Price must not be null")
	@DecimalMin(value = "0.01", message = "Price must be greater than 0")
	private Double price;
	
	@NotNull(message = "Stock quantity must not be null")
	@Min(value = 0, message = "Stock quantity cannot be negative")
	private int stockQuantity;
	private LocalDateTime bookCreatedDate;
	private boolean isBookDeleted;
	
	
	
	public LocalDateTime getBookCreatedDate() {
		return bookCreatedDate;
	}
	public void setBookCreatedDate(LocalDateTime bookCreatedDate) {
		this.bookCreatedDate = bookCreatedDate;
	}
	public boolean isBookDeleted() {
		return isBookDeleted;
	}
	public void setBookDeleted(boolean isBookDeleted) {
		this.isBookDeleted = isBookDeleted;
	}
	public Book(Long bookId, @NotBlank String title, Author author, Category category,
			@NotNull @Min(value = 0, message = "Must be more than 0") Double price,
			@NotNull @Min(value = 0, message = "Out of Stock") int stockQuantity, LocalDateTime bookCreatedDate,
			boolean isBookDeleted) {
		super();
		this.bookId = bookId;
		this.title = title;
		this.author = author;
		this.category = category;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.bookCreatedDate = bookCreatedDate;
		this.isBookDeleted = isBookDeleted;
	}
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
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
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
	public Book() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
//	@OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
//	private Inventory inventory;
	
//	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
//	private List<Review> review;
}
