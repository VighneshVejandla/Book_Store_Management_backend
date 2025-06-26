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

	private String isbn;
}
