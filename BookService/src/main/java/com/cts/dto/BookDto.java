package com.cts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
	private Long bookId;
	private String title;
	private Double price;
	private int stockQuantity;
	private String isbn;
	
	private AuthorDto author;
	private CategoryDto category;

}
