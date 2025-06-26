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

	private String imageBase64;

}
